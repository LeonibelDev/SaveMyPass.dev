<template>
  <div class="bg-[#F2F2F7] min-h-screen flex flex-col selection:bg-brand-orange/20 overflow-x-hidden">
    <PublicNavbar />
    <main class="flex-1 flex items-center justify-center p-6 py-12">
      <div class="w-full max-w-[480px] animate-fade-in">
        <div class="bg-white/80 backdrop-blur-xl rounded-[32px] p-10 shadow-2xl border border-white/20">
          
          <div class="text-center mb-10">
            <div class="w-16 h-16 bg-emerald-500 text-white flex items-center justify-center rounded-[18px] shadow-sm mx-auto mb-6">
              <i class="fa-solid fa-shield-check text-2xl"></i>
            </div>
            <h1 class="text-[28px] font-bold text-slate-900 tracking-tight leading-tight">New Password</h1>
            <p class="text-[15px] text-slate-400 font-medium mt-2">Use your recovery key to restore access</p>
          </div>

          <Transition name="fade">
            <div v-if="error" class="mb-6 p-4 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-[14px] font-semibold text-center">
              {{ error }}
            </div>
            <div v-else-if="success" class="mb-6 p-4 bg-emerald-50 border border-emerald-100 rounded-2xl text-emerald-600 text-[14px] font-semibold text-center">
              {{ success }}
            </div>
          </Transition>

          <form v-if="!success && metadata" @submit.prevent="handleReset" class="space-y-4">
            <div class="space-y-1">
              <label class="block text-[12px] font-bold text-slate-400 ml-1">Recovery Key</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-key text-[14px]"></i>
                </span>
                <input v-model="form.recoveryKey" type="text" required placeholder="XXXXXX-XXXXXX-XXXXXX"
                  class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-mono font-bold text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <div class="space-y-1">
              <label class="block text-[12px] font-bold text-slate-400 ml-1">New Master Password</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-lock text-[14px]"></i>
                </span>
                <input v-model="form.password" type="password" required placeholder="••••••••"
                  class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <div class="space-y-1">
              <label class="block text-[12px] font-bold text-slate-400 ml-1">Confirm New Password</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-check-double text-[14px]"></i>
                </span>
                <input v-model="form.confirmPassword" type="password" required placeholder="••••••••"
                  class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <button type="submit" :disabled="loading"
              class="w-full mt-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-3.5 rounded-xl transition-all active:scale-[0.98] flex items-center justify-center gap-2 text-[16px] shadow-sm disabled:opacity-60">
              <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
              {{ loading ? 'Resetting...' : 'Update Password' }}
            </button>
          </form>

          <div class="mt-8 text-center">
            <router-link to="/login" class="text-[14px] text-slate-400 font-bold hover:text-brand-orange transition-colors">
              <i class="fa-solid fa-arrow-left mr-2"></i>Back to login
            </router-link>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PublicNavbar from '@/components/PublicNavbar.vue'
import { authApi } from '@/api/auth'
import { 
  deriveMasterKeyAndAuthHash, 
  deriveKeyFromRecoveryString,
  unwrapKey,
  wrapKey
} from '@/crypto/aes'

const route = useRoute()
const router = useRouter()

const form = reactive({ recoveryKey: '', password: '', confirmPassword: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

const token = ref(route.query.token)
const metadata = ref(null)

onMounted(async () => {
  if (!token.value) {
    error.value = 'Invalid or missing reset token.'
    return
  }
  try {
    const res = await authApi.getVaultMetadata(token.value)
    metadata.value = res.data.data
  } catch (e) {
    error.value = 'Reset link is invalid or has expired.'
  }
})

async function handleReset() {
  if (form.password !== form.confirmPassword) {
    error.value = 'Passwords do not match.'
    return
  }

  loading.value = true
  error.value = null

  // Give the browser a chance to render the loading state
  await new Promise(resolve => setTimeout(resolve, 50))
  try {
    // 1. Derive keys
    const { key: masterKey, authHash } = await deriveMasterKeyAndAuthHash(form.password, metadata.value.salt)
    const recoveryKey = await deriveKeyFromRecoveryString(form.recoveryKey)

    // 2. Unwrap Vault Key using Recovery Key
    const vaultKey = await unwrapKey(recoveryKey, metadata.value.recoveryVaultKey)

    // 3. Re-wrap Vault Key using NEW Master Key
    const newEncryptedVaultKey = await wrapKey(masterKey, vaultKey)

    // 4. Send to server
    await authApi.resetPassword({
      token: token.value,
      newAuthHash: authHash,
      newSalt: metadata.value.salt,
      newEncryptedVaultKey
    })

    success.value = 'Password updated successfully! Redirecting to login...'
    setTimeout(() => router.push('/login'), 2000)
  } catch (e) {
    console.error("Reset failed detail:", e)
    if (e.name === 'OperationError') {
      error.value = 'Reset failed. Your recovery key seems to be incorrect.'
    } else {
      error.value = e?.response?.data?.message || 'Reset failed. Please try again later.'
    }
  } finally {
    loading.value = false
  }
}
</script>

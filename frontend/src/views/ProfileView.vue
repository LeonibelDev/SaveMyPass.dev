<template>
  <div class="flex-1 flex flex-col bg-[#F2F2F7] overflow-hidden selection:bg-brand-orange/20">

    <!-- Header -->
    <header class="h-16 border-b border-slate-200/60 bg-white/80 backdrop-blur-xl flex items-center px-6 z-20 flex-shrink-0">
      <div class="max-w-4xl mx-auto w-full flex items-center justify-between">
        <h1 class="text-[17px] font-bold text-slate-900">Settings</h1>
        <div class="w-8 h-8 rounded-full bg-slate-100 flex items-center justify-center text-[12px] font-bold text-slate-500">
          {{ initials }}
        </div>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto py-8 px-4 sm:px-6 no-scrollbar">
      <div class="max-w-2xl mx-auto space-y-8 animate-fade-in">

        <!-- Status Messages -->
        <Transition name="fade">
          <div v-if="error || success" class="mb-4">
            <div v-if="error" class="p-4 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-[14px] font-semibold flex items-center gap-3">
              <i class="fa-solid fa-circle-exclamation"></i>
              {{ error }}
            </div>
            <div v-if="success" class="p-4 bg-emerald-50 border border-emerald-100 rounded-2xl text-emerald-600 text-[14px] font-semibold flex items-center gap-3">
              <i class="fa-solid fa-circle-check"></i>
              {{ success }}
            </div>
          </div>
        </Transition>

        <!-- Account Section -->
        <div>
          <p class="text-[13px] font-semibold text-slate-500 uppercase tracking-wider ml-4 mb-2">Account</p>
          <div class="bg-white rounded-[20px] border border-slate-200/60 overflow-hidden shadow-sm divide-y divide-slate-100">
            <div class="px-5 py-4 flex items-center justify-between gap-4">
              <div class="flex items-center gap-4">
                <div class="w-8 h-8 rounded-lg bg-blue-500 text-white flex items-center justify-center">
                  <i class="fa-solid fa-user text-[14px]"></i>
                </div>
                <div>
                  <p class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-0.5">Name</p>
                  <p class="text-[16px] font-medium text-slate-900">{{ auth.name }}</p>
                </div>
              </div>
            </div>
            <div class="px-5 py-4 flex items-center justify-between gap-4">
              <div class="flex items-center gap-4">
                <div class="w-8 h-8 rounded-lg bg-emerald-500 text-white flex items-center justify-center">
                  <i class="fa-solid fa-envelope text-[14px]"></i>
                </div>
                <div>
                  <p class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-0.5">Email</p>
                  <p class="text-[16px] font-medium text-slate-900">{{ auth.email }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Security Section -->
        <div>
          <p class="text-[13px] font-semibold text-slate-500 uppercase tracking-wider ml-4 mb-2">Security & Master Password</p>
          <div class="bg-white rounded-[20px] border border-slate-200/60 overflow-hidden shadow-sm">
            <div class="px-5 py-6">
              <form @submit.prevent="handleChangePassword" class="space-y-4">
                <div>
                  <label class="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5 ml-1">Current Password</label>
                  <input v-model="form.oldPassword" type="password" required placeholder="••••••••"
                    class="w-full bg-slate-100/50 border-none rounded-xl px-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all" />
                </div>

                <div class="grid sm:grid-cols-2 gap-4">
                  <div>
                    <label class="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5 ml-1">New Password</label>
                    <input v-model="form.newPassword" type="password" required placeholder="••••••••"
                      class="w-full bg-slate-100/50 border-none rounded-xl px-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all" />
                  </div>
                  <div>
                    <label class="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1.5 ml-1">Confirm New</label>
                    <input v-model="form.confirmNewPassword" type="password" required placeholder="••••••••"
                      class="w-full bg-slate-100/50 border-none rounded-xl px-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all" />
                  </div>
                </div>

                <div class="pt-2">
                  <button type="submit" :disabled="loading"
                    class="w-full bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-3.5 rounded-xl transition-all active:scale-[0.98] flex items-center justify-center gap-2 text-[15px] disabled:opacity-60 shadow-sm">
                    <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
                    <i v-else class="fa-solid fa-key text-[14px]"></i>
                    {{ loading ? 'Updating...' : 'Change Master Password' }}
                  </button>
                  <p class="text-[12px] text-slate-400 text-center mt-4 px-4 font-medium leading-relaxed">
                    Changing your password will re-encrypt your entire vault. This may take a moment.
                  </p>
                </div>
              </form>
            </div>
          </div>
        </div>

        <!-- Privacy & Info -->
        <div>
          <p class="text-[13px] font-semibold text-slate-500 uppercase tracking-wider ml-4 mb-2">Privacy & Help</p>
          <div class="bg-white rounded-[20px] border border-slate-200/60 overflow-hidden shadow-sm divide-y divide-slate-100">
            <a href="https://savemypass.dev/security" target="_blank" class="px-5 py-4 flex items-center justify-between hover:bg-slate-50 active:bg-slate-100 transition-all group">
              <div class="flex items-center gap-4">
                <div class="w-8 h-8 rounded-lg bg-slate-800 text-white flex items-center justify-center">
                  <i class="fa-solid fa-shield-halved text-[14px]"></i>
                </div>
                <span class="text-[15px] font-medium text-slate-900">Security Whitepaper</span>
              </div>
              <i class="fa-solid fa-chevron-right text-slate-300 text-[12px] group-hover:translate-x-1 transition-transform"></i>
            </a>
            <div class="px-5 py-4 flex items-center justify-between">
              <div class="flex items-center gap-4">
                <div class="w-8 h-8 rounded-lg bg-slate-100 text-slate-500 flex items-center justify-center">
                  <i class="fa-solid fa-code-branch text-[14px]"></i>
                </div>
                <span class="text-[15px] font-medium text-slate-900">Version</span>
              </div>
              <span class="text-[14px] font-bold text-slate-400">1.2.0-stable</span>
            </div>
          </div>
        </div>

        <div class="text-center py-4">
          <p class="text-[12px] font-semibold text-slate-300 uppercase tracking-[0.2em]">SaveMyPass Zero-Knowledge Architecture</p>
        </div>

      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useVaultStore } from '@/stores/vault'

const auth = useAuthStore()
const vault = useVaultStore()
const form = reactive({ oldPassword: '', newPassword: '', confirmNewPassword: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

const initials = computed(() => {
  if (!auth.name) return '??'
  return auth.name.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()
})

async function handleChangePassword() {
  if (form.newPassword !== form.confirmNewPassword) {
    error.value = 'Passwords do not match.'
    return
  }

  if (form.newPassword.length < 8) {
    error.value = 'New password must be at least 8 characters.'
    return
  }

  loading.value = true
  error.value = null
  success.value = null

  try {
    await vault.rotateVault(form.newPassword)
    success.value = 'Vault re-encrypted and password updated successfully.'
    form.oldPassword = ''
    form.newPassword = ''
    form.confirmNewPassword = ''
  } catch (e) {
    console.error(e)
    error.value = e?.response?.data?.message || e.message || 'Failed to update password.'
  } finally {
    loading.value = false
  }
}
</script>
<template>
  <div class="bg-[#F2F2F7] min-h-screen flex flex-col selection:bg-brand-orange/20">

    <!-- Navbar -->
    <PublicNavbar />

    <!-- Main -->
    <main class="flex-1 flex items-center justify-center p-6 py-12">
      <div class="w-full max-w-[480px] animate-fade-in">
        <div class="bg-white/80 backdrop-blur-xl rounded-[32px] p-10 shadow-2xl border border-white/20">
          
          <div class="text-center mb-10">
            <h1 class="text-[32px] font-bold text-slate-900 tracking-tight leading-tight">Create Account</h1>
            <p class="text-[15px] text-slate-400 font-medium mt-2">Start your secure zero-knowledge vault</p>
          </div>

          <!-- Status Messages -->
          <Transition name="fade">
            <div v-if="error" class="mb-6 p-4 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-[14px] font-semibold text-center">
              {{ error }}
            </div>
            <div v-else-if="success" class="mb-6 p-4 bg-emerald-50 border border-emerald-100 rounded-2xl text-emerald-500 text-[14px] font-semibold text-center">
              {{ success }}
            </div>
          </Transition>

          <form @submit.prevent="handleSignup" class="space-y-4">
            
            <div class="space-y-1">
              <label class="block text-[12px] font-bold text-slate-400 ml-1">Full Name</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-user text-[14px]"></i>
                </span>
                <input v-model="form.nombreCompleto" type="text" required placeholder="John Doe"
                  class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <div class="space-y-1">
              <label class="block text-[12px] font-bold text-slate-400 ml-1">Email Address</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-envelope text-[14px]"></i>
                </span>
                <input v-model="form.email" type="email" required placeholder="name@example.com"
                  class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="space-y-1">
                <label class="block text-[12px] font-bold text-slate-400 ml-1">Master Password</label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                    <i class="fa-solid fa-lock text-[14px]"></i>
                  </span>
                  <input v-model="form.password" type="password" required placeholder="••••••••"
                    class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
                </div>
              </div>
              <div class="space-y-1">
                <label class="block text-[12px] font-bold text-slate-400 ml-1">Confirm</label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                    <i class="fa-solid fa-shield-check text-[14px]"></i>
                  </span>
                  <input v-model="form.passwordConfirmacion" type="password" required placeholder="••••••••"
                    class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
                </div>
              </div>
            </div>

            <button type="submit" :disabled="loading"
              class="w-full mt-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-3.5 rounded-xl transition-all active:scale-[0.98] flex items-center justify-center gap-2 text-[16px] shadow-sm disabled:opacity-60">
              <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
              {{ loading ? 'Creating account...' : 'Sign Up' }}
            </button>
          </form>

          <div class="mt-8 text-center">
            <p class="text-[14px] text-slate-400 font-medium">
              Already have an account?
              <router-link to="/login" class="text-brand-orange font-bold hover:opacity-70 transition-opacity ml-1">Log in</router-link>
            </p>
          </div>
        </div>

        <p class="text-center mt-8 text-[11px] text-slate-400 font-bold uppercase tracking-[0.25em]">
          Privacy First • Encryption by Default
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import PublicNavbar from '@/components/PublicNavbar.vue'
import { authApi } from '@/api/auth'
import { generateRandomSalt, deriveMasterKeyAndAuthHash } from '@/crypto/aes'

const router = useRouter()
const form = reactive({ nombreCompleto: '', email: '', password: '', passwordConfirmacion: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

async function handleSignup() {
  if (form.password !== form.passwordConfirmacion) {
    error.value = 'Passwords do not match'
    return
  }

  loading.value = true
  error.value = null
  success.value = null
  try {
    const salt = generateRandomSalt()
    const { authHash } = await deriveMasterKeyAndAuthHash(form.password, salt)

    await authApi.signup({
      nombreCompleto: form.nombreCompleto,
      email: form.email,
      password: authHash,
      passwordConfirmacion: authHash,
      salt: salt
    })

    success.value = 'Account created successfully!'
    setTimeout(() => router.push('/login'), 1500)
  } catch (e) {
    error.value = e?.response?.data?.message || 'Registration failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="bg-black min-h-screen flex flex-col text-slate-300 selection:bg-orange-500 selection:text-white">

    <header class="p-6">
      <div class="max-w-7xl mx-auto flex justify-between items-center">
        <a href="/" class="flex items-center gap-2 group">
          <div class="bg-white p-1.5 rounded-lg group-hover:bg-orange-500 transition-colors cursor-pointer">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-black group-hover:text-white transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
            </svg>
          </div>
          <span class="font-bold text-lg tracking-tight text-white">SaveMyPass<span class="text-orange-500">.dev</span></span>
        </a>
        <router-link to="/login" class="text-sm font-semibold text-slate-400 hover:text-white transition-colors">Log In</router-link>
      </div>
    </header>

    <main class="flex-1 flex items-center justify-center p-6 py-12">
      <div class="w-full max-w-[480px]">
        <div class="bg-[#0a0a0a] border border-white/10 rounded-[2.5rem] p-10 md:p-12 shadow-2xl">

          <div class="text-center mb-10">
            <h1 class="text-3xl font-extrabold text-white tracking-tight mb-2">Create account</h1>
            <p class="text-slate-500 font-medium text-sm">Security simplified. Start your vault today.</p>
          </div>

          <div v-if="error" class="mb-6 p-4 bg-rose-500/10 border border-rose-500/20 rounded-2xl text-rose-500 text-sm font-medium text-center">{{ error }}</div>
          <div v-if="success" class="mb-6 p-4 bg-emerald-500/10 border border-emerald-500/20 rounded-2xl text-emerald-500 text-sm font-medium text-center">{{ success }}</div>

          <form @submit.prevent="handleSignup" class="space-y-5">

            <!-- Full Name -->
            <div class="space-y-2">
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest ml-1">Full Name</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </span>
                <input v-model="form.nombreCompleto" type="text" required placeholder="John Doe"
                  class="w-full bg-black border border-white/10 rounded-2xl pl-12 pr-4 py-3.5 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600 text-sm" />
              </div>
            </div>

            <!-- Email -->
            <div class="space-y-2">
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest ml-1">Email Address</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207" />
                  </svg>
                </span>
                <input v-model="form.email" type="email" required placeholder="name@example.com"
                  class="w-full bg-black border border-white/10 rounded-2xl pl-12 pr-4 py-3.5 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600 text-sm" />
              </div>
            </div>

            <!-- Passwords grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="space-y-2">
                <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest ml-1">Password</label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-500">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                    </svg>
                  </span>
                  <input v-model="form.password" type="password" required placeholder="••••••••"
                    class="w-full bg-black border border-white/10 rounded-2xl pl-12 pr-4 py-3.5 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600 text-sm" />
                </div>
              </div>
              <div class="space-y-2">
                <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest ml-1">Confirm</label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-500">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                    </svg>
                  </span>
                  <input v-model="form.passwordConfirmacion" type="password" required placeholder="••••••••"
                    class="w-full bg-black border border-white/10 rounded-2xl pl-12 pr-4 py-3.5 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600 text-sm" />
                </div>
              </div>
            </div>

            <button type="submit" :disabled="loading"
              class="w-full bg-white hover:bg-slate-200 text-black font-bold py-4 rounded-2xl transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] active:scale-[0.98] mt-4 flex items-center justify-center gap-2 disabled:opacity-60">
              <svg v-if="loading" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
              </svg>
              {{ loading ? 'Creating account...' : 'Create Account' }}
            </button>
          </form>

          <div class="mt-10 text-center">
            <p class="text-sm text-slate-500 font-medium">
              Already have an account?
              <router-link to="/login" class="text-white hover:text-orange-500 font-bold ml-1 transition-colors">Log in</router-link>
            </p>
          </div>
        </div>

        <p class="text-center mt-8 text-[10px] text-slate-600 font-bold tracking-[0.2em] uppercase">
          Privacy first • Encryption by default
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@/api/auth'

const router = useRouter()
const form = reactive({ nombreCompleto: '', email: '', password: '', passwordConfirmacion: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

async function handleSignup() {
  loading.value = true
  error.value = null
  success.value = null
  try {
    await authApi.signup(form)
    success.value = 'Account created successfully!'
    setTimeout(() => router.push('/login'), 1500)
  } catch (e) {
    error.value = e?.response?.data?.message || 'Registration failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

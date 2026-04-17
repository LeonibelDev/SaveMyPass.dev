<template>
  <div class="bg-black min-h-screen flex flex-col text-slate-300 selection:bg-orange-500 selection:text-white">

    <!-- Header -->
    <header class="p-6">
      <div class="max-w-7xl mx-auto flex justify-between items-center">
        <a href="/" class="flex items-center gap-2 group">
          <div class="bg-white p-1.5 rounded-lg group-hover:bg-orange-500 transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-black group-hover:text-white transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
            </svg>
          </div>
          <span class="font-bold text-lg tracking-tight text-white">SaveMyPass<span class="text-orange-500">.dev</span></span>
        </a>
        <router-link to="/signup" class="text-sm font-semibold text-slate-400 hover:text-white transition-colors">Sign Up</router-link>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 flex items-center justify-center p-6">
      <div class="w-full max-w-[440px]">
        <div class="bg-[#0a0a0a] border border-white/10 rounded-[2.5rem] p-10 md:p-12 shadow-2xl">

          <div class="text-center mb-10">
            <h1 class="text-3xl font-extrabold text-white tracking-tight mb-2">Welcome back</h1>
            <p class="text-slate-500 font-medium">Your Digital Vault. Simplified.</p>
          </div>

          <!-- Error -->
          <div v-if="error" class="mb-6 p-4 bg-rose-500/10 border border-rose-500/20 rounded-2xl text-rose-500 text-sm font-medium text-center">
            {{ error }}
          </div>

          <form @submit.prevent="handleLogin" class="space-y-6">
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
                  class="w-full bg-black border border-white/10 rounded-2xl pl-12 pr-4 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
              </div>
            </div>

            <!-- Password -->
            <div class="space-y-2">
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest ml-1">Password</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                </span>
                <input v-model="form.password" type="password" required placeholder="••••••••"
                  class="w-full bg-black border border-white/10 rounded-2xl pl-12 pr-4 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
              </div>
            </div>

            <button type="submit" :disabled="loading"
              class="w-full bg-white hover:bg-slate-200 text-black font-bold py-4 rounded-2xl transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] active:scale-[0.98] mt-2 flex items-center justify-center gap-2 disabled:opacity-60">
              <svg v-if="loading" class="animate-spin h-4 w-4 text-black" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
              </svg>
              {{ loading ? 'Signing in...' : 'Log In' }}
            </button>
          </form>

          <div class="mt-10 text-center">
            <p class="text-sm text-slate-500 font-medium">
              Don't have an account?
              <router-link to="/signup" class="text-white hover:text-orange-500 font-bold ml-1 transition-colors">Create one</router-link>
            </p>
          </div>
        </div>

        <p class="text-center mt-8 text-xs text-slate-600 font-medium tracking-wide uppercase">
          &copy; 2026 SaveMyPass Security Systems
        </p>
      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '@/api/auth'
import { useAuthStore } from '@/stores/auth'
import { deriveKey } from '@/crypto/aes'

const router = useRouter()
const auth = useAuthStore()

const form = reactive({ email: '', password: '' })
const loading = ref(false)
const error = ref(null)

async function handleLogin() {
  loading.value = true
  error.value = null
  try {
    const res = await authApi.login({ email: form.email, password: form.password })
    const data = res.data.data
    const aesKey = await deriveKey(form.password, data.salt)
    auth.setSession(data, aesKey)
    router.push('/home')
  } catch (e) {
    error.value = e?.response?.data?.message || 'Invalid credentials. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

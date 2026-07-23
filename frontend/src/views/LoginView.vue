<template>
  <div class="bg-gradient-to-b from-slate-50 via-slate-50 to-slate-100 min-h-screen flex flex-col selection:bg-brand-orange/20 overflow-x-hidden">

    <!-- Navbar -->
    <PublicNavbar />

    <!-- Main -->
    <main class="flex-1 flex flex-col p-6 pt-28 pb-12 relative">
      <div class="absolute inset-0 z-0 bg-[radial-gradient(circle_at_center,rgba(249,115,22,0.02)_0,transparent_55%)] pointer-events-none"></div>

      <div class="m-auto w-full max-w-[400px] animate-fade-in z-10">
        <div class="bg-white/80 backdrop-blur-xl rounded-[32px] p-6 sm:p-8 shadow-[0_20px_50px_rgba(0,0,0,0.04)] border border-slate-200/40">
          
          <div class="text-center mb-10">
            <div class="w-14 h-14 bg-gradient-to-br from-orange-500 to-orange-600 text-white flex items-center justify-center rounded-2xl shadow-[0_4px_12px_rgba(249,115,22,0.25)] mx-auto mb-5">
              <i class="fa-solid fa-folder-open text-2xl"></i>
            </div>
            <h1 class="text-[30px] font-black text-slate-900 tracking-tight leading-tight font-outfit">Welcome Back</h1>
            <p class="text-[14px] text-slate-400 font-semibold mt-2">Log in to access your secure vault</p>
          </div>

          <!-- Error -->
          <Transition name="fade">
            <div v-if="error" class="mb-6 p-4 bg-rose-50 border border-rose-100 rounded-2xl text-rose-600 text-[13px] font-bold text-center">
              <i class="fa-solid fa-triangle-exclamation mr-1"></i> {{ error }}
            </div>
          </Transition>

          <form @submit.prevent="handleLogin" class="space-y-4">
            
            <div class="space-y-1.5">
              <label class="block text-[12px] font-bold text-slate-400 ml-1 uppercase tracking-wider">Email Address</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-envelope text-[13px]"></i>
                </span>
                <input v-model="form.email" type="email" required placeholder="name@example.com"
                  class="w-full bg-[#F1F5F9]/50 border border-slate-200/50 rounded-xl pl-11 pr-4 py-3 text-[14.5px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange-glow focus:border-brand-orange focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <div class="space-y-1.5">
              <label class="block text-[12px] font-bold text-slate-400 ml-1 uppercase tracking-wider">Master Password</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-lock text-[13px]"></i>
                </span>
                <input v-model="form.password" type="password" required placeholder="••••••••"
                  class="w-full bg-[#F1F5F9]/50 border border-slate-200/50 rounded-xl pl-11 pr-4 py-3 text-[14.5px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange-glow focus:border-brand-orange focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <button type="submit" :disabled="loading"
              class="w-full mt-6 bg-gradient-to-r from-orange-500 to-orange-600 hover:shadow-[0_10px_25px_rgba(249,115,22,0.3)] text-white font-bold py-3.5 rounded-xl transition-all active:scale-[0.98] flex items-center justify-center gap-2 text-[15px] shadow-[0_4px_12px_rgba(249,115,22,0.15)] disabled:opacity-60">
              <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
              {{ loading ? 'Signing in...' : 'Sign In' }}
            </button>
          </form>

          <div class="mt-8 text-center">
            <p class="text-[14px] text-slate-400 font-semibold">
              New here?
              <router-link to="/signup" class="text-brand-orange font-bold hover:opacity-75 transition-opacity ml-1">Create an account</router-link>
            </p>
          </div>
        </div>

        <p class="text-center mt-8 text-[11px] text-slate-400 font-bold uppercase tracking-[0.25em]">
          Zero-Knowledge Security Protocol
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
import { useAuthStore } from '@/stores/auth'
import { deriveMasterKeyAndAuthHash } from '@/crypto/aes'

const router = useRouter()
const auth = useAuthStore()

const form = reactive({ email: '', password: '' })
const loading = ref(false)
const error = ref(null)

async function handleLogin() {
  loading.value = true
  error.value = null
  try {
    const saltRes = await authApi.getSalt(form.email)
    const salt = saltRes.data.data
    if (salt === 'N/A') throw new Error('Invalid email or password.')

    const { key, authHash } = await deriveMasterKeyAndAuthHash(form.password, salt)
    const res = await authApi.login({ email: form.email, password: authHash })
    const data = res.data.data
    auth.setSession(data, key)
    router.push('/')
  } catch (e) {
    console.error(e)
    error.value = e?.response?.data?.message || e.message || 'Invalid credentials. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

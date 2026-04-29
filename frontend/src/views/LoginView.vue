<template>
  <div class="bg-[#F2F2F7] min-h-screen flex flex-col selection:bg-brand-orange/20">

    <!-- Navbar -->
    <PublicNavbar />

    <!-- Main -->
    <main class="flex-1 flex items-center justify-center p-6">
      <div class="w-full max-w-[420px] animate-fade-in">
        <div class="bg-white/80 backdrop-blur-xl rounded-[32px] p-10 shadow-2xl border border-white/20">
          
          <div class="text-center mb-10">
            <div class="w-16 h-16 bg-brand-orange text-white flex items-center justify-center rounded-[18px] shadow-sm mx-auto mb-6">
              <i class="fa-solid fa-key text-3xl"></i>
            </div>
            <h1 class="text-[32px] font-bold text-slate-900 tracking-tight leading-tight">Welcome Back</h1>
            <p class="text-[15px] text-slate-400 font-medium mt-2">Log in to access your secure vault</p>
          </div>

          <!-- Error -->
          <Transition name="fade">
            <div v-if="error" class="mb-6 p-4 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-[14px] font-semibold text-center">
              {{ error }}
            </div>
          </Transition>

          <form @submit.prevent="handleLogin" class="space-y-4">
            
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

            <button type="submit" :disabled="loading"
              class="w-full mt-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-3.5 rounded-xl transition-all active:scale-[0.98] flex items-center justify-center gap-2 text-[16px] shadow-sm disabled:opacity-60">
              <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
              {{ loading ? 'Signing in...' : 'Sign In' }}
            </button>
          </form>

          <div class="mt-8 text-center">
            <p class="text-[14px] text-slate-400 font-medium">
              New here?
              <router-link to="/signup" class="text-brand-orange font-bold hover:opacity-70 transition-opacity ml-1">Create an account</router-link>
            </p>
          </div>
        </div>

        <p class="text-center mt-8 text-[12px] text-slate-400 font-semibold uppercase tracking-[0.2em]">
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

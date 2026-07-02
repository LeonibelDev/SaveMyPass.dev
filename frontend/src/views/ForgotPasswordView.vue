<template>
  <div class="bg-[#F2F2F7] min-h-screen flex flex-col selection:bg-brand-orange/20 overflow-x-hidden">
    <PublicNavbar />
    <main class="flex-1 flex items-center justify-center p-6 py-12">
      <div class="w-full max-w-[420px] animate-fade-in">
        <div class="bg-white/80 backdrop-blur-xl rounded-[32px] p-10 shadow-2xl border border-white/20">
          
          <div class="text-center mb-10">
            <div class="w-16 h-16 bg-slate-800 text-white flex items-center justify-center rounded-[18px] shadow-sm mx-auto mb-6">
              <i class="fa-solid fa-envelope-open-text text-2xl"></i>
            </div>
            <h1 class="text-[28px] font-bold text-slate-900 tracking-tight leading-tight">Reset Password</h1>
            <p class="text-[15px] text-slate-400 font-medium mt-2">Enter your email to receive a recovery link</p>
          </div>

          <Transition name="fade">
            <div v-if="error" class="mb-6 p-4 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-[14px] font-semibold text-center">
              {{ error }}
            </div>
            <div v-else-if="success" class="mb-6 p-4 bg-emerald-50 border border-emerald-100 rounded-2xl text-emerald-600 text-[14px] font-semibold text-center">
              {{ success }}
            </div>
          </Transition>

          <form v-if="!success" @submit.prevent="handleSubmit" class="space-y-4">
            <div class="space-y-1">
              <label class="block text-[12px] font-bold text-slate-400 ml-1">Email Address</label>
              <div class="relative">
                <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                  <i class="fa-solid fa-envelope text-[14px]"></i>
                </span>
                <input v-model="email" type="email" required placeholder="name@example.com"
                  class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3.5 text-[16px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
              </div>
            </div>

            <button type="submit" :disabled="loading"
              class="w-full mt-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-3.5 rounded-xl transition-all active:scale-[0.98] flex items-center justify-center gap-2 text-[16px] shadow-sm disabled:opacity-60">
              <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
              {{ loading ? 'Sending...' : 'Send Reset Link' }}
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
import { ref } from 'vue'
import PublicNavbar from '@/components/PublicNavbar.vue'
import { authApi } from '@/api/auth'

const email = ref('')
const loading = ref(false)
const error = ref(null)
const success = ref(null)

async function handleSubmit() {
  loading.value = true
  error.value = null
  try {
    await authApi.forgotPassword(email.value)
    success.value = 'If an account exists, you will receive a reset link shortly.'
  } catch (e) {
    error.value = 'Failed to send reset link. Please try again later.'
  } finally {
    loading.value = false
  }
}
</script>

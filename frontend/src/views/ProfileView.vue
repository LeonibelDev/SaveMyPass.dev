<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F7F8FA] selection:bg-orange-100 selection:text-orange-900">

    <!-- Header -->
    <header class="h-16 border-b border-slate-100 flex items-center px-6 justify-between flex-shrink-0 bg-white z-10">
      <div class="flex items-center flex-1 max-w-xl">
        <h1 class="text-sm font-black text-slate-800 tracking-tight uppercase">
          Settings
        </h1>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto py-10 px-6">
      <div class="max-w-4xl mx-auto">

        <!-- Title -->
        <div class="mb-8">
          <h2 class="text-xl font-black text-slate-800 tracking-tight">
            Your account
          </h2>
          <p class="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em] mt-1">
            Manage personal data & security
          </p>
        </div>

        <!-- Error -->
        <div v-if="error" class="mb-6 animate-fade-in">
          <div
            class="p-3.5 bg-rose-50 border border-rose-100 rounded-xl text-rose-500 text-xs font-bold uppercase tracking-widest flex items-center gap-2">
            <i class="fa-solid fa-circle-exclamation"></i>
            {{ error }}
          </div>
        </div>

        <!-- Success -->
        <div v-if="success" class="mb-6 animate-fade-in">
          <div
            class="p-3.5 bg-emerald-50 border border-emerald-100 rounded-xl text-emerald-600 text-xs font-bold uppercase tracking-widest flex items-center gap-2">
            <i class="fa-solid fa-circle-check"></i>
            {{ success }}
          </div>
        </div>

        <div class="grid lg:grid-cols-2 gap-4">

          <!-- Info -->
          <section class="bg-white rounded-2xl border border-slate-100 overflow-hidden">
            <div class="px-6 py-5 border-b border-slate-50 flex items-center gap-3">
              <div class="w-8 h-8 bg-slate-50 border border-slate-100 rounded-xl flex items-center justify-center">
                <i class="fa-solid fa-user text-slate-400 text-[12px]"></i>
              </div>
              <div>
                <h3 class="text-sm font-bold text-slate-800">
                  Information
                </h3>
                <p class="text-[10px] font-black text-slate-400 uppercase tracking-widest">
                  Personal details
                </p>
              </div>
            </div>

            <div class="px-6 py-6 space-y-4">
              <div>
                <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 ml-1">
                  Full name
                </label>
                <div class="flex items-center gap-3 bg-slate-50 border border-slate-100 rounded-xl px-4 py-3">
                  <i class="fa-solid fa-id-card text-slate-300 text-[12px]"></i>
                  <span class="text-sm font-bold text-slate-800">
                    {{ auth.name }}
                  </span>
                </div>
              </div>

              <div>
                <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 ml-1">
                  Email address
                </label>
                <div class="flex items-center gap-3 bg-slate-50 border border-slate-100 rounded-xl px-4 py-3">
                  <i class="fa-solid fa-at text-slate-300 text-[12px]"></i>
                  <span class="text-sm font-bold text-slate-800">
                    {{ auth.email }}
                  </span>
                </div>
              </div>
            </div>
          </section>

          <!-- Security -->
          <section class="bg-white rounded-2xl border border-slate-100 overflow-hidden">
            <div class="px-6 py-5 border-b border-slate-50 flex items-center gap-3">
              <div class="w-8 h-8 bg-orange-50 border border-orange-100 rounded-xl flex items-center justify-center">
                <i class="fa-solid fa-lock text-brand-orange text-[12px]"></i>
              </div>
              <div>
                <h3 class="text-sm font-bold text-slate-800">
                  Security
                </h3>
                <p class="text-[10px] font-black text-slate-400 uppercase tracking-widest">
                  Master password
                </p>
              </div>
            </div>

            <form @submit.prevent="handleChangePassword" class="px-6 py-6 space-y-4">

              <div>
                <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 ml-1">
                  Current password
                </label>
                <input v-model="form.oldPassword" type="password" required placeholder="••••••••"
                  class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-3 text-sm text-slate-800 font-medium focus:bg-white focus:ring-2 focus:ring-brand-orange/20 focus:outline-none transition-all placeholder:text-slate-300" />
              </div>

              <div>
                <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 ml-1">
                  New password
                </label>
                <input v-model="form.newPassword" type="password" required placeholder="••••••••"
                  class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-3 text-sm text-slate-800 font-medium focus:bg-white focus:ring-2 focus:ring-brand-orange/20 focus:outline-none transition-all placeholder:text-slate-300" />
              </div>

              <div>
                <label class="block text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 ml-1">
                  Confirm new
                </label>
                <input v-model="form.confirmNewPassword" type="password" required placeholder="••••••••"
                  class="w-full bg-slate-50 border border-slate-100 rounded-xl px-4 py-3 text-sm text-slate-800 font-medium focus:bg-white focus:ring-2 focus:ring-brand-orange/20 focus:outline-none transition-all placeholder:text-slate-300" />
              </div>

              <button type="submit" :disabled="loading"
                class="w-full h-9 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-lg shadow-orange-100 active:scale-95 flex items-center justify-center gap-2 text-xs uppercase tracking-widest disabled:opacity-60 mt-2">
                <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
                <i v-else class="fa-solid fa-shield-halved text-[12px]"></i>
                {{ loading ? 'Updating...' : 'Update password' }}
              </button>

            </form>
          </section>

        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/auth'
import { deriveKey } from '@/crypto/aes'

const auth = useAuthStore()
const form = reactive({ oldPassword: '', newPassword: '', confirmNewPassword: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

async function handleChangePassword() {
  if (form.newPassword !== form.confirmNewPassword) {
    error.value = 'Passwords do not match.'
    return
  }

  loading.value = true
  error.value = null
  success.value = null

  try {
    const res = await authApi.changePassword(form)
    const newAuthData = res.data.data
    const newAesKey = await deriveKey(form.newPassword, newAuthData.salt)
    auth.updateKey(newAuthData, newAesKey)

    success.value = 'Vault re-encrypted successfully.'
    form.oldPassword = ''
    form.newPassword = ''
    form.confirmNewPassword = ''

  } catch (e) {
    error.value = e?.response?.data?.message || 'Failed to update password.'
  } finally {
    loading.value = false
  }
}
</script>
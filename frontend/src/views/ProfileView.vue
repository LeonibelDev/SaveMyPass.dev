<template>
  <div class="bg-black text-slate-300 min-h-screen flex selection:bg-orange-500 selection:text-white">

    <!-- Sidebar -->
    <aside class="hidden md:flex flex-col w-64 border-r border-white/10 p-8 bg-[#0a0a0a]">
      <div class="flex items-center gap-2 mb-10">
        <div class="bg-white p-1.5 rounded-lg">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-black" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
          </svg>
        </div>
        <span class="font-bold text-lg tracking-tight text-white">SaveMyPass<span class="text-orange-500">.dev</span></span>
      </div>

      <!-- Nav links -->
      <nav class="space-y-1 flex-1">
        <router-link to="/home" class="flex items-center gap-3 px-4 py-3 rounded-2xl text-slate-500 hover:bg-white/5 hover:text-white transition-colors font-semibold text-sm">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
          </svg>
          Dashboard
        </router-link>
        <router-link to="/profile" class="flex items-center gap-3 px-4 py-3 rounded-2xl bg-white/10 text-white font-semibold text-sm">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          Profile
        </router-link>
      </nav>

      <!-- User footer -->
      <div class="border-t border-white/10 pt-6">
        <div class="flex items-center gap-3">
          <div class="w-9 h-9 bg-white/10 rounded-xl flex items-center justify-center text-white font-bold text-sm">
            {{ initials }}
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-xs font-bold text-slate-500 truncate">{{ auth.name }}</p>
            <p class="text-sm font-bold truncate text-white">{{ auth.email }}</p>
          </div>
        </div>
        <button @click="handleLogout" class="text-xs text-rose-500 font-bold mt-3 inline-block hover:underline">Log Out</button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="flex-1 p-8 md:p-10 max-w-5xl">
      <header class="mb-12">
        <h1 class="text-3xl font-extrabold text-white tracking-tight">Profile Settings</h1>
        <p class="text-slate-500 mt-1">Manage your account information and security preferences.</p>
      </header>

      <!-- Alerts -->
      <div v-if="error" class="mb-8 p-4 bg-rose-500/10 border border-rose-500/20 rounded-2xl flex items-center gap-3 text-rose-500">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 flex-shrink-0" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
        </svg>
        <span class="font-semibold text-sm">{{ error }}</span>
      </div>

      <div v-if="success" class="mb-8 p-4 bg-emerald-500/10 border border-emerald-500/20 rounded-2xl flex items-center gap-3 text-emerald-500">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 flex-shrink-0" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
        </svg>
        <span class="font-semibold text-sm">{{ success }}</span>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">

        <!-- User Info Card -->
        <section class="bg-[#0a0a0a] border border-white/10 rounded-[2rem] p-8 shadow-2xl">
          <div class="flex items-center gap-4 mb-8">
            <div class="w-12 h-12 bg-white/5 rounded-2xl flex items-center justify-center text-slate-300 border border-white/10">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-bold text-white">Personal Information</h2>
              <p class="text-slate-500 text-sm font-medium">Your public and account details.</p>
            </div>
          </div>

          <div class="space-y-6">
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Full Name</label>
              <div class="bg-black border border-white/10 rounded-2xl px-5 py-4 text-white font-semibold">{{ auth.name }}</div>
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Email Address</label>
              <div class="bg-black border border-white/10 rounded-2xl px-5 py-4 text-white font-semibold">{{ auth.email }}</div>
            </div>
          </div>
        </section>

        <!-- Change Password Card -->
        <section class="bg-[#0a0a0a] border border-white/10 rounded-[2rem] p-8 shadow-2xl">
          <div class="flex items-center gap-4 mb-8">
            <div class="w-12 h-12 bg-rose-500/10 rounded-2xl flex items-center justify-center text-rose-500 border border-rose-500/20">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-bold text-white">Security</h2>
              <p class="text-slate-500 text-sm font-medium">Update your account password.</p>
            </div>
          </div>

          <form @submit.prevent="handleChangePassword" class="space-y-5">
            <div class="space-y-2">
              <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Current Password</label>
              <input v-model="form.oldPassword" type="password" required placeholder="••••••••"
                class="w-full bg-black border border-white/10 rounded-2xl px-5 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
            </div>
            <div class="space-y-4">
              <div class="space-y-2">
                <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">New Password</label>
                <input v-model="form.newPassword" type="password" required placeholder="••••••••"
                  class="w-full bg-black border border-white/10 rounded-2xl px-5 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
              </div>
              <div class="space-y-2">
                <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Confirm New Password</label>
                <input v-model="form.confirmNewPassword" type="password" required placeholder="••••••••"
                  class="w-full bg-black border border-white/10 rounded-2xl px-5 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
              </div>
            </div>
            <button type="submit" :disabled="loading"
              class="w-full bg-white hover:bg-slate-200 text-black font-bold py-4 rounded-2xl transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] active:scale-[0.98] mt-4 flex items-center justify-center gap-2 disabled:opacity-60">
              <svg v-if="loading" class="animate-spin h-4 w-4 text-black" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
              </svg>
              {{ loading ? 'Updating…' : 'Update Password' }}
            </button>
          </form>
        </section>
      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/auth'
import { deriveKey } from '@/crypto/aes'

const router = useRouter()
const auth = useAuthStore()
const form = reactive({ oldPassword: '', newPassword: '', confirmNewPassword: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

const initials = computed(() =>
  auth.name?.split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase() || '?'
)

function handleLogout() {
  auth.logout()
  router.push('/login')
}

async function handleChangePassword() {
  loading.value = true
  error.value = null
  success.value = null
  try {
    const res = await authApi.changePassword(form)
    const newAuthData = res.data.data
    const newAesKey = await deriveKey(form.newPassword, newAuthData.salt)
    auth.updateKey(newAuthData, newAesKey)
    success.value = 'Password updated and vault re-encrypted successfully.'
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

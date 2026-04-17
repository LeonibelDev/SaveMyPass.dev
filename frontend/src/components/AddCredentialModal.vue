<template>
  <div class="fixed inset-0 z-[60] flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-black/60 backdrop-blur-md" @click="$emit('close')"></div>
    <div class="relative w-full max-w-md bg-[#0a0a0a] rounded-[2rem] p-10 shadow-[0_0_40px_rgba(0,0,0,0.5)] border border-white/10">
      <h2 class="text-2xl font-extrabold text-white mb-1 tracking-tight">New Credential</h2>
      <p class="text-slate-500 text-sm mb-8 font-medium">Add a new account to your secure vault.</p>

      <div v-if="error" class="mb-6 p-4 bg-rose-500/10 border border-rose-500/20 rounded-2xl text-rose-500 text-sm font-medium">{{ error }}</div>

      <form @submit.prevent="handleSubmit" class="space-y-5">
        <div>
          <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Platform Name</label>
          <input v-model="form.site" type="text" required placeholder="example.com" autofocus
            class="w-full bg-black border border-white/10 rounded-2xl px-5 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
        </div>
        <div>
          <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Username / Email</label>
          <input v-model="form.username" type="text" required placeholder="john@doe.com"
            class="w-full bg-black border border-white/10 rounded-2xl px-5 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
        </div>
        <div>
          <label class="block text-xs font-bold text-slate-500 uppercase tracking-widest mb-2 ml-1">Secret Password</label>
          <div class="relative flex items-center">
            <input v-model="form.password" :type="showPw ? 'text' : 'password'" required placeholder="••••••••"
              class="w-full bg-black border border-white/10 rounded-2xl pl-5 pr-28 py-4 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600" />
            <div class="absolute right-2 flex items-center gap-1">
              <button type="button" @click="showPw = !showPw"
                class="px-3 py-2 rounded-xl text-slate-400 hover:text-white hover:bg-white/5 transition-colors text-xs font-bold">
                {{ showPw ? 'Hide' : 'Show' }}
              </button>
              <button type="button" @click="generatePassword" title="Generate secure password"
                class="p-2 rounded-xl text-slate-400 hover:text-white hover:bg-white/5 transition-colors flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                   <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
              </button>
            </div>
          </div>
        </div>

        <button type="submit" :disabled="loading"
          class="w-full bg-white hover:bg-slate-200 text-black font-bold py-4 rounded-2xl transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] mt-6 flex items-center justify-center gap-2 disabled:opacity-60 active:scale-[0.98]">
          <svg v-if="loading" class="animate-spin h-4 w-4 text-black" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
          </svg>
          {{ loading ? 'Saving…' : 'Securely Save' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useVaultStore } from '@/stores/vault'

const emit = defineEmits(['close'])
const vault = useVaultStore()

const form = reactive({ site: '', username: '', password: '' })
const loading = ref(false)
const error = ref(null)
const showPw = ref(false)

function generatePassword() {
  const chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()'
  const arr = new Uint32Array(20)
  crypto.getRandomValues(arr)
  form.password = Array.from(arr).map(n => chars[n % chars.length]).join('')
  showPw.value = true
}

async function handleSubmit() {
  loading.value = true
  error.value = null
  try {
    await vault.addCredential(form.site, form.username, form.password)
    emit('close')
  } catch (e) {
    error.value = e?.response?.data?.message || 'Failed to save credential.'
  } finally {
    loading.value = false
  }
}
</script>

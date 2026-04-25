<template>
  <div class="fixed inset-0 z-[60] flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-black/50 backdrop-blur-md" @click="$emit('close')" />

    <div class="relative w-full max-w-[420px]">
      <div class="bg-white rounded-[2rem] p-8 border border-slate-100">

        <!-- Badge -->
        <div class="inline-flex items-center gap-1.5 bg-orange-50 border border-orange-200 text-orange-600 rounded-full px-3 py-1 text-[10px] font-black uppercase tracking-widest mb-4">
          <i class="fa-solid fa-plus text-[9px]"></i>
          New entry
        </div>

        <h2 class="text-2xl font-black text-slate-900 tracking-tight mb-1">New credential</h2>
        <p class="text-[10px] text-slate-400 font-bold uppercase tracking-widest mb-7">
          Add an account to your secure vault
        </p>

        <!-- Error -->
        <div v-if="error" class="mb-5 p-3.5 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-xs font-bold text-center flex items-center justify-center gap-2">
          <i class="fa-solid fa-circle-exclamation"></i> {{ error }}
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-5">

          <div class="space-y-1.5">
            <label class="block text-[10px] font-black text-slate-500 uppercase tracking-widest ml-0.5">Platform name</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-globe text-[13px]"></i>
              </span>
              <input v-model="form.site" type="text" required placeholder="e.g. GitHub, Notion…" autofocus
                class="w-full bg-slate-50 border border-slate-200 rounded-2xl pl-11 pr-4 py-3.5 text-sm font-medium text-slate-900 focus:bg-white focus:border-orange-200 focus:ring-4 focus:ring-orange-50 focus:outline-none transition-all placeholder:text-slate-400" />
            </div>
          </div>

          <div class="space-y-1.5">
            <label class="block text-[10px] font-black text-slate-500 uppercase tracking-widest ml-0.5">Username / email</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-at text-[13px]"></i>
              </span>
              <input v-model="form.username" type="text" required placeholder="john@doe.com"
                class="w-full bg-slate-50 border border-slate-200 rounded-2xl pl-11 pr-4 py-3.5 text-sm font-medium text-slate-900 focus:bg-white focus:border-orange-200 focus:ring-4 focus:ring-orange-50 focus:outline-none transition-all placeholder:text-slate-400" />
            </div>
          </div>

          <div class="space-y-1.5">
            <label class="block text-[10px] font-black text-slate-500 uppercase tracking-widest ml-0.5">Secret password</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-lock text-[13px]"></i>
              </span>
              <input v-model="form.password" :type="showPw ? 'text' : 'password'" required placeholder="••••••••"
                class="w-full bg-slate-50 border border-slate-200 rounded-2xl pl-11 pr-24 py-3.5 text-sm font-medium text-slate-900 focus:bg-white focus:border-orange-200 focus:ring-4 focus:ring-orange-50 focus:outline-none transition-all placeholder:text-slate-400" />
              <div class="absolute inset-y-0 right-0 flex items-center gap-0.5 pr-2">
                <button type="button" @click="showPw = !showPw"
                  class="px-2 py-1.5 text-slate-400 hover:text-orange-500 hover:bg-orange-50 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all flex items-center gap-1">
                  <i :class="showPw ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" class="text-[11px]"></i>
                  {{ showPw ? 'Hide' : 'Show' }}
                </button>
                <button type="button" @click="generatePassword"
                  class="p-1.5 rounded-xl text-slate-400 hover:text-orange-500 hover:bg-orange-50 transition-all">
                  <i class="fa-solid fa-bolt text-[12px]"></i>
                </button>
              </div>
            </div>
          </div>

          <button type="submit" :disabled="loading"
            class="w-full bg-brand-orange hover:bg-brand-orange-hover text-white font-black py-3.5 rounded-2xl transition-all shadow-lg shadow-orange-100 active:scale-[0.98] flex items-center justify-center gap-2 text-[11px] uppercase tracking-widest disabled:opacity-60">
            <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
            <i v-else class="fa-solid fa-shield-halved text-[12px]"></i>
            {{ loading ? 'Saving…' : 'Securely save' }}
          </button>

        </form>

        <div class="mt-6 text-center">
          <p class="text-[10px] text-slate-400 font-black uppercase tracking-widest">
            Changed your mind?
            <button type="button" @click="$emit('close')" class="text-brand-orange hover:text-brand-orange-hover ml-1 transition-colors">Discard</button>
          </p>
        </div>

      </div>
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

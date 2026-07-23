<template>
  <div class="fixed inset-0 z-[60] flex items-center justify-center p-6">
    <Transition name="fade">
      <div class="absolute inset-0 bg-black/20 backdrop-blur-sm" @click="$emit('close')" />
    </Transition>

    <div class="relative w-full max-w-[400px] animate-scale-in">
      <div class="bg-white/95 backdrop-blur-xl rounded-[28px] p-8 shadow-2xl border border-slate-150">

        <h2 class="text-[22px] font-black text-slate-900 tracking-tight mb-1 text-center font-outfit">New Credential</h2>
        <p class="text-[14px] text-slate-400 font-semibold mb-8 text-center">
          Add an account to your secure vault
        </p>

        <!-- Error -->
        <div v-if="error"
          class="mb-6 p-3 bg-rose-50 border border-rose-100 rounded-xl text-rose-600 text-[13px] font-bold text-center">
          {{ error }}
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-4">

          <div class="space-y-1.5">
            <label class="block text-[11px] font-bold text-slate-405 uppercase tracking-wider ml-1 font-outfit">Platform</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                <img v-if="form.site"
                  :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${form.site}&size=64`"
                  :alt="form.site" class="w-5 h-5 object-contain" @error="$event.target.style.display = 'none'" />
                <i v-else class="fa-solid fa-globe text-[14px]"></i>
              </span>
              <input v-model="form.site" type="text" required placeholder="e.g. github.com" autofocus
                class="w-full bg-[#F1F5F9]/50 border border-slate-200/50 rounded-xl pl-10 pr-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange-glow focus:border-brand-orange focus:outline-none transition-all placeholder:text-slate-400" />
            </div>
          </div>

          <div class="space-y-1.5">
            <label class="block text-[11px] font-bold text-slate-405 uppercase tracking-wider ml-1 font-outfit">Username / Email</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-envelope text-[14px]"></i>
              </span>
              <input v-model="form.username" type="text" required placeholder="john@example.com"
                class="w-full bg-[#F1F5F9]/50 border border-slate-200/50 rounded-xl pl-10 pr-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange-glow focus:border-brand-orange focus:outline-none transition-all placeholder:text-slate-400" />
            </div>
          </div>

          <div class="space-y-1.5">
            <label class="block text-[11px] font-bold text-slate-405 uppercase tracking-wider ml-1 font-outfit">Password</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-lock text-[14px]"></i>
              </span>
              <input v-model="form.password" :type="showPw ? 'text' : 'password'" required placeholder="Your password"
                class="w-full bg-[#F1F5F9]/50 border border-slate-200/50 rounded-xl pl-10 pr-20 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange-glow focus:border-brand-orange focus:outline-none transition-all placeholder:text-slate-400" />
              <div class="absolute inset-y-0 right-0 flex items-center pr-2">
                <button type="button" @click="showPw = !showPw"
                  class="p-2 text-slate-400 hover:text-brand-orange transition-colors">
                  <i :class="showPw ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" class="text-[14px]"></i>
                </button>
                <button type="button" @click="generatePassword"
                  class="p-2 text-slate-400 hover:text-brand-orange transition-colors">
                  <i class="fa-solid fa-bolt-lightning text-[13px]"></i>
                </button>
              </div>
            </div>
          </div>

          <button type="submit" :disabled="loading"
            class="w-full mt-6 bg-gradient-to-r from-orange-500 to-orange-600 hover:shadow-[0_8px_20px_rgba(249,115,22,0.25)] text-white font-bold py-3.5 rounded-xl transition-all shadow-[0_4px_10px_rgba(249,115,22,0.15)] active:scale-[0.98] flex items-center justify-center gap-2 text-[15px] disabled:opacity-60 font-outfit">
            <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
            {{ loading ? 'Saving…' : 'Save Credential' }}
          </button>

          <button type="button" @click="$emit('close')"
            class="w-full py-2 text-brand-orange font-bold text-[14px] hover:opacity-75 transition-opacity font-outfit">
            Cancel
          </button>

        </form>

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

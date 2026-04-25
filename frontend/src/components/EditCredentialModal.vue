<template>
  <div class="fixed inset-0 z-[60] flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-slate-900/50 backdrop-blur-sm" @click="$emit('close')" />

    <div class="relative w-full max-w-[420px]">
      <div class="bg-white rounded-[2rem] p-8 border border-slate-100">

        <!-- Badge -->
        <div
          class="inline-flex items-center gap-1.5 bg-red-50 border border-red-200 text-red-500 rounded-full px-3 py-1 text-[10px] font-black uppercase tracking-widest mb-4">
          <i class="fa-solid fa-pen text-[9px]"></i>
          Editing
        </div>

        <h2 class="text-2xl font-black text-slate-900 tracking-tight mb-1">Edit credential</h2>
        <p class="text-[10px] text-slate-400 font-bold uppercase tracking-widest mb-7">
          Update the info for this account
        </p>

        <!-- Error -->
        <div v-if="error"
          class="mb-5 p-3.5 bg-rose-50 border border-rose-100 rounded-2xl text-rose-500 text-xs font-bold text-center flex items-center justify-center gap-2">
          <i class="fa-solid fa-circle-exclamation"></i> {{ error }}
        </div>

        <!-- Site chip (read-only) -->
        <div class="flex items-center gap-3 bg-slate-50 border border-slate-200 rounded-2xl px-4 py-3 mb-6">
          <div
            class="w-9 h-9 rounded-xl bg-white border border-slate-200 flex items-center justify-center flex-shrink-0 overflow-hidden">
            <img
              :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${credential.site}&size=64`"
              :alt="credential.site" class="w-5 h-5 object-contain" @error="$event.target.style.display = 'none'" />
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-sm font-black text-slate-900 tracking-tight truncate">{{ credential.site }}</p>
            <p class="text-[11px] text-slate-400 font-medium">
              Last updated {{ new Date(credential.updatedAt).toLocaleDateString(undefined, {
                month: 'short', day:
                  'numeric', year: 'numeric' }) }}
            </p>
          </div>
          <span
            class="inline-flex items-center gap-1 bg-emerald-50 border border-emerald-200 text-emerald-600 rounded-lg px-2.5 py-1 text-[10px] font-black uppercase tracking-widest flex-shrink-0">
            <i class="fa-solid fa-check text-[9px]"></i> Saved
          </span>
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-5">

          <div class="space-y-1.5">
            <label class="block text-[10px] font-black text-slate-500 uppercase tracking-widest ml-0.5">Username /
              email</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-at text-[13px]"></i>
              </span>
              <input v-model="form.username" type="text" required
                class="w-full bg-slate-50 border border-slate-200 rounded-2xl pl-11 pr-4 py-3.5 text-sm font-medium text-slate-900 focus:bg-white focus:border-orange-200 focus:ring-4 focus:ring-orange-50 focus:outline-none transition-all" />
            </div>
          </div>

          <div class="space-y-1.5">
            <div class="flex items-center justify-between ml-0.5 mb-0">
              <label class="text-[10px] font-black text-slate-500 uppercase tracking-widest">Secret password</label>
              <button type="button" @click="generatePassword"
                class="text-[10px] font-black text-brand-orange hover:text-brand-orange-hover uppercase tracking-widest transition-colors">
                Generate new
              </button>
            </div>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-lock text-[13px]"></i>
              </span>
              <input v-model="form.password" :type="showPw ? 'text' : 'password'" required
                class="w-full bg-slate-50 border border-slate-200 rounded-2xl pl-11 pr-20 py-3.5 text-sm font-medium text-slate-900 focus:bg-white focus:border-orange-200 focus:ring-4 focus:ring-orange-50 focus:outline-none transition-all" />
              <div class="absolute inset-y-0 right-0 flex items-center pr-2">
                <button type="button" @click="showPw = !showPw"
                  class="px-2 py-1.5 text-slate-400 hover:text-orange-500 hover:bg-orange-50 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all flex items-center gap-1">
                  <i :class="showPw ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" class="text-[11px]"></i>
                  {{ showPw ? 'Hide' : 'Show' }}
                </button>
              </div>
            </div>
          </div>

          <button type="submit" :disabled="loading"
            class="w-full bg-brand-orange hover:bg-brand-orange-hover text-white font-black py-3.5 rounded-2xl transition-all shadow-lg shadow-orange-100 active:scale-[0.98] flex items-center justify-center gap-2 text-[11px] uppercase tracking-widest disabled:opacity-60">
            <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
            <i v-else class="fa-solid fa-floppy-disk text-[12px]"></i>
            {{ loading ? 'Saving…' : 'Save changes' }}
          </button>

        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useVaultStore } from '@/stores/vault'

const props = defineProps({ credential: { type: Object, required: true } })
const emit = defineEmits(['close'])
const vault = useVaultStore()

const form = reactive({
  site: props.credential.site,
  username: props.credential.username,
  password: props.credential.password,
})
const loading = ref(false)
const error = ref(null)
const showPw = ref(false)

async function handleSubmit() {
  loading.value = true
  error.value = null
  try {
    await vault.updateCredential(props.credential.id, form.site, form.username, form.password)
    emit('close')
  } catch (e) {
    error.value = e?.response?.data?.message || 'Failed to update credential.'
  } finally {
    loading.value = false
  }
}
</script>

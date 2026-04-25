<template>
  <div class="bg-white border border-slate-200 rounded-3xl p-6 group hover:border-brand-orange hover:shadow-xl hover:shadow-orange-100/50 transition-all duration-300 flex flex-col justify-between">

    <!-- Header: icon + site + username + actions -->
    <div class="flex items-start justify-between mb-8">
      <div class="flex items-center gap-4">
        <!-- Favicon / icon -->
        <div class="relative flex-shrink-0">
          <div class="w-12 h-12 bg-slate-50 border border-slate-100 rounded-2xl flex items-center justify-center overflow-hidden group-hover:bg-orange-50 transition-colors">
            <img v-if="faviconUrl" :src="faviconUrl" @error="faviconUrl = null" alt="" class="w-6 h-6 object-contain" />
            <span v-else class="text-sm font-black text-slate-400 uppercase tracking-widest">{{ credential.site.charAt(0) }}</span>
          </div>
        </div>

        <div>
          <h3 class="font-bold tracking-tight text-slate-900 group-hover:text-brand-orange transition-colors">{{ credential.site }}</h3>
          <p class="text-slate-400 text-xs font-bold uppercase tracking-widest">{{ credential.username }}</p>
        </div>
      </div>

      <!-- Edit / Delete actions -->
      <div class="flex gap-1 opacity-10 md:opacity-0 group-hover:opacity-100 transition-opacity duration-200">
        <button @click="$emit('edit', credential)"
          class="p-2 hover:bg-orange-50 rounded-xl text-slate-400 hover:text-brand-orange transition-colors" title="Edit">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4.5 w-4.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
          </svg>
        </button>
        <button @click="$emit('delete', credential)"
          class="p-2 hover:bg-rose-50 rounded-xl text-slate-400 hover:text-rose-500 transition-colors" title="Delete">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4.5 w-4.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Password row -->
    <div class="flex items-center justify-between bg-slate-50 border border-slate-100 rounded-2xl p-3 px-4 group-hover:bg-white group-hover:border-orange-100 transition-all">
      <input :type="visible ? 'text' : 'password'" :value="credential.password" readonly
        class="bg-transparent border-none text-sm font-bold font-mono tracking-wider w-full focus:ring-0 text-slate-900 focus:outline-none p-0 selection:bg-orange-100 selection:text-brand-orange" />
      <div class="flex items-center gap-1 flex-shrink-0 ml-4">
        <button @click="visible = !visible" class="p-1 px-2 hover:bg-white rounded-lg text-slate-400 hover:text-slate-900 shadow-sm border border-transparent hover:border-slate-100 transition-all" :title="visible ? 'Hide Password' : 'Show Password'">
          <svg v-if="!visible" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
          </svg>
        </button>
        <button @click="copyPassword" class="p-1 px-2 rounded-lg transition-all flex items-center justify-center border border-transparent" :class="copied ? 'bg-emerald-50 text-emerald-600 border-emerald-100' : 'hover:bg-white hover:border-slate-100 text-slate-400 hover:text-slate-900'" :title="copied ? 'Copied' : 'Copy Password'">
          <svg v-if="copied" xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({ credential: { type: Object, required: true } })
defineEmits(['edit', 'delete'])

const visible = ref(false)
const copied = ref(false)
const faviconUrl = ref(
  props.credential.site
    ? `https://www.google.com/s2/favicons?sz=64&domain=${encodeURIComponent(props.credential.site)}`
    : null
)

async function copyPassword() {
  try {
    await navigator.clipboard.writeText(props.credential.password)
    copied.value = true
    setTimeout(() => copied.value = false, 2000)
  } catch {}
}
</script>

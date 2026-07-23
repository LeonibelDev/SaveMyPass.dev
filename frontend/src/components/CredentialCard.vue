<template>
  <div class="bg-white border border-slate-200/50 rounded-3xl p-6 group hover:border-brand-orange hover:shadow-xl hover:shadow-orange-100/30 transition-all duration-300 flex flex-col justify-between">

    <!-- Header: icon + site + username + actions -->
    <div class="flex items-start justify-between mb-8">
      <div class="flex items-center gap-4">
        <!-- Favicon / icon -->
        <div class="relative flex-shrink-0">
          <div class="w-12 h-12 bg-white border border-slate-200/60 rounded-2xl flex items-center justify-center overflow-hidden group-hover:scale-105 transition-all shadow-sm">
            <img v-if="faviconUrl" :src="faviconUrl" @error="faviconUrl = null" alt="" class="w-6 h-6 object-contain" />
            <span v-else class="text-sm font-black text-slate-400 uppercase tracking-widest font-outfit">{{ credential.site.charAt(0) }}</span>
          </div>
        </div>

        <div>
          <h3 class="font-bold tracking-tight text-slate-900 group-hover:text-brand-orange transition-colors font-outfit">{{ credential.site }}</h3>
          <p class="text-slate-400 text-xs font-semibold uppercase tracking-wider mt-0.5 font-outfit">{{ credential.username }}</p>
        </div>
      </div>

      <!-- Edit / Delete actions -->
      <div class="flex gap-1.5 opacity-10 md:opacity-0 group-hover:opacity-100 transition-opacity duration-200">
        <button @click="$emit('edit', credential)"
          class="w-8 h-8 rounded-lg hover:bg-orange-50 border border-transparent hover:border-orange-100/50 text-slate-400 hover:text-brand-orange flex items-center justify-center transition-colors" title="Edit">
          <i class="fa-solid fa-pen-to-square text-[13px]"></i>
        </button>
        <button @click="$emit('delete', credential)"
          class="w-8 h-8 rounded-lg hover:bg-rose-50 border border-transparent hover:border-rose-100/50 text-slate-400 hover:text-rose-550 flex items-center justify-center transition-colors" title="Delete">
          <i class="fa-solid fa-trash-can text-[13px]"></i>
        </button>
      </div>
    </div>

    <!-- Password row -->
    <div class="flex items-center justify-between bg-slate-50 border border-slate-150 rounded-2xl p-3 px-4 group-hover:bg-white group-hover:border-orange-100 transition-all">
      <input :type="visible ? 'text' : 'password'" :value="credential.password" readonly
        class="bg-transparent border-none text-sm font-bold font-mono tracking-wider w-full focus:ring-0 text-slate-900 focus:outline-none p-0 selection:bg-orange-100 selection:text-brand-orange" />
      <div class="flex items-center gap-1.5 flex-shrink-0 ml-4">
        <button @click="visible = !visible" class="w-8 h-8 flex items-center justify-center hover:bg-white rounded-lg text-slate-400 hover:text-slate-900 shadow-sm border border-transparent hover:border-slate-150 transition-all" :title="visible ? 'Hide Password' : 'Show Password'">
          <i :class="['fa-solid', visible ? 'fa-eye-slash' : 'fa-eye']" class="text-[13px]"></i>
        </button>
        <button @click="copyPassword" class="w-8 h-8 rounded-lg transition-all flex items-center justify-center border border-transparent" :class="copied ? 'bg-emerald-50 text-emerald-600 border-emerald-100' : 'hover:bg-white hover:border-slate-150 text-slate-400 hover:text-slate-900'" :title="copied ? 'Copied' : 'Copy Password'">
          <i v-if="copied" class="fa-solid fa-circle-check text-[13px]"></i>
          <i v-else class="fa-solid fa-copy text-[13px]"></i>
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

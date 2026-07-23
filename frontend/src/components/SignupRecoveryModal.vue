<template>
  <div class="fixed inset-0 z-[100] flex items-center justify-center p-4 sm:p-6">
    <!-- Backdrop -->
    <Transition name="fade">
      <div class="absolute inset-0 bg-black/20 backdrop-blur-sm"></div>
    </Transition>

    <!-- Modal Content -->
    <div
      class="relative w-full max-w-lg bg-white/95 backdrop-blur-xl border border-slate-150 rounded-[28px] shadow-2xl overflow-hidden animate-in fade-in zoom-in duration-300">
      <div class="p-8 sm:p-10 text-center">

        <div
          class="w-20 h-20 bg-orange-500/10 text-orange-600 flex items-center justify-center rounded-[24px] mx-auto mb-8">
          <i class="fa-solid fa-shield-keyhole text-4xl"></i>
        </div>

        <h2 class="text-2xl font-bold text-slate-900 mb-4 tracking-tight">Your Recovery Key</h2>
        <p class="text-[15px] text-slate-500 font-medium leading-relaxed mb-6">
          This is the only way to recover your account if you forget your master password.
          <strong>We cannot reset it for you.</strong>
        </p>

        <div class="bg-slate-50 border-2 border-dashed border-slate-200 rounded-2xl p-6 mb-6 group relative">
          <span class="text-xl font-mono font-bold text-slate-800 tracking-wider break-all">
            {{ recoveryKey }}
          </span>
          <button @click="copyKey"
            class="absolute top-2 right-2 p-2 text-slate-400 hover:text-orange-500 transition-colors">
            <i class="fa-solid" :class="copied ? 'fa-check text-emerald-500' : 'fa-copy'"></i>
          </button>
        </div>

        <div class="flex items-start gap-3 text-left p-4 bg-rose-50 rounded-2xl border border-rose-100 mb-8">
          <i class="fa-solid fa-triangle-exclamation text-rose-500 mt-1"></i>
          <p class="text-[13px] text-rose-600 font-semibold leading-relaxed">
            Write this down or save it safely. If you lose this key and your password, your zero-knowledge data is lost
            forever.
          </p>
        </div>

        <!-- Botón de acción: Emite confirmación al padre -->
        <button @click="$emit('confirm')" :disabled="!copied"
          class="w-full bg-gradient-to-r from-orange-500 to-orange-600 hover:shadow-[0_8px_20px_rgba(249,115,22,0.25)] text-white font-bold py-3.5 rounded-xl transition-all shadow-[0_4px_10px_rgba(249,115,22,0.15)] active:scale-[0.98] disabled:opacity-60 disabled:cursor-not-allowed">
          {{ copied ? "I've saved my recovery key" : "Copy key to continue" }}
        </button>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  recoveryKey: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['confirm'])
const copied = ref(false)

async function copyKey() {
  try {
    await navigator.clipboard.writeText(props.recoveryKey)
    copied.value = true
    // Opcional: resetear el icono de check después de 3 segundos
    setTimeout(() => { copied.value = false }, 3000)
  } catch (err) {
    console.error('Failed to copy: ', err)
  }
}
</script>
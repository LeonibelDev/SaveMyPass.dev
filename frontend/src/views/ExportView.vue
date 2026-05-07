<template>
  <div class="flex-1 flex flex-col bg-[#F2F2F7] overflow-hidden selection:bg-brand-orange/20">

    <!-- Header -->
    <header
      class="h-16 border-b border-slate-200/60 bg-white/80 backdrop-blur-xl flex items-center px-6 z-20 flex-shrink-0">
      <div class="max-w-4xl mx-auto w-full">
        <h1 class="text-[17px] font-bold text-slate-900">Export Data</h1>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto py-8 px-4 sm:px-6 no-scrollbar">
      <div class="max-w-2xl mx-auto animate-fade-in">

        <!-- Intro -->
        <div class="mb-8 px-2">
          <h2 class="text-[24px] font-bold text-slate-900 tracking-tight mb-2">Export your vault</h2>
          <p class="text-[15px] text-slate-500 font-medium leading-relaxed">
            Download your encrypted or decrypted data for backup purposes. Your data belongs to you.
          </p>
        </div>

        <!-- Warning -->
        <div class="mb-8 p-4 bg-orange-50 border border-orange-100 rounded-[20px] flex items-start gap-4">
          <div class="w-10 h-10 rounded-full bg-orange-100 flex items-center justify-center flex-shrink-0">
            <i class="fa-solid fa-triangle-exclamation text-brand-orange text-[16px]"></i>
          </div>
          <div>
            <p class="text-[14px] text-brand-orange font-bold uppercase tracking-wider mb-1">Security Warning</p>
            <p class="text-[13px] text-brand-orange/80 font-medium leading-relaxed">
              Exported files are not protected by zero-knowledge encryption. Store them in a secure, encrypted location.
            </p>
          </div>
        </div>

        <!-- Export List -->
        <!-- Export List -->
        <div
          class="bg-white rounded-[24px] border border-slate-200/60 overflow-hidden shadow-sm divide-y divide-slate-100">

          <!-- Fila de Exportación (Repetir para PDF, JSON, CSV) -->
          <div @click="handleExport"
            class="relative p-5 sm:p-6 flex flex-row items-center justify-between gap-4 hover:bg-slate-50/80 transition-all cursor-pointer group">
            <!-- Contenido Izquierdo: Icono + Texto -->
            <div class="flex items-center gap-4 min-w-0">
              <!-- Icono con contenedor responsivo -->
              <div
                class="w-10 h-10 sm:w-12 sm:h-12 rounded-2xl bg-rose-50 text-rose-500 flex items-center justify-center shadow-sm flex-shrink-0 group-hover:scale-105 transition-transform duration-300">
                <i class="fa-solid fa-file-pdf text-lg sm:text-xl"></i>
              </div>

              <div class="flex-1 min-w-0">
                <h3 class="text-[15px] sm:text-[16px] font-bold text-slate-900 mb-0.5 flex items-center gap-2">
                  PDF Backup
                  <!-- Tag sutil solo visible en desktop/tablet si quieres destacar algo -->
                  <span
                    class="hidden md:inline-block text-[10px] px-2 py-0.5 bg-slate-100 text-slate-500 rounded-full font-semibold uppercase tracking-wider">Offline</span>
                </h3>
                <p
                  class="text-[12px] sm:text-[13px] text-slate-400 font-medium leading-tight line-clamp-1 sm:line-clamp-none">
                  Formatted document for offline use
                </p>
              </div>
            </div>

            <!-- Botón Derecho: Se adapta según dispositivo -->
            <div class="flex items-center gap-3">
              <!-- Texto de estado (opcional para tablet/PC) -->
              <span v-if="exporting" class="hidden sm:inline text-[13px] font-medium text-slate-400 animate-pulse">
                Processing...
              </span>

              <button :disabled="exporting"
                class="h-9 sm:h-10 w-9 sm:w-auto sm:px-5 flex items-center justify-center bg-slate-900 text-white font-bold rounded-xl text-[13px] sm:text-[14px] hover:bg-brand-orange active:scale-95 transition-all shadow-sm disabled:opacity-50">
                <span class="hidden sm:inline">{{ exporting ? 'Exporting...' : 'Export' }}</span>
                <i class="fa-solid fa-download sm:hidden text-[14px]"></i> <!-- Solo icono en móvil -->
                <i
                  class="fa-solid fa-chevron-right hidden sm:inline ml-2 text-[10px] opacity-50 group-hover:translate-x-1 transition-transform"></i>
              </button>
            </div>

            <!-- Overlay invisible para capturar el click en toda la fila -->
            <!-- Esto permite que el cursor sea 'pointer' en toda el área -->
            <div class="absolute inset-0 z-0"></div>
          </div>

          <!-- JSON Snapshot -->
          <div @click="handleExportJSON"
            class="relative p-5 sm:p-6 flex flex-row items-center justify-between gap-4 hover:bg-slate-50/80 transition-all cursor-pointer group">
            <div class="flex items-center gap-4 min-w-0">
              <div
                class="w-10 h-10 sm:w-12 sm:h-12 rounded-2xl bg-blue-50 text-blue-500 flex items-center justify-center shadow-sm flex-shrink-0 group-hover:scale-105 transition-transform duration-300">
                <i class="fa-solid fa-code text-lg sm:text-xl"></i>
              </div>
              <div class="flex-1 min-w-0">
                <h3 class="text-[15px] sm:text-[16px] font-bold text-slate-900 mb-0.5">JSON Snapshot</h3>
                <p
                  class="text-[12px] sm:text-[13px] text-slate-400 font-medium leading-tight line-clamp-1 sm:line-clamp-none">
                  Raw data for migration & auditing
                </p>
              </div>
            </div>
            <div class="flex items-center gap-3">
              <button
                class="h-9 sm:h-10 w-9 sm:w-auto sm:px-5 flex items-center justify-center bg-slate-900 text-white font-bold rounded-xl text-[13px] sm:text-[14px] hover:bg-brand-orange active:scale-95 transition-all shadow-sm">
                <span class="hidden sm:inline">Download</span>
                <i class="fa-solid fa-download sm:hidden text-[14px]"></i>
                <i
                  class="fa-solid fa-chevron-right hidden sm:inline ml-2 text-[10px] opacity-50 group-hover:translate-x-1 transition-transform"></i>
              </button>
            </div>
          </div>

        </div>

        <div class="mt-8 text-center px-6">
          <p class="text-[12px] text-slate-400 font-medium leading-relaxed italic">
            "Your data is your property. We provide the tools to ensure you never lose access."
          </p>
        </div>

      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useVaultStore } from '@/stores/vault'

const vault = useVaultStore()
const exporting = ref(false)

async function handleExport() {
  exporting.value = true
  try {
    await vault.exportVault()
  } catch (err) {
    console.error('Export error:', err)
  } finally {
    exporting.value = false
  }
}

async function handleExportCSV() {
  exporting.value = true
  try {
    await vault.exportVaultCSV()
  } catch (err) {
    console.error('Export error:', err)
  } finally {
    exporting.value = false
  }
}

async function handleExportJSON() {
  const data = JSON.stringify(vault.credentials, null, 2)
  const blob = new Blob([data], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `savemypass-vault-${new Date().toISOString().split('T')[0]}.json`
  a.click()
  URL.revokeObjectURL(url)
}
</script>
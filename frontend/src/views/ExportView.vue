<template>
  <div class="flex-1 flex flex-col bg-[#F2F2F7] overflow-hidden selection:bg-brand-orange/20">

    <!-- Header -->
    <header class="h-16 border-b border-slate-200/60 bg-white/80 backdrop-blur-xl flex items-center px-6 z-20 flex-shrink-0">
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
        <div class="bg-white rounded-[24px] border border-slate-200/60 overflow-hidden shadow-sm divide-y divide-slate-100">
          
          <!-- PDF -->
          <div class="p-6 flex items-center justify-between gap-6 hover:bg-slate-50 transition-colors group">
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-2xl bg-rose-50 text-rose-500 flex items-center justify-center shadow-sm">
                <i class="fa-solid fa-file-pdf text-xl"></i>
              </div>
              <div class="flex-1 min-w-0">
                <h3 class="text-[16px] font-bold text-slate-900 mb-0.5">PDF Backup</h3>
                <p class="text-[13px] text-slate-400 font-medium truncate">Formatted document for offline use</p>
              </div>
            </div>
            <button @click="handleExport" :disabled="exporting"
              class="h-10 px-5 bg-slate-900 text-white font-bold rounded-xl text-[14px] active:scale-95 transition-all disabled:opacity-40">
              {{ exporting ? '...' : 'Export' }}
            </button>
          </div>

          <!-- JSON -->
          <div class="p-6 flex items-center justify-between gap-6 hover:bg-slate-50 transition-colors group">
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-2xl bg-blue-50 text-blue-500 flex items-center justify-center shadow-sm">
                <i class="fa-solid fa-code text-xl"></i>
              </div>
              <div class="flex-1 min-w-0">
                <h3 class="text-[16px] font-bold text-slate-900 mb-0.5">JSON Snapshot</h3>
                <p class="text-[13px] text-slate-400 font-medium truncate">Raw data for migration & auditing</p>
              </div>
            </div>
            <button @click="handleExportJSON"
              class="h-10 px-5 bg-slate-900 text-white font-bold rounded-xl text-[14px] active:scale-95 transition-all">
              Download
            </button>
          </div>

          <!-- CSV -->
          <div class="p-6 flex items-center justify-between gap-6 hover:bg-slate-50 transition-colors group">
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-2xl bg-emerald-50 text-emerald-600 flex items-center justify-center shadow-sm">
                <i class="fa-solid fa-table text-xl"></i>
              </div>
              <div class="flex-1 min-w-0">
                <h3 class="text-[16px] font-bold text-slate-900 mb-0.5">CSV Snapshot</h3>
                <p class="text-[13px] text-slate-400 font-medium truncate">Spreadsheet-friendly format</p>
              </div>
            </div>
            <button @click="handleExportCSV" :disabled="exporting"
              class="h-10 px-5 bg-slate-900 text-white font-bold rounded-xl text-[14px] active:scale-95 transition-all disabled:opacity-40">
              {{ exporting ? '...' : 'Export' }}
            </button>
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
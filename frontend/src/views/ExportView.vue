<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F7F8FA]">

    <!-- Header -->
    <header class="h-16 border-b border-slate-100 flex items-center px-6 justify-between flex-shrink-0 bg-white z-10">
      <div class="flex items-center flex-1 max-w-xl">
        <h1 class="text-sm font-black text-slate-800 tracking-tight uppercase">
          Data Sovereignty
        </h1>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto">
      <div class="max-w-4xl mx-auto py-12 px-6">

        <!-- Intro -->
        <div class="mb-10">
          <h2 class="text-xl font-black text-slate-800 tracking-tight mb-2">
            Export your vault
          </h2>
          <p class="text-sm text-slate-400 font-medium max-w-xl leading-relaxed">
            Download your encrypted or decrypted data for backup purposes. Your data belongs to you, and you should always have a local copy.
          </p>
        </div>

        <!-- Cards -->
        <div class="grid sm:grid-cols-2 md:grid-cols-3 gap-4">

          <!-- PDF -->
          <div class="p-5 rounded-2xl border border-slate-100 bg-white flex flex-col">
            <div class="w-10 h-10 bg-rose-50 text-rose-500 rounded-xl flex items-center justify-center mb-4">
              <i class="fa-solid fa-file-pdf text-sm"></i>
            </div>

            <h3 class="text-sm font-bold text-slate-800 mb-1">
              PDF Backup
            </h3>

            <p class="text-[11px] text-slate-400 font-medium leading-relaxed mb-6">
              Generate a formatted PDF document containing your credentials for offline storage.
            </p>

            <button
              @click="handleExport"
              :disabled="exporting"
              class="mt-auto h-9 bg-slate-900 text-white font-bold rounded-xl text-[10px] uppercase tracking-widest active:scale-95 transition-all disabled:opacity-50"
            >
              {{ exporting ? 'Generating...' : 'Generate' }}
            </button>
          </div>

          <!-- JSON -->
          <div class="p-5 rounded-2xl border border-slate-100 bg-white flex flex-col">
            <div class="w-10 h-10 bg-blue-50 text-blue-500 rounded-xl flex items-center justify-center mb-4">
              <i class="fa-solid fa-code text-sm"></i>
            </div>

            <h3 class="text-sm font-bold text-slate-800 mb-1">
              JSON Snapshot
            </h3>

            <p class="text-[11px] text-slate-400 font-medium leading-relaxed mb-6">
              Export your raw vault data for migration or advanced auditing.
            </p>

            <button
              @click="handleExportJSON"
              :disabled="exporting"
              class="mt-auto h-9 bg-slate-900 text-white font-bold rounded-xl text-[10px] uppercase tracking-widest active:scale-95 transition-all disabled:opacity-50"
            >
              Download
            </button>
          </div>

          <!-- CSV -->
          <div class="p-5 rounded-2xl border border-slate-100 bg-white flex flex-col">
            <div class="w-10 h-10 bg-green-50 text-green-500 rounded-xl flex items-center justify-center mb-4">
              <i class="fa-solid fa-table text-sm"></i>
            </div>

            <h3 class="text-sm font-bold text-slate-800 mb-1">
              CSV Snapshot
            </h3>

            <p class="text-[11px] text-slate-400 font-medium leading-relaxed mb-6">
              Export your vault data in a spreadsheet-friendly format.
            </p>

            <button
              @click="handleExportCSV"
              :disabled="exporting"
              class="mt-auto h-9 bg-slate-900 text-white font-bold rounded-xl text-[10px] uppercase tracking-widest active:scale-95 transition-all disabled:opacity-50"
            >
              Download
            </button>
          </div>

        </div>

        <!-- Warning -->
        <div class="mt-10 p-4 rounded-xl bg-orange-50 border border-orange-100 flex items-start gap-3">
          <i class="fa-solid fa-triangle-exclamation text-brand-orange mt-0.5 text-sm"></i>
          <p class="text-[10px] text-brand-orange/80 font-bold uppercase tracking-widest leading-relaxed">
            Exported files are not protected by zero-knowledge encryption. Store them securely.
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
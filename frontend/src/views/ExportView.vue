<template>
  <div class="view-root">

    <!-- Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar island-toolbar--titled">
        <h1 class="toolbar-title">Export Data</h1>
      </header>
    </div>

    <!-- Main -->
    <main class="view-main">
      <div class="content-container" style="max-width: 680px;">

        <!-- Intro -->
        <div style="padding: 12px 4px 16px;">
          <h2 style="font-family:'Outfit',sans-serif; font-size:22px; font-weight:800; color:#f1f5f9; letter-spacing:-0.03em; margin-bottom:6px;">
            Export your vault
          </h2>
          <p style="font-size:13px; color:#64748b; font-weight:500; line-height:1.6;">
            Download your encrypted or decrypted data for backup. Your data belongs to you.
          </p>
        </div>

        <!-- Warning island -->
        <div class="island-card island-card--padded" style="margin-bottom:14px; background: rgba(249,115,22,0.07); border-color: rgba(249,115,22,0.18);">
          <div style="display:flex; align-items:flex-start; gap:12px;">
            <div style="width:36px; height:36px; border-radius:10px; background:rgba(249,115,22,0.12); border:1px solid rgba(249,115,22,0.2); display:flex; align-items:center; justify-content:center; color:#f97316; flex-shrink:0;">
              <i class="fa-solid fa-lightbulb"></i>
            </div>
            <div>
              <p style="font-size:11px; font-weight:700; letter-spacing:0.08em; text-transform:uppercase; color:#f97316; font-family:'Outfit',sans-serif; margin-bottom:4px;">Security Warning</p>
              <p style="font-size:12px; color:rgba(249,115,22,0.8); font-weight:500; line-height:1.5;">
                Exported files are not protected by zero-knowledge encryption. Store them in a secure, encrypted location.
              </p>
            </div>
          </div>
        </div>

        <!-- Export options island -->
        <div class="island-card" style="margin-bottom:14px;">

          <!-- PDF -->
          <div class="export-row">
            <div class="export-icon" style="background:rgba(239,68,68,0.1); border-color:rgba(239,68,68,0.15); color:#f87171;">
              <i class="fa-solid fa-file-pdf text-lg"></i>
            </div>
            <div class="export-info">
              <h3 class="export-title">PDF Backup</h3>
              <p class="export-desc">Formatted document for offline use</p>
            </div>
            <button @click="handleExport" :disabled="exporting" class="btn-primary">
              {{ exporting ? '…' : 'Export' }}
            </button>
          </div>

          <!-- JSON -->
          <div class="export-row">
            <div class="export-icon" style="background:rgba(59,130,246,0.1); border-color:rgba(59,130,246,0.15); color:#60a5fa;">
              <i class="fa-solid fa-file-code text-lg"></i>
            </div>
            <div class="export-info">
              <h3 class="export-title">JSON Snapshot</h3>
              <p class="export-desc">Raw data for migration &amp; auditing</p>
            </div>
            <button @click="handleExportJSON" class="btn-primary">Download</button>
          </div>

          <!-- CSV -->
          <div class="export-row" style="border-bottom:none;">
            <div class="export-icon" style="background:rgba(34,197,94,0.1); border-color:rgba(34,197,94,0.15); color:#4ade80;">
              <i class="fa-solid fa-file-excel text-lg"></i>
            </div>
            <div class="export-info">
              <h3 class="export-title">CSV Snapshot</h3>
              <p class="export-desc">Spreadsheet-friendly format</p>
            </div>
            <button @click="handleExportCSV" :disabled="exporting" class="btn-primary">
              {{ exporting ? '…' : 'Export' }}
            </button>
          </div>

        </div>

        <div style="text-align:center; padding:8px 0 24px;">
          <p style="font-size:11px; color:#334155; font-weight:500; font-style:italic; line-height:1.6; max-width:340px; margin:0 auto;">
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
  try { await vault.exportVault() }
  catch (err) { console.error('Export error:', err) }
  finally { exporting.value = false }
}

async function handleExportCSV() {
  exporting.value = true
  try { await vault.exportVaultCSV() }
  catch (err) { console.error('Export error:', err) }
  finally { exporting.value = false }
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

<style scoped>
@import '@/assets/island-theme.css';

.export-row {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
  transition: background 0.12s;
}

.export-row:hover { background: rgba(255, 255, 255, 0.03); }

.export-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  border: 1px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.export-info { flex: 1; min-width: 0; }

.export-title {
  font-family: 'Outfit', sans-serif;
  font-size: 15px;
  font-weight: 700;
  color: #e2e8f0;
  letter-spacing: -0.01em;
  margin-bottom: 2px;
}

.export-desc {
  font-size: 12px;
  color: #475569;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
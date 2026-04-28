<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F4F5F7]">

    <!-- Header / Toolbar -->
    <header
      class="h-[68px] border-b border-slate-100 flex items-center px-6 justify-between flex-shrink-0 bg-white z-10">
      <!-- Search -->
      <div class="relative w-full max-w-sm group">
        <span
          class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 group-focus-within:text-brand-orange transition-colors pointer-events-none">
          <i class="fa-solid fa-magnifying-glass text-[12px]"></i>
        </span>
        <input ref="searchInput" v-model="search" type="text" placeholder="Search vault…"
          class="w-full bg-[#F4F5F7] border-[1.5px] border-transparent rounded-[10px] pl-9 pr-16 py-[9px] text-[13px] font-medium text-slate-700 focus:bg-white focus:border-brand-orange focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-400" />
        <div class="absolute inset-y-0 right-0 pr-2.5 flex items-center gap-1 pointer-events-none">
          <kbd class="hidden sm:flex items-center gap-0.5 group-focus-within:opacity-0 transition-opacity">
            <span
              class="inline-flex items-center justify-center h-5 min-w-[20px] px-1 bg-white border border-slate-200 rounded-[5px] text-[10px] font-bold text-slate-400 shadow-[0_1px_0_#D1D5DB]">⌘</span>
            <span
              class="inline-flex items-center justify-center h-5 min-w-[20px] px-1 bg-white border border-slate-200 rounded-[5px] text-[10px] font-bold text-slate-400 shadow-[0_1px_0_#D1D5DB]">K</span>
          </kbd>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex items-center gap-2 ml-4">
        <button @click="triggerExport" :disabled="exporting"
          class="h-9 px-4 flex items-center gap-1.5 text-slate-500 hover:text-brand-orange hover:bg-orange-50 rounded-[10px] transition-all text-[11px] font-bold uppercase tracking-[0.07em] border-[1.5px] border-slate-200 hover:border-brand-orange/30">
          <i class="fa-solid fa-arrow-down-to-bracket text-[11px]"></i>
          Export
        </button>
        <button @click="showAddModal = true"
          class="h-9 px-[18px] bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-[10px] transition-all shadow-[0_4px_14px_rgba(249,115,22,0.35)] hover:shadow-[0_6px_18px_rgba(249,115,22,0.45)] hover:-translate-y-px flex items-center gap-2 active:scale-[0.97] text-[11px] uppercase tracking-[0.07em]">
          <i class="fa-solid fa-plus text-[11px]"></i>
          New entry
        </button>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto scrollbar-thin scrollbar-thumb-slate-200 scrollbar-track-transparent">
      <!-- Loading -->
      <div v-if="vault.loading" class="flex flex-col items-center justify-center py-40">
        <i class="fa-solid fa-circle-notch fa-spin text-slate-300 text-2xl mb-4"></i>
        <p class="text-xs font-bold text-slate-300 uppercase tracking-widest">Loading vault…</p>
      </div>

      <div v-else class="max-w-4xl mx-auto py-10 px-6">

        <!-- Export message -->
        <div v-if="exportMsg" class="mb-6 animate-fade-in">
          <div
            class="p-3.5 bg-emerald-50 border border-emerald-100 rounded-xl text-emerald-600 text-xs font-bold uppercase tracking-widest text-center">
            <i class="fa-solid fa-circle-check mr-2"></i>{{ exportMsg }}
          </div>
        </div>

        <!-- Section header -->
        <div class="flex items-center justify-between mb-3 px-0.5">
          <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-[0.2em] flex items-center gap-2">
            All items
            <span
              class="inline-flex items-center px-2 py-0.5 bg-white border border-slate-200 rounded-full text-[10px] font-bold text-slate-400">
              {{ filtered.length }}
            </span>
          </h2>
          <div class="flex items-center gap-1.5 text-[10px] font-bold text-slate-400 uppercase tracking-[0.1em]">
            <i class="fa-solid fa-arrow-down-wide-short text-[10px]"></i>
            Last updated
          </div>
        </div>

        <!-- Credentials list -->
        <div
          class="bg-white rounded-2xl border border-slate-100 overflow-hidden divide-y divide-slate-50 shadow-[0_1px_3px_rgba(0,0,0,0.04),0_4px_12px_rgba(0,0,0,0.03)]">
          <div v-for="cred in filtered" :key="cred.id" @click="goToDetail(cred.id)"
            class="group flex items-center gap-4 px-5 py-3.5 hover:bg-slate-50/80 transition-colors cursor-pointer">

            <!-- Favicon -->
            <div
              class="w-10 h-10 rounded-[12px] bg-[#F4F5F7] border-[1.5px] border-slate-100 flex items-center justify-center flex-shrink-0 overflow-hidden transition-all group-hover:border-brand-orange/20 group-hover:shadow-[0_0_0_3px_rgba(249,115,22,0.08)]">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${cred.site}&size=64`"
                :alt="cred.site" class="w-5 h-5 object-contain" @error="$event.target.style.display = 'none'" />
              <i v-if="!cred.site" class="fa-solid fa-globe text-slate-300 text-sm"></i>
            </div>

            <!-- Info -->
            <div class="flex-1 min-w-0">
              <p
                class="font-bold text-[14px] text-slate-800 tracking-tight truncate group-hover:text-brand-orange transition-colors leading-snug">
                {{ cred.site }}
              </p>
              <p class="text-[12px] text-slate-400 font-medium truncate mt-0.5">
                {{ cred.username }}
              </p>
            </div>

            <!-- Date + arrow -->
            <div class="flex items-center gap-3 flex-shrink-0">
              <span
                class="text-[11px] font-semibold text-slate-300 tracking-[0.04em] group-hover:text-slate-400 transition-colors hidden sm:block">
                {{ new Date(cred.updatedAt).toLocaleDateString(undefined, {
                  month: 'short', day: 'numeric', year: 'numeric'
                }) }}
              </span>
              <i
                class="fa-solid fa-chevron-right text-[10px] text-slate-200 group-hover:text-brand-orange transition-colors"></i>
            </div>
          </div>
        </div>

        <!-- Empty state -->
        <div v-if="filtered.length === 0" class="flex flex-col items-center justify-center py-32 text-center">
          <div
            class="w-[72px] h-[72px] bg-white rounded-[22px] border-[1.5px] border-slate-100 flex items-center justify-center mb-5 shadow-[0_2px_8px_rgba(0,0,0,0.05)]">
            <i class="fa-solid fa-vault text-[28px] text-slate-200"></i>
          </div>
          <h3 class="font-black text-slate-800 text-[19px] tracking-tight mb-2">Vault is empty</h3>
          <p class="text-[13px] text-slate-400 font-medium max-w-[240px] leading-relaxed mb-6">
            Your secure enclave is ready. Add your first credential to get started.
          </p>
          <button @click="showAddModal = true"
            class="h-9 px-5 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-[10px] transition-all shadow-[0_4px_14px_rgba(249,115,22,0.35)] flex items-center gap-2 text-[11px] uppercase tracking-[0.07em]">
            <i class="fa-solid fa-plus text-[11px]"></i>
            New entry
          </button>
        </div>

      </div>
    </main>

    <!-- Modals -->
    <AddCredentialModal v-if="showAddModal" @close="handleAdded" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useVaultStore } from '@/stores/vault'
import AddCredentialModal from '@/components/AddCredentialModal.vue'

const router = useRouter()
const vault = useVaultStore()

const search = ref('')
const showAddModal = ref(false)
const exporting = ref(false)
const exportMsg = ref(null)

const searchInput = ref(null)

const filtered = computed(() => {
  if (!search.value.trim()) return vault.credentials
  const q = search.value.toLowerCase()
  return vault.credentials.filter(c =>
    c.site.toLowerCase().includes(q) || c.username.toLowerCase().includes(q)
  )
})

function goToDetail(id) {
  router.push(`/credentials/${id}`)
}

async function triggerExport() {
  exporting.value = true
  try {
    await vault.exportVault()
    exportMsg.value = 'PDF Downloaded Successfully!'
    setTimeout(() => exportMsg.value = null, 4000)
  } catch (err) {
    console.error('Export error:', err)
    exportMsg.value = 'Export failed.'
  } finally {
    exporting.value = false
  }
}

function handleAdded() {
  showAddModal.value = false
  vault.fetchCredentials()
}


function handleGlobalKey(e) {
  if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
    e.preventDefault()
    searchInput.value?.focus()
  }
}

onMounted(() => {
  vault.fetchCredentials()
  window.addEventListener('keydown', handleGlobalKey)
})
</script>
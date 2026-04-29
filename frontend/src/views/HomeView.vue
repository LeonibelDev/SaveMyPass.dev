<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F2F2F7]">

    <!-- Header / Toolbar -->
    <header
      class="h-16 border-b border-slate-200/60 flex items-center px-4 sm:px-6 justify-between flex-shrink-0 bg-white/80 backdrop-blur-xl z-20">
      
      <!-- Search -->
      <div class="relative flex-1 max-w-sm group">
        <span
          class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 group-focus-within:text-brand-orange transition-colors pointer-events-none">
          <i class="fa-solid fa-magnifying-glass text-[12px]"></i>
        </span>
        <input ref="searchInput" v-model="search" type="text" placeholder="Search vault"
          class="w-full bg-[#E3E3E8] border-none rounded-xl pl-9 pr-4 sm:pr-16 py-2 text-[15px] font-medium text-slate-700 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all placeholder:text-slate-500" />
        <div class="absolute inset-y-0 right-0 pr-2.5 flex items-center gap-1 pointer-events-none">
          <kbd class="hidden sm:flex items-center gap-0.5 group-focus-within:opacity-0 transition-opacity">
            <span
              class="inline-flex items-center justify-center h-5 min-w-[20px] px-1 bg-white border border-slate-200 rounded-[5px] text-[10px] font-bold text-slate-400 shadow-sm">⌘</span>
            <span
              class="inline-flex items-center justify-center h-5 min-w-[20px] px-1 bg-white border border-slate-200 rounded-[5px] text-[10px] font-bold text-slate-400 shadow-sm">K</span>
          </kbd>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex items-center gap-2 ml-3 sm:ml-4">
        <button @click="triggerExport" :disabled="exporting"
          class="h-9 w-9 sm:w-auto sm:px-4 flex items-center justify-center sm:gap-1.5 text-slate-600 hover:text-brand-orange hover:bg-orange-50 rounded-xl transition-all text-[12px] font-semibold border border-slate-200 hover:border-brand-orange/30 active:scale-95">
          <i class="fa-solid fa-arrow-down-to-bracket text-[11px]"></i>
          <span class="hidden sm:inline">Export</span>
        </button>
        <button @click="showAddModal = true"
          class="h-9 w-9 sm:w-auto sm:px-[18px] bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-sm flex items-center justify-center sm:gap-2 active:scale-95 text-[12px]">
          <i class="fa-solid fa-plus text-[11px]"></i>
          <span class="hidden sm:inline">New entry</span>
        </button>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto no-scrollbar">
      <!-- Loading -->
      <div v-if="vault.loading" class="flex flex-col items-center justify-center py-40">
        <div class="w-8 h-8 border-[3px] border-slate-200 border-t-brand-orange rounded-full animate-spin"></div>
      </div>

      <div v-else class="max-w-4xl mx-auto py-6 sm:py-8 px-4 sm:px-6">

        <!-- Export message -->
        <div v-if="exportMsg" class="mb-6 animate-fade-in">
          <div
            class="p-3.5 bg-emerald-50 border border-emerald-100 rounded-2xl text-emerald-600 text-[13px] font-semibold text-center">
            <i class="fa-solid fa-circle-check mr-2"></i>{{ exportMsg }}
          </div>
        </div>

        <!-- Section header -->
        <div class="flex items-center justify-between mb-4 px-1">
          <h2 class="text-[12px] font-bold text-slate-400 uppercase tracking-wider flex items-center gap-2">
            Items
            <span
              class="inline-flex items-center px-2 py-0.5 bg-slate-200/50 rounded-full text-[10px] font-bold text-slate-500">
              {{ filtered.length }}
            </span>
          </h2>
          <div class="flex items-center gap-1.5 text-[12px] font-semibold text-slate-400">
            <i class="fa-solid fa-arrow-down-wide-short text-[11px]"></i>
            <span class="hidden xs:inline">Recently updated</span>
          </div>
        </div>

        <!-- Credentials list -->
        <div
          class="bg-white rounded-[20px] border border-slate-200/60 overflow-hidden divide-y divide-slate-100 shadow-sm">
          <div v-for="cred in filtered" :key="cred.id" @click="goToDetail(cred.id)"
            class="group flex items-center gap-3 sm:gap-4 px-4 sm:px-5 py-4 hover:bg-slate-50 transition-colors cursor-pointer active:bg-slate-100">

            <!-- Favicon -->
            <div
              class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 flex items-center justify-center flex-shrink-0 overflow-hidden group-hover:scale-105 transition-transform">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${cred.site}&size=64`"
                :alt="cred.site" class="w-5 h-5 object-contain" @error="$event.target.style.display = 'none'" />
              <i v-if="!cred.site" class="fa-solid fa-globe text-slate-300 text-sm"></i>
            </div>

            <!-- Info -->
            <div class="flex-1 min-w-0">
              <p
                class="font-semibold text-[15px] text-slate-900 tracking-tight truncate leading-tight">
                {{ cred.site }}
              </p>
              <p class="text-[13px] text-slate-400 font-medium truncate mt-0.5">
                {{ cred.username }}
              </p>
            </div>

            <!-- Date + arrow -->
            <div class="flex items-center gap-3 flex-shrink-0">
              <span
                class="text-[12px] font-medium text-slate-300 hidden sm:block">
                {{ new Date(cred.updatedAt).toLocaleDateString(undefined, {
                  month: 'short', day: 'numeric'
                }) }}
              </span>
              <i
                class="fa-solid fa-chevron-right text-[11px] text-slate-200 group-hover:text-slate-400 transition-colors"></i>
            </div>
          </div>
        </div>

        <!-- Empty state -->
        <div v-if="filtered.length === 0" class="flex flex-col items-center justify-center py-32 text-center px-4">
          <div
            class="w-20 h-20 bg-white rounded-[24px] border border-slate-200/60 flex items-center justify-center mb-6 shadow-sm">
            <i class="fa-solid fa-vault text-[32px] text-slate-200"></i>
          </div>
          <h3 class="font-bold text-slate-900 text-xl tracking-tight mb-2">Vault is empty</h3>
          <p class="text-[15px] text-slate-500 font-medium max-w-[240px] leading-relaxed mb-8">
            Your secure enclave is ready. Add your first credential to get started.
          </p>
          <button @click="showAddModal = true"
            class="h-11 px-8 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-sm active:scale-95 text-[14px]">
            Add Entry
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
    exportMsg.value = 'Vault exported successfully'
    setTimeout(() => exportMsg.value = null, 4000)
  } catch (err) {
    console.error('Export error:', err)
    exportMsg.value = 'Export failed'
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
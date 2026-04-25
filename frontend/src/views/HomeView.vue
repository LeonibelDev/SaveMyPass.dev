<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F7F8FA]">

    <!-- Header / Toolbar -->
    <header class="h-16 border-b border-slate-100 flex items-center px-6 justify-between flex-shrink-0 bg-white z-10">
      <!-- Search -->
      <div class="relative w-full max-w-sm group">
        <span
          class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 group-focus-within:text-brand-orange transition-colors pointer-events-none">
          <i class="fa-solid fa-magnifying-glass text-[13px]"></i>
        </span>
        <input ref="searchInput" v-model="search" type="text" placeholder="Search vault…"
          class="w-full bg-slate-100 border-none rounded-xl pl-9 pr-16 py-2 text-sm font-medium text-slate-700 focus:bg-white focus:ring-2 focus:ring-brand-orange/20 focus:outline-none transition-all placeholder:text-slate-400" />
        <div class="absolute inset-y-0 right-0 pr-2.5 flex items-center gap-1 pointer-events-none">
          <kbd class="hidden sm:flex items-center gap-0.5 group-focus-within:opacity-0 transition-opacity">
            <span
              class="inline-flex items-center justify-center h-5 min-w-[20px] px-1 bg-white border border-slate-200 rounded text-[10px] font-bold text-slate-400 shadow-sm">⌘</span>
            <span
              class="inline-flex items-center justify-center h-5 min-w-[20px] px-1 bg-white border border-slate-200 rounded text-[10px] font-bold text-slate-400 shadow-sm">K</span>
          </kbd>
        </div>
      </div>

      <!-- Actions -->
      <div class="flex items-center gap-2 ml-4">
        <button @click="triggerExport" :disabled="exporting"
          class="h-9 px-4 flex items-center gap-2 text-slate-500 hover:text-brand-orange hover:bg-orange-50 rounded-xl transition-all text-xs font-bold uppercase tracking-widest border border-slate-100">
          <i class="fa-solid fa-arrow-down-to-bracket text-[12px]"></i>
          Export
        </button>
        <button @click="showAddModal = true"
          class="h-9 px-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-lg shadow-orange-100 flex items-center gap-2 active:scale-95 text-xs uppercase tracking-widest">
          <i class="fa-solid fa-plus text-[12px]"></i>
          New entry
        </button>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto">
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
        <div class="flex items-center justify-between mb-4 px-1">
          <h2 class="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em]">
            All items <span class="ml-2 text-slate-300">{{ filtered.length }}</span>
          </h2>
          <div class="flex items-center gap-1 text-[10px] font-black text-slate-300 uppercase tracking-widest">
            <i class="fa-solid fa-arrow-down-wide-short text-[10px]"></i>
            Last updated
          </div>
        </div>

        <!-- Credentials list -->
        <div class="bg-white rounded-2xl border border-slate-100 overflow-hidden divide-y divide-slate-50">
          <div v-for="cred in filtered" :key="cred.id" @click="goToDetail(cred.id)"
            class="group flex items-center gap-4 px-5 py-3.5 hover:bg-slate-50 transition-colors cursor-pointer">

            <!-- Favicon -->
            <div
              class="w-9 h-9 rounded-xl bg-slate-50 border border-slate-100 flex items-center justify-center flex-shrink-0 overflow-hidden">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${cred.site}&size=64`"
                :alt="cred.site" class="w-5 h-5 object-contain" @error="$event.target.style.display = 'none'" />
              <i v-if="!cred.site" class="fa-solid fa-globe text-slate-300 text-sm"></i>
            </div>

            <!-- Info -->
            <div class="flex-1 min-w-0">
              <p
                class="font-bold text-slate-800 text-sm tracking-tight truncate group-hover:text-brand-orange transition-colors">
                {{ cred.site }}
              </p>
              <p class="text-[11px] text-slate-400 font-medium truncate mt-0.5">
                {{ cred.username }}
              </p>
            </div>

            <!-- Date + arrow -->
            <div class="flex items-center gap-3 flex-shrink-0">
              <span
                class="text-[10px] font-bold text-slate-300 uppercase tracking-widest group-hover:text-slate-400 transition-colors hidden sm:block">
                {{ new Date(cred.updatedAt).toLocaleDateString(undefined, {
                  month: 'short', day: 'numeric', year:
                    'numeric'
                }) }}
              </span>
              <i
                class="fa-solid fa-chevron-right text-[11px] text-slate-200 group-hover:text-brand-orange transition-colors"></i>
            </div>
          </div>
        </div>

        <!-- Empty state -->
        <div v-if="filtered.length === 0" class="flex flex-col items-center justify-center py-32 text-center">
          <div
            class="w-20 h-20 bg-white rounded-[1.75rem] border border-slate-100 flex items-center justify-center mb-6">
            <i class="fa-solid fa-vault text-3xl text-slate-200"></i>
          </div>
          <h3 class="font-black text-slate-800 text-xl tracking-tight mb-2">Vault is empty</h3>
          <p class="text-sm text-slate-400 font-medium max-w-[260px] leading-relaxed mb-6">
            Your secure enclave is ready. Add your first credential to get started.
          </p>
          <button @click="showAddModal = true"
            class="h-9 px-5 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-lg shadow-orange-100 flex items-center gap-2 text-xs uppercase tracking-widest">
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

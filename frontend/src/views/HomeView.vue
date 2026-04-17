<template>
  <div class="bg-black text-slate-300 min-h-screen flex selection:bg-orange-500 selection:text-white">

    <!-- Sidebar -->
    <aside class="hidden md:flex flex-col w-64 border-r border-white/10 p-8 bg-[#0a0a0a] sticky top-0 h-screen">
      <div class="flex items-center gap-2 mb-10">
        <div class="bg-white p-1.5 rounded-lg">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-black" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
          </svg>
        </div>
        <span class="font-bold text-lg tracking-tight text-white">SaveMyPass<span class="text-orange-500">.dev</span></span>
      </div>

      <nav class="space-y-1 flex-1">
        <router-link to="/home" class="flex items-center gap-3 px-4 py-3 rounded-2xl bg-white/10 text-white font-semibold text-sm">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
          </svg>
          Dashboard
        </router-link>
        <router-link to="/profile" class="flex items-center gap-3 px-4 py-3 rounded-2xl text-slate-500 hover:bg-white/5 hover:text-white transition-colors font-semibold text-sm">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          Profile
        </router-link>

        <button @click="triggerExport" :disabled="exporting"
          class="w-full flex items-center gap-3 px-4 py-3 rounded-2xl text-slate-500 hover:bg-white/5 hover:text-white transition-colors font-semibold text-sm disabled:opacity-50">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
          </svg>
          Export PDF
        </button>
      </nav>

      <!-- User footer -->
      <div class="border-t border-white/10 pt-6">
        <div class="flex items-center gap-3">
          <div class="w-9 h-9 bg-white/10 rounded-xl flex items-center justify-center text-white font-bold text-sm">
            {{ initials }}
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-xs font-bold text-slate-500 truncate">{{ auth.name }}</p>
            <p class="text-sm font-bold truncate text-white">{{ auth.email }}</p>
          </div>
        </div>
        <button @click="handleLogout" class="text-xs text-rose-500 font-bold mt-3 inline-block hover:underline">Log Out</button>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="flex-1 p-8 md:p-10 overflow-y-auto">

      <!-- Header -->
      <div class="flex items-center justify-between mb-10">
        <div>
          <h1 class="text-3xl font-extrabold text-white tracking-tight">My Vault</h1>
          <p class="text-slate-500 mt-1 font-medium">{{ vault.credentials.length }} saved credential{{ vault.credentials.length !== 1 ? 's' : '' }}, all encrypted.</p>
        </div>
        <button @click="showAddModal = true"
          class="bg-white hover:bg-slate-200 text-black font-bold px-6 py-3 rounded-2xl transition-all shadow-[0_0_20px_rgba(255,255,255,0.1)] active:scale-[0.98] flex items-center gap-2">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          New Entry
        </button>
      </div>

      <!-- Search -->
      <div class="mb-8">
        <div class="relative max-w-md">
          <span class="absolute inset-y-0 left-0 pl-4 flex items-center text-slate-500">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0" />
            </svg>
          </span>
          <input v-model="search" type="text" placeholder="Search credentials…"
            class="w-full bg-[#0a0a0a] border border-white/10 rounded-2xl pl-12 pr-4 py-3 text-white focus:bg-[#111] focus:border-white/30 focus:outline-none transition-all placeholder:text-slate-600 shadow-sm" />
        </div>
      </div>

      <!-- Export success toast -->
      <div v-if="exportMsg"
        class="mb-6 p-4 bg-emerald-500/10 border border-emerald-500/20 rounded-2xl flex items-center gap-3 text-emerald-500">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
        </svg>
        <span class="font-semibold text-sm">{{ exportMsg }}</span>
      </div>

      <!-- Loading -->
      <div v-if="vault.loading" class="flex flex-col items-center justify-center py-24 gap-4">
        <svg class="animate-spin h-8 w-8 text-white" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
        </svg>
        <p class="text-slate-500 font-medium">Decrypting your vault…</p>
      </div>

      <!-- Error -->
      <div v-else-if="vault.error"
        class="p-4 bg-rose-500/10 border border-rose-500/20 rounded-2xl text-rose-500 flex items-center gap-3">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
        </svg>
        <span class="font-semibold text-sm">{{ vault.error }}</span>
      </div>

      <!-- Empty state -->
      <div v-else-if="vault.credentials.length === 0" class="flex flex-col items-center justify-center py-24 gap-4">
        <div class="w-20 h-20 bg-white/5 rounded-3xl flex items-center justify-center border border-white/10">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
          </svg>
        </div>
        <h2 class="text-xl font-bold text-white">Your vault is empty</h2>
        <p class="text-slate-500 font-medium text-sm">Add your first credential to get started.</p>
        <button @click="showAddModal = true"
          class="bg-white hover:bg-slate-200 text-black font-bold px-6 py-3 rounded-2xl transition-all mt-2">
          Add First Entry
        </button>
      </div>

      <!-- Credentials grid -->
      <div v-else class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4">
        <CredentialCard
          v-for="cred in filtered"
          :key="cred.id"
          :credential="cred"
          @edit="openEdit(cred)"
          @delete="confirmDelete(cred)"
        />
        <div v-if="filtered.length === 0" class="col-span-full text-center py-12 text-slate-500 font-medium">
          No results for "{{ search }}"
        </div>
      </div>
    </main>

    <!-- Add Modal -->
    <AddCredentialModal v-if="showAddModal" @close="showAddModal = false" />

    <!-- Edit Modal -->
    <EditCredentialModal v-if="editingCred" :credential="editingCred" @close="editingCred = null" />

    <!-- Delete confirm modal -->
    <div v-if="deletingCred" class="fixed inset-0 z-[60] flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/80 backdrop-blur-md" @click="deletingCred = null"></div>
      <div class="relative w-full max-w-md bg-[#0a0a0a] rounded-3xl p-10 shadow-2xl border border-white/10">
        <h2 class="text-2xl font-extrabold text-white mb-2">Delete credential?</h2>
        <p class="text-slate-400 text-sm mb-8 font-medium">
          This will permanently remove <strong class="text-white">{{ deletingCred.site }}</strong> from your vault.
        </p>
        <div class="flex gap-3">
          <button @click="deletingCred = null"
            class="flex-1 border border-white/10 text-white font-bold py-3 rounded-2xl hover:bg-white/5 transition-colors">
            Cancel
          </button>
          <button @click="doDelete" :disabled="deleting"
            class="flex-1 bg-rose-500 hover:bg-rose-600 text-white font-bold py-3 rounded-2xl transition-all flex items-center justify-center gap-2 disabled:opacity-60">
            <svg v-if="deleting" class="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
            </svg>
            Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useVaultStore } from '@/stores/vault'
import CredentialCard from '@/components/CredentialCard.vue'
import AddCredentialModal from '@/components/AddCredentialModal.vue'
import EditCredentialModal from '@/components/EditCredentialModal.vue'

const router = useRouter()
const auth = useAuthStore()
const vault = useVaultStore()

const search = ref('')
const showAddModal = ref(false)
const editingCred = ref(null)
const deletingCred = ref(null)
const deleting = ref(false)
const exporting = ref(false)
const exportMsg = ref(null)

const initials = computed(() =>
  auth.name?.split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase() || '?'
)

const filtered = computed(() => {
  if (!search.value.trim()) return vault.credentials
  const q = search.value.toLowerCase()
  return vault.credentials.filter(c =>
    c.site.toLowerCase().includes(q) || c.username.toLowerCase().includes(q)
  )
})

function openEdit(cred) { editingCred.value = { ...cred } }
function confirmDelete(cred) { deletingCred.value = cred }

async function doDelete() {
  deleting.value = true
  try {
    await vault.removeCredential(deletingCred.value.id)
    deletingCred.value = null
  } finally {
    deleting.value = false
  }
}

async function triggerExport() {
  exporting.value = true
  try {
    await vault.exportVault()
    exportMsg.value = 'PDF Downloaded Successfully!'
    setTimeout(() => exportMsg.value = null, 4000)
  } catch (err) {
    console.error('Export error:', err)
    exportMsg.value = 'Export failed. Please try again.'
  } finally {
    exporting.value = false
  }
}

function handleLogout() {
  auth.logout()
  router.push('/login')
}

onMounted(() => vault.fetchCredentials())
</script>

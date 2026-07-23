<template>
  <div class="view-root">

    <!-- Floating Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar">
        <!-- Search -->
        <div class="toolbar-search-wrap group">
          <i class="fa-solid fa-magnifying-glass toolbar-search-icon"></i>
          <input
            ref="searchInput"
            v-model="search"
            type="text"
            placeholder="Search vault…"
            class="toolbar-search-input"
          />
          <kbd class="toolbar-kbd group-focus-within:opacity-0">
            <span class="kbd-key">⌘</span>
            <span class="kbd-key">K</span>
          </kbd>
        </div>

        <div class="toolbar-actions">
          <button @click="triggerExport" :disabled="exporting" class="btn-ghost">
            <i class="fa-solid fa-share-from-square"></i>
            <span class="hidden sm:inline">Export</span>
          </button>
          <button @click="showAddModal = true" class="btn-primary">
            <i class="fa-solid fa-plus text-xs"></i>
            <span class="hidden sm:inline">New Entry</span>
          </button>
        </div>
      </header>
    </div>

    <!-- Main Content -->
    <main class="view-main">

      <!-- Loading -->
      <div v-if="vault.loading" class="flex flex-col items-center justify-center py-40">
        <div class="loading-spinner"></div>
      </div>

      <div v-else class="content-container">

        <!-- Export message -->
        <div v-if="exportMsg" class="island-alert island-alert--success">
          <i class="fa-solid fa-circle-check"></i>
          {{ exportMsg }}
        </div>

        <!-- Section header -->
        <div class="section-header">
          <span class="section-label">
            Vault Items
            <span class="section-badge">{{ filtered.length }}</span>
          </span>
          <div class="section-sort">
            <i class="fa-solid fa-arrow-down-wide-short text-[11px]"></i>
            <span class="hidden xs:inline">Recently updated</span>
          </div>
        </div>

        <!-- Credentials island card -->
        <div v-if="filtered.length > 0" class="island-card">
          <div
            v-for="(cred, index) in filtered"
            :key="cred.id"
            @click="goToDetail(cred.id)"
            class="list-row"
            :class="{ 'list-row--last': index === filtered.length - 1 }"
          >
            <div class="list-favicon">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${cred.site}&size=64`"
                :alt="cred.site"
                class="w-5 h-5 object-contain"
                @error="$event.target.style.display = 'none'"
              />
              <i v-if="!cred.site" class="fa-solid fa-globe text-slate-500 text-sm"></i>
            </div>
            <div class="list-info">
              <p class="list-title">{{ cred.site }}</p>
              <p class="list-sub">{{ cred.username }}</p>
            </div>
            <div class="list-meta">
              <span class="list-date hidden sm:block">
                {{ new Date(cred.updatedAt).toLocaleDateString(undefined, { month: 'short', day: 'numeric' }) }}
              </span>
              <i class="fa-solid fa-chevron-right list-arrow"></i>
            </div>
          </div>
        </div>

        <!-- Empty state -->
        <div v-if="filtered.length === 0" class="empty-state">
          <div class="empty-icon">
            <i class="fa-solid fa-folder-closed text-[28px] text-slate-500"></i>
          </div>
          <h3 class="empty-title">Vault is empty</h3>
          <p class="empty-desc">Your secure enclave is ready. Add your first credential to get started.</p>
          <button @click="showAddModal = true" class="btn-primary mt-2">Add Entry</button>
        </div>

      </div>
    </main>

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

function goToDetail(id) { router.push(`/credentials/${id}`) }

async function triggerExport() {
  exporting.value = true
  try {
    await vault.exportVault()
    exportMsg.value = 'Vault exported successfully'
    setTimeout(() => exportMsg.value = null, 4000)
  } catch (err) {
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

<style scoped>
@import '@/assets/island-theme.css';
</style>
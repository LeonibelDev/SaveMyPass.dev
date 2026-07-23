<template>
  <div class="view-root">

    <!-- Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar">
        <div class="flex items-center gap-4 flex-1 min-w-0">
          <h1 class="toolbar-title">Notes</h1>
          <!-- Search -->
          <div class="toolbar-search-wrap group">
            <i class="fa-solid fa-magnifying-glass toolbar-search-icon"></i>
            <input
              ref="searchInput"
              v-model="searchQuery"
              type="text"
              placeholder="Search notes…"
              class="toolbar-search-input"
            />
          </div>
        </div>
        <div class="toolbar-actions">
          <router-link to="/notes/new" class="btn-primary">
            <i class="fa-solid fa-plus text-xs"></i>
            <span class="hidden sm:inline">New Note</span>
          </router-link>
        </div>
      </header>
    </div>

    <!-- Loading -->
    <div v-if="loading && notes.length === 0" class="flex-1 flex items-center justify-center">
      <div class="loading-spinner"></div>
    </div>

    <!-- Empty state -->
    <div v-else-if="notes.length === 0" class="view-main">
      <div class="content-container">
        <div class="empty-state" style="padding-top: 100px;">
          <div class="empty-icon">
            <i class="fa-solid fa-file-lines text-[28px] text-slate-500"></i>
          </div>
          <h3 class="empty-title">Secure Workspace</h3>
          <p class="empty-desc">Store sensitive documents, recovery keys, or personal thoughts behind your master encryption.</p>
          <router-link to="/notes/new" class="btn-primary">
            <i class="fa-solid fa-plus text-xs"></i>
            Create first note
          </router-link>
        </div>
      </div>
    </div>

    <!-- Notes grid -->
    <main v-else class="view-main">
      <div style="max-width: 1100px; margin: 0 auto;">

        <!-- Filter info -->
        <div v-if="searchQuery" class="section-header">
          <span class="section-label">
            {{ filteredNotes.length }} results for "{{ searchQuery }}"
          </span>
          <button @click="searchQuery = ''" class="btn-ghost" style="height:28px; padding: 0 10px; font-size: 12px;">
            Clear
          </button>
        </div>

        <!-- No results -->
        <div v-if="filteredNotes.length === 0" class="empty-state">
          <div class="empty-icon"><i class="fa-solid fa-folder-open text-[24px] text-slate-500"></i></div>
          <h3 class="empty-title">No notes match</h3>
          <p class="empty-desc">Try a different keyword.</p>
        </div>

        <!-- Grid -->
        <div v-else class="notes-grid">
          <div
            v-for="note in filteredNotes"
            :key="note.id"
            class="note-card"
            @click="goToDetail(note.id)"
          >
            <div class="note-card-head">
              <div class="note-card-icon">
                <i class="fa-solid fa-file-lines"></i>
              </div>
              <span class="note-card-date">{{ formatDate(note.updatedAt || note.createdAt) }}</span>
            </div>
            <h3 class="note-card-title">{{ note.title }}</h3>
            <p class="note-card-preview">{{ stripHtml(note.content) }}</p>
            <div class="note-card-footer">
              <span>Open Note</span>
              <i class="fa-solid fa-chevron-right text-[10px]"></i>
            </div>
          </div>
        </div>

      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useNotesStore } from '../stores/notes'

const useNotes = useNotesStore()
const { notes, loading } = storeToRefs(useNotes)
const router = useRouter()

const searchQuery = ref('')
const searchInput = ref(null)

const filteredNotes = computed(() => {
  if (!searchQuery.value) return notes.value
  const query = searchQuery.value.toLowerCase()
  return notes.value.filter(note =>
    note.title.toLowerCase().includes(query) ||
    stripHtml(note.content).toLowerCase().includes(query)
  )
})

function goToDetail(id) { router.push(`/notes/${id}`) }

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Intl.DateTimeFormat('en-US', { month: 'short', day: 'numeric' }).format(new Date(dateStr))
}

function stripHtml(html) {
  const tmp = document.createElement('DIV')
  tmp.innerHTML = html
  return tmp.textContent || tmp.innerText || ''
}

function handleGlobalKey(e) {
  if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
    e.preventDefault()
    searchInput.value?.focus()
  }
}

onMounted(() => {
  useNotes.fetchNotes()
  window.addEventListener('keydown', handleGlobalKey)
})
</script>

<style scoped>
@import '@/assets/island-theme.css';

.notes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 12px;
  animation: fadeUp 0.3s ease forwards;
}

.note-card {
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 16px;
  padding: 18px;
  cursor: pointer;
  transition: all 0.15s ease;
  display: flex;
  flex-direction: column;
  min-height: 180px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.05), inset 0 1px 0 rgba(255, 255, 255, 1);
  backdrop-filter: blur(12px);
}

.note-card:hover {
  border-color: rgba(249, 115, 22, 0.25);
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(0, 0, 0, 0.08), inset 0 1px 0 rgba(255, 255, 255, 1);
}

.note-card-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.note-card-icon {
  width: 32px;
  height: 32px;
  border-radius: 9px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
  font-size: 13px;
  transition: color 0.15s;
}

.note-card:hover .note-card-icon { color: #f97316; }

.note-card-date {
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: #94a3b8;
  font-family: 'Outfit', sans-serif;
}

.note-card-title {
  font-family: 'Outfit', sans-serif;
  font-size: 15px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
  margin-bottom: 6px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  transition: color 0.15s;
}

.note-card:hover .note-card-title { color: #f97316; }

.note-card-preview {
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
  line-height: 1.6;
  flex: 1;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.note-card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 12px;
  padding-top: 10px;
  border-top: 1px solid rgba(0, 0, 0, 0.04);
  font-size: 11px;
  font-weight: 700;
  color: #f97316;
  font-family: 'Outfit', sans-serif;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  opacity: 0;
  transform: translateY(4px);
  transition: all 0.15s;
}

.note-card:hover .note-card-footer {
  opacity: 1;
  transform: translateY(0);
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(8px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
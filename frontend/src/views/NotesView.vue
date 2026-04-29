<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F2F2F7]">

    <!-- Header -->
    <header
      class="h-16 border-b border-slate-200/60 flex items-center px-4 sm:px-6 justify-between flex-shrink-0 bg-white/80 backdrop-blur-xl z-20">
      <div class="flex items-center gap-4 sm:gap-8 flex-1 max-w-4xl">
        <h1 class="text-[17px] font-bold text-slate-900 tracking-tight hidden xs:block">
          Notes
        </h1>

        <!-- Search Bar -->
        <div class="relative flex-1 max-w-sm group">
          <span
            class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 group-focus-within:text-brand-orange transition-colors pointer-events-none">
            <i class="fa-solid fa-magnifying-glass text-[12px]"></i>
          </span>
          <input ref="searchInput" v-model="searchQuery" type="text" placeholder="Search notes"
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
      </div>

      <div class="flex items-center gap-3 ml-3 sm:ml-0">
        <router-link to="/notes/new"
          class="h-9 w-9 sm:w-auto sm:px-5 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-sm flex items-center justify-center sm:gap-2 active:scale-95 text-[12px]">
          <i class="fa-solid fa-plus text-[11px]"></i>
          <span class="hidden sm:inline">New Note</span>
        </router-link>
      </div>
    </header>

    <!-- Main -->
    <main v-if="loading && notes.length === 0" class="flex-1 flex items-center justify-center">
      <div class="w-8 h-8 border-[3px] border-slate-200 border-t-brand-orange rounded-full animate-spin"></div>
    </main>

    <main v-else-if="notes.length === 0" class="flex-1 overflow-y-auto no-scrollbar">
      <div class="max-w-5xl mx-auto py-10 sm:py-20 px-4 sm:px-8">

        <!-- Empty State -->
        <div
          class="flex flex-col items-center justify-center text-center py-16 sm:py-24 bg-white rounded-[24px] border border-slate-200/60 shadow-sm px-6">
          <!-- Icon -->
          <div
            class="w-20 h-20 bg-orange-50 rounded-[20px] flex items-center justify-center mb-8">
            <i class="fa-solid fa-note-sticky text-3xl text-brand-orange"></i>
          </div>

          <!-- Title -->
          <h2 class="font-bold text-slate-900 text-xl tracking-tight mb-3">
            Secure Workspace
          </h2>

          <!-- Description -->
          <p class="text-[15px] text-slate-500 font-medium max-w-[320px] leading-relaxed mb-10">
            Store sensitive documents, recovery keys, or personal thoughts behind your master encryption.
          </p>

          <!-- CTA -->
          <router-link to="/notes/new"
            class="h-11 px-8 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-sm flex items-center gap-3 text-[14px] active:scale-95">
            <i class="fa-solid fa-plus text-[11px]"></i>
            Create first note
          </router-link>
        </div>

      </div>
    </main>

    <main v-else class="flex-1 overflow-y-auto px-4 sm:px-8 py-6 sm:py-10 no-scrollbar">
      <div class="max-w-7xl mx-auto">
        
        <!-- Filter Info -->
        <div v-if="searchQuery" class="mb-6 flex items-center justify-between px-1">
          <p class="text-[13px] text-slate-400 font-semibold">
            Found <span class="text-slate-900">{{ filteredNotes.length }}</span> results
          </p>
          <button @click="searchQuery = ''" class="text-[13px] font-bold text-brand-orange hover:underline">Clear</button>
        </div>

        <div v-if="filteredNotes.length === 0" class="py-20 text-center">
          <div class="w-16 h-16 bg-white rounded-[20px] border border-slate-200/60 flex items-center justify-center mx-auto mb-4 shadow-sm">
            <i class="fa-solid fa-magnifying-glass text-slate-200 text-xl"></i>
          </div>
          <h3 class="font-bold text-slate-900 mb-1">No notes match</h3>
          <p class="text-[14px] text-slate-400">Try a different keyword.</p>
        </div>

        <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 sm:gap-6">
          <div v-for="note in filteredNotes" :key="note.id"
            class="bg-white rounded-[20px] border border-slate-200/60 p-5 sm:p-6 hover:border-brand-orange/30 cursor-pointer transition-all hover:shadow-md active:bg-slate-50 group flex flex-col h-[200px] sm:h-[220px]"
            @click="goToDetail(note.id)">
            
            <div class="flex justify-between items-start mb-4">
              <div
                class="w-10 h-10 bg-slate-50 rounded-xl flex items-center justify-center group-hover:scale-105 transition-transform">
                <i class="fa-solid fa-file-lines text-slate-400 group-hover:text-brand-orange transition-colors"></i>
              </div>
              <span class="text-[10px] font-bold uppercase tracking-wider text-slate-300">
                {{ formatDate(note.updatedAt || note.createdAt) }}
              </span>
            </div>

            <h3
              class="font-bold text-slate-900 text-[16px] mb-2 group-hover:text-brand-orange transition-colors line-clamp-1 tracking-tight">
              {{ note.title }}
            </h3>
            
            <p class="text-[13px] text-slate-400 leading-relaxed font-medium line-clamp-3 sm:line-clamp-4 flex-1"
              v-text="stripHtml(note.content)">
            </p>

            <div
              class="mt-4 pt-4 border-t border-slate-50 flex items-center justify-between opacity-0 group-hover:opacity-100 transition-all transform translate-y-2 group-hover:translate-y-0">
              <span class="text-[11px] font-bold text-brand-orange uppercase tracking-wider">Open Note</span>
              <i class="fa-solid fa-chevron-right text-[10px] text-brand-orange"></i>
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

function goToDetail(id) {
  router.push(`/notes/${id}`)
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return new Intl.DateTimeFormat('en-US', { month: 'short', day: 'numeric' }).format(date)
}

function stripHtml(html) {
  const tmp = document.createElement("DIV");
  tmp.innerHTML = html;
  return tmp.textContent || tmp.innerText || "";
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
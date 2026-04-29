<template>
  <div class="flex-1 flex flex-col bg-[#F2F2F7] overflow-hidden selection:bg-brand-orange/20">

    <!-- Header -->
    <header class="h-16 border-b border-slate-200/60 bg-white/80 backdrop-blur-xl flex items-center px-4 sm:px-6 z-30 flex-shrink-0">
      <div class="max-w-5xl mx-auto w-full flex items-center justify-between">
        <button @click="goBack"
          class="flex items-center gap-1 text-brand-orange hover:opacity-70 transition-opacity active:opacity-50">
          <i class="fa-solid fa-chevron-left text-sm"></i>
          <span class="text-[17px] font-medium">Notes</span>
        </button>

        <div class="flex items-center gap-3">
          <button @click="save" :disabled="!isDirty || saving"
            class="h-9 px-4 text-brand-orange font-bold text-[17px] disabled:opacity-30 transition-all active:opacity-50">
            {{ saving ? 'Saving…' : 'Done' }}
          </button>
          <button @click="showDeleteModal = true"
            class="w-9 h-9 rounded-full text-slate-300 hover:text-rose-500 hover:bg-rose-50 transition-all flex items-center justify-center active:scale-90">
            <i class="fa-solid fa-trash-can text-[14px]"></i>
          </button>
        </div>
      </div>
    </header>

    <!-- Editor Container -->
    <div class="flex-1 overflow-hidden flex flex-col">
      
      <!-- Formatting Toolbar -->
      <div class="bg-white/50 backdrop-blur-md border-b border-slate-100 px-4 sm:px-8 py-2 sticky top-0 z-20 overflow-x-auto no-scrollbar">
        <div class="max-w-5xl mx-auto flex items-center gap-1.5 min-w-max">
          <button @click="format('bold')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90" title="Bold">
            <i class="fa-solid fa-bold text-[13px]"></i>
          </button>
          <button @click="format('italic')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90" title="Italic">
            <i class="fa-solid fa-italic text-[13px]"></i>
          </button>
          <button @click="format('underline')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90" title="Underline">
            <i class="fa-solid fa-underline text-[13px]"></i>
          </button>
          <div class="w-px h-5 bg-slate-200 mx-1.5"></div>
          <button @click="format('insertUnorderedList')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90" title="Bullets">
            <i class="fa-solid fa-list-ul text-[13px]"></i>
          </button>
          <button @click="format('insertOrderedList')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90" title="Numbers">
            <i class="fa-solid fa-list-ol text-[13px]"></i>
          </button>
          <div class="w-px h-5 bg-slate-200 mx-1.5"></div>
          <button @click="format('justifyLeft')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90">
            <i class="fa-solid fa-align-left text-[13px]"></i>
          </button>
          <button @click="format('justifyCenter')" 
            class="w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 text-slate-600 transition-all active:scale-90">
            <i class="fa-solid fa-align-center text-[13px]"></i>
          </button>
        </div>
      </div>

      <!-- Main Editor -->
      <main v-if="loading" class="flex-1 flex items-center justify-center bg-white">
        <div class="w-8 h-8 border-[3px] border-slate-200 border-t-brand-orange rounded-full animate-spin"></div>
      </main>

      <main v-else-if="error" class="flex-1 flex items-center justify-center bg-white">
        <div class="text-center px-6">
          <div class="w-20 h-20 bg-rose-50 rounded-[24px] flex items-center justify-center mb-6 mx-auto">
            <i class="fa-solid fa-circle-exclamation text-rose-500 text-3xl"></i>
          </div>
          <h3 class="font-bold text-slate-900 text-xl tracking-tight mb-2">Note not found</h3>
          <p class="text-[15px] text-slate-400 font-medium mb-8">This note might have been removed.</p>
          <button @click="goBack" class="text-brand-orange font-bold text-[17px]">Go back to notes</button>
        </div>
      </main>

      <main v-else class="flex-1 overflow-y-auto bg-white no-scrollbar">
        <div class="max-w-4xl mx-auto px-6 sm:px-12 py-10 sm:py-16">

          <!-- Meta -->
          <div class="flex items-center gap-3 mb-6 text-slate-300 text-[13px] font-medium">
            <span>{{ formattedDate }}</span>
            <span v-if="isDirty" class="text-brand-orange">• Unsaved</span>
          </div>

          <!-- Title input -->
          <input v-model="title" type="text" placeholder="Title"
            class="w-full text-[32px] sm:text-[40px] font-bold text-slate-900 tracking-tight bg-transparent border-none outline-none placeholder:text-slate-100 mb-6 focus:placeholder:text-slate-50 transition-all" />

          <!-- Body editor -->
          <div ref="bodyRef" v-once contenteditable="true" @input="onInput" @keydown="handleKeydown"
            data-placeholder="Start writing…"
            class="w-full min-h-[500px] outline-none text-[17px] text-slate-700 leading-relaxed font-medium empty:before:content-[attr(data-placeholder)] empty:before:text-slate-200 editor-content">
          </div>

        </div>
      </main>
    </div>

    <!-- Delete Alert -->
    <Transition name="fade">
      <div v-if="showDeleteModal"
        class="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-black/30 backdrop-blur-sm">
        <div
          class="bg-white/90 backdrop-blur-xl rounded-[24px] p-8 max-w-sm w-full shadow-2xl text-center border border-white/20">
          <h3 class="text-[19px] font-bold text-slate-900 mb-2">Delete Note?</h3>
          <p class="text-[14px] text-slate-500 font-medium leading-relaxed mb-8">
            This action cannot be undone. Your encrypted note will be permanently removed.
          </p>
          <div class="flex flex-col gap-2">
            <button @click="handleDelete"
              class="w-full py-3.5 bg-rose-500 text-white rounded-xl font-bold text-[16px] active:opacity-70 transition-all">
              Delete
            </button>
            <button @click="showDeleteModal = false"
              class="w-full py-3.5 text-brand-orange font-semibold text-[16px] active:opacity-70 transition-all">
              Cancel
            </button>
          </div>
        </div>
      </div>
    </Transition>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNotesStore } from '@/stores/notes'

const router = useRouter()
const route = useRoute()
const notesStore = useNotesStore()

const noteId = route.params.id

// State
const title = ref('')
const body = ref('')
const bodyRef = ref(null)

const originalTitle = ref('')
const originalBody = ref('')

const loading = ref(true)
const error = ref(false)
const saving = ref(false)
const showDeleteModal = ref(false)

const updatedAt = ref(null)

// Dirty check
const isDirty = computed(() =>
    title.value !== originalTitle.value || body.value !== originalBody.value
)

// Formatted date
const formattedDate = computed(() => {
    if (!updatedAt.value) return 'Just now'
    return new Intl.DateTimeFormat('en-US', {
        month: 'short', day: 'numeric', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    }).format(new Date(updatedAt.value))
})

// Load note
onMounted(async () => {
    try {
        const note = await notesStore.getNoteById(noteId)
        title.value = note.title
        body.value = note.content
        originalTitle.value = note.title
        originalBody.value = note.content
        updatedAt.value = note.updatedAt ?? note.createdAt ?? null

    } catch (e) {
        error.value = true
    } finally {
        loading.value = false
        await nextTick()
        if (bodyRef.value) {
            bodyRef.value.innerHTML = originalBody.value
        }
    }
})

const goBack = () => router.push('/notes')

const onInput = () => {
    body.value = bodyRef.value.innerHTML
}

const format = (cmd, val = null) => {
    document.execCommand(cmd, false, val)
    onInput()
    if (bodyRef.value) bodyRef.value.focus()
}

const handleKeydown = (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'b') {
        e.preventDefault()
        format('bold')
    }
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'i') {
        e.preventDefault()
        format('italic')
    }
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'u') {
        e.preventDefault()
        format('underline')
    }
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 's') {
        e.preventDefault()
        save()
    }
}

const save = async () => {
    if (!title.value.trim() || !isDirty.value) return
    saving.value = true
    try {
        await notesStore.updateNote(noteId, title.value, body.value, 'note')
        originalTitle.value = title.value
        originalBody.value = body.value
        updatedAt.value = new Date().toISOString()
    } catch (e) {
        console.error('Failed to save note:', e)
    } finally {
        saving.value = false
    }
}

const handleDelete = async () => {
    try {
        await notesStore.deleteNote(noteId)
        router.push('/notes')
    } catch (e) {
        console.error('Failed to delete note:', e)
    } finally {
        showDeleteModal.value = false
    }
}
</script>

<style scoped>
.editor-content:focus {
    outline: none;
}

:deep(.editor-content ul) {
    list-style-type: disc;
    padding-left: 1.5rem;
    margin: 1rem 0;
}

:deep(.editor-content ol) {
    list-style-type: decimal;
    padding-left: 1.5rem;
    margin: 1rem 0;
}

:deep(.editor-content b), :deep(.editor-content strong) {
    font-weight: 700;
}
</style>
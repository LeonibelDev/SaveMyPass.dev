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

        <button @click="save" :disabled="!title.trim() || saving"
          class="h-9 px-4 text-brand-orange font-bold text-[17px] disabled:opacity-30 transition-all active:opacity-50">
          {{ saving ? 'Creating…' : 'Save' }}
        </button>
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
      <main class="flex-1 overflow-y-auto bg-white no-scrollbar">
        <div class="max-w-4xl mx-auto px-6 sm:px-12 py-10 sm:py-16">

          <!-- Title input -->
          <input v-model="title" type="text" placeholder="Note Title"
            class="w-full text-[32px] sm:text-[40px] font-bold text-slate-900 tracking-tight bg-transparent border-none outline-none placeholder:text-slate-100 mb-6 focus:placeholder:text-slate-50 transition-all" />

          <!-- Body editor -->
          <div ref="bodyRef" v-once contenteditable="true" @input="onInput" @keydown="handleKeydown"
            data-placeholder="Start writing your secure note…"
            class="w-full min-h-[500px] outline-none text-[17px] text-slate-700 leading-relaxed font-medium empty:before:content-[attr(data-placeholder)] empty:before:text-slate-200 editor-content">
          </div>

        </div>
      </main>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useNotesStore } from '@/stores/notes'
const router = useRouter()
const notesStore = useNotesStore()

const title = ref('')
const body = ref('')
const bodyRef = ref(null)
const saving = ref(false)

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
  if (!title.value.trim() || saving.value) return
  saving.value = true
  try {
    await notesStore.addNote(title.value, body.value, 'note')
    goBack()
  } catch (e) {
    console.error('Failed to create note:', e)
  } finally {
    saving.value = false
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
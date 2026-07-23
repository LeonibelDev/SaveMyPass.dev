<template>
  <div class="view-root">

    <!-- Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar island-toolbar--titled">
        <button @click="goBack" class="btn-back">
          <i class="fa-solid fa-chevron-left text-sm"></i>
          Notes
        </button>
        <div class="toolbar-actions">
          <button @click="save" :disabled="!title.trim() || saving" class="btn-primary">
            {{ saving ? 'Creating…' : 'Save' }}
          </button>
        </div>
      </header>
    </div>

    <!-- Island Formatting Toolbar -->
    <div class="format-toolbar-wrap">
      <div class="format-toolbar">
        <button @click="format('bold')"                class="fmt-btn" title="Bold"><i class="fa-solid fa-bold"></i></button>
        <button @click="format('italic')"              class="fmt-btn" title="Italic"><i class="fa-solid fa-italic"></i></button>
        <button @click="format('underline')"           class="fmt-btn" title="Underline"><i class="fa-solid fa-underline"></i></button>
        <div class="fmt-divider"></div>
        <button @click="format('insertUnorderedList')" class="fmt-btn" title="Bullets"><i class="fa-solid fa-list-ul"></i></button>
        <button @click="format('insertOrderedList')"   class="fmt-btn" title="Numbers"><i class="fa-solid fa-list-ol"></i></button>
        <div class="fmt-divider"></div>
        <button @click="format('justifyLeft')"         class="fmt-btn"><i class="fa-solid fa-align-left"></i></button>
        <button @click="format('justifyCenter')"       class="fmt-btn"><i class="fa-solid fa-align-center"></i></button>
      </div>
    </div>

    <!-- Editor island -->
    <div class="editor-area">
      <div class="editor-inner">
        <input
          v-model="title"
          type="text"
          placeholder="Note Title"
          class="editor-title-input"
        />
        <div
          ref="bodyRef"
          v-once
          contenteditable="true"
          @input="onInput"
          @keydown="handleKeydown"
          data-placeholder="Start writing your secure note…"
          class="editor-body"
        ></div>
      </div>
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
const onInput = () => { body.value = bodyRef.value.innerHTML }
const format = (cmd, val = null) => {
  document.execCommand(cmd, false, val)
  onInput()
  if (bodyRef.value) bodyRef.value.focus()
}

const handleKeydown = (e) => {
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'b') { e.preventDefault(); format('bold') }
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'i') { e.preventDefault(); format('italic') }
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'u') { e.preventDefault(); format('underline') }
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 's') { e.preventDefault(); save() }
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
@import '@/assets/island-theme.css';

.editor-title-input {
  width: 100%;
  font-family: 'Outfit', sans-serif;
  font-size: 32px;
  font-weight: 800;
  color: #0f172a;
  letter-spacing: -0.03em;
  background: transparent;
  border: none;
  outline: none;
  margin-bottom: 20px;
}
.editor-title-input::placeholder { color: #e2e8f0; }

.editor-body {
  width: 100%;
  min-height: 500px;
  outline: none;
  font-size: 15px;
  color: #334155;
  line-height: 1.8;
  font-weight: 500;
}

.editor-body:empty::before {
  content: attr(data-placeholder);
  color: #cbd5e1;
}

:deep(.editor-body ul) { list-style-type: disc; padding-left: 1.5rem; margin: 1rem 0; }
:deep(.editor-body ol) { list-style-type: decimal; padding-left: 1.5rem; margin: 1rem 0; }
:deep(.editor-body b), :deep(.editor-body strong) { font-weight: 700; color: #0f172a; }
</style>
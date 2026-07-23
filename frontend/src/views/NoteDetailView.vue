<template>
  <div class="view-root">

    <!-- Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar island-toolbar--titled">
        <button @click="goBack" class="btn-back">
          <i class="fa-solid fa-chevron-left text-sm"></i>
          <span>Notes</span>
        </button>
        <div class="toolbar-actions">
          <button @click="save" :disabled="!isDirty || saving" class="btn-primary">
            {{ saving ? 'Saving…' : 'Done' }}
          </button>
          <button @click="showDeleteModal = true" class="btn-danger">
            <i class="fa-solid fa-trash-can text-sm"></i>
          </button>
        </div>
      </header>
    </div>

    <!-- Island Formatting Toolbar -->
    <div class="format-toolbar-wrap">
      <div class="format-toolbar">
        <button @click="format('bold')"               class="fmt-btn" title="Bold"><i class="fa-solid fa-bold"></i></button>
        <button @click="format('italic')"             class="fmt-btn" title="Italic"><i class="fa-solid fa-italic"></i></button>
        <button @click="format('underline')"          class="fmt-btn" title="Underline"><i class="fa-solid fa-underline"></i></button>
        <div class="fmt-divider"></div>
        <button @click="format('insertUnorderedList')" class="fmt-btn" title="Bullets"><i class="fa-solid fa-list-ul"></i></button>
        <button @click="format('insertOrderedList')"  class="fmt-btn" title="Numbers"><i class="fa-solid fa-list-ol"></i></button>
        <div class="fmt-divider"></div>
        <button @click="format('justifyLeft')"        class="fmt-btn"><i class="fa-solid fa-align-left"></i></button>
        <button @click="format('justifyCenter')"      class="fmt-btn"><i class="fa-solid fa-align-center"></i></button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="flex-1 flex items-center justify-center">
      <div class="loading-spinner"></div>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="flex-1 flex items-center justify-center">
      <div class="empty-state">
        <div class="empty-icon"><i class="fa-solid fa-folder-open text-rose-500 text-[28px]"></i></div>
        <h3 class="empty-title">Note not found</h3>
        <p class="empty-desc">This note might have been removed.</p>
        <button @click="goBack" class="btn-ghost">Go back</button>
      </div>
    </div>

    <!-- Editor island -->
    <div v-else class="editor-area">
      <div class="editor-inner">
        <!-- Meta -->
        <div class="editor-meta">
          <span>{{ formattedDate }}</span>
          <span v-if="isDirty" class="editor-unsaved">• Unsaved</span>
        </div>

        <!-- Title -->
        <input
          v-model="title"
          type="text"
          placeholder="Title"
          class="editor-title-input"
        />

        <!-- Body -->
        <div
          ref="bodyRef"
          v-once
          contenteditable="true"
          @input="onInput"
          @keydown="handleKeydown"
          data-placeholder="Start writing…"
          class="editor-body"
        ></div>
      </div>
    </div>

    <!-- Island delete dialog (no backdrop) -->
    <Transition name="dialog-slide">
      <div v-if="showDeleteModal" class="island-dialog-wrap">
        <div class="island-dialog">
          <div class="dialog-icon"><i class="fa-solid fa-trash-can"></i></div>
          <h3 class="dialog-title">Delete Note?</h3>
          <p class="dialog-desc">This cannot be undone. Your encrypted note will be permanently removed.</p>
          <div class="dialog-actions">
            <button @click="showDeleteModal = false" class="dialog-btn-cancel">Cancel</button>
            <button @click="handleDelete" class="dialog-btn-delete">Delete</button>
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

const isDirty = computed(() =>
  title.value !== originalTitle.value || body.value !== originalBody.value
)

const formattedDate = computed(() => {
  if (!updatedAt.value) return 'Just now'
  return new Intl.DateTimeFormat('en-US', {
    month: 'short', day: 'numeric', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  }).format(new Date(updatedAt.value))
})

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
    if (bodyRef.value) bodyRef.value.innerHTML = originalBody.value
  }
})

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
@import '@/assets/island-theme.css';

.editor-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: #94a3b8;
  font-family: 'Outfit', sans-serif;
  margin-bottom: 16px;
}

.editor-unsaved { color: #f97316; }

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
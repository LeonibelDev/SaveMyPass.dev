<template>
    <div class="flex-1 flex flex-col bg-[#F7F8FA] overflow-hidden selection:bg-orange-100 selection:text-orange-900">

        <!-- Header -->
        <header class="h-16 border-b border-slate-100 bg-white flex items-center px-8 z-10 flex-shrink-0">
            <div class="max-w-4xl mx-auto w-full flex items-center justify-between">
                <button @click="goBack"
                    class="flex items-center gap-2 text-slate-400 hover:text-slate-900 transition-colors group">
                    <i class="fa-solid fa-arrow-left text-sm transition-transform group-hover:-translate-x-1"></i>
                    <span class="text-[10px] font-black uppercase tracking-[0.2em]">Back to notes</span>
                </button>

                <div class="flex items-center gap-3">
                    <!-- Delete button -->
                    <button @click="confirmDelete"
                        class="h-9 px-4 text-slate-400 hover:text-red-500 hover:bg-red-50 font-bold rounded-2xl transition-all flex items-center gap-2 text-xs uppercase tracking-widest">
                        <i class="fa-solid fa-trash text-[11px]"></i>
                        Delete
                    </button>

                    <!-- Save button -->
                    <button @click="save" :disabled="!title.trim() || !isDirty || saving"
                        class="h-9 px-6 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-2xl transition-all shadow-lg shadow-orange-100 active:scale-[0.98] disabled:opacity-40 flex items-center gap-2 text-xs uppercase tracking-widest">
                        <i v-if="saving" class="fa-solid fa-spinner animate-spin text-[12px]"></i>
                        <i v-else class="fa-solid fa-floppy-disk text-[12px]"></i>
                        {{ saving ? 'Saving…' : 'Save note' }}
                    </button>
                </div>
            </div>
        </header>

        <!-- Loading state -->
        <div v-if="loading" class="flex-1 flex items-center justify-center">
            <div class="flex flex-col items-center gap-4">
                <i class="fa-solid fa-spinner animate-spin text-slate-300 text-2xl"></i>
                <span class="text-[10px] font-black uppercase tracking-[0.2em] text-slate-300">Loading note…</span>
            </div>
        </div>

        <!-- Error state -->
        <div v-else-if="error" class="flex-1 flex items-center justify-center">
            <div class="flex flex-col items-center gap-4 text-center">
                <i class="fa-solid fa-circle-exclamation text-red-300 text-3xl"></i>
                <p class="text-sm font-semibold text-slate-500">Could not load this note.</p>
                <button @click="goBack"
                    class="text-[10px] font-black uppercase tracking-[0.2em] text-brand-orange hover:underline">
                    Go back
                </button>
            </div>
        </div>

        <!-- Editor -->
        <main v-else class="flex-1 overflow-y-auto">
            <div class="max-w-4xl mx-auto px-8 py-12">

                <!-- Meta info -->
                <div class="flex items-center gap-3 mb-8">
                    <span class="text-[10px] font-black uppercase tracking-[0.2em] text-slate-300">
                        <i class="fa-regular fa-clock mr-1"></i>
                        {{ formattedDate }}
                    </span>
                    <span v-if="isDirty"
                        class="text-[10px] font-black uppercase tracking-[0.2em] text-orange-400 flex items-center gap-1">
                        <span class="w-1.5 h-1.5 rounded-full bg-orange-400 inline-block"></span>
                        Unsaved changes
                    </span>
                </div>

                <!-- Title input -->
                <input v-model="title" type="text" placeholder="Note title…"
                    class="w-full text-4xl font-black text-slate-900 tracking-tighter bg-transparent border-none outline-none placeholder:text-slate-200 mb-8" />

                <!-- Divider -->
                <div class="border-t border-slate-100 mb-8"></div>

                <!-- Body editor -->
                <div ref="bodyRef" v-once contenteditable="true" @input="onInput" @keydown="handleKeydown"
                    data-placeholder="Start writing your secure note…"
                    class="w-full min-h-[400px] outline-none text-base text-slate-700 leading-relaxed font-medium empty:before:content-[attr(data-placeholder)] empty:before:text-slate-300">
                </div>

            </div>
        </main>

        <!-- Delete confirmation modal -->
        <Transition name="fade">
            <div v-if="showDeleteModal"
                class="fixed inset-0 z-50 flex items-center justify-center bg-black/20 backdrop-blur-sm px-4"
                @click.self="showDeleteModal = false">
                <div class="bg-white rounded-3xl shadow-2xl p-8 max-w-sm w-full">
                    <div class="w-12 h-12 rounded-2xl bg-red-50 flex items-center justify-center mb-5">
                        <i class="fa-solid fa-trash text-red-400"></i>
                    </div>
                    <h3 class="text-lg font-black text-slate-900 mb-2">Delete this note?</h3>
                    <p class="text-sm text-slate-400 mb-7">This action cannot be undone. The note will be permanently
                        removed.</p>
                    <div class="flex gap-3">
                        <button @click="showDeleteModal = false"
                            class="flex-1 h-10 rounded-2xl border border-slate-200 text-slate-500 text-xs font-black uppercase tracking-widest hover:bg-slate-50 transition-colors">
                            Cancel
                        </button>
                        <button @click="deleteNote" :disabled="deleting"
                            class="flex-1 h-10 rounded-2xl bg-red-500 hover:bg-red-600 text-white text-xs font-black uppercase tracking-widest transition-colors disabled:opacity-50">
                            {{ deleting ? 'Deleting…' : 'Delete' }}
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
const deleting = ref(false)
const showDeleteModal = ref(false)

const updatedAt = ref(null)

// Dirty check
const isDirty = computed(() =>
    title.value !== originalTitle.value || body.value !== originalBody.value
)

// Formatted date
const formattedDate = computed(() => {
    if (!updatedAt.value) return ''
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
        // Set contenteditable content after DOM is ready
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

const handleKeydown = (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'b') {
        e.preventDefault()
        document.execCommand('bold', false, null)
        onInput()
    }
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'i') {
        e.preventDefault()
        document.execCommand('italic', false, null)
        onInput()
    }
    if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'u') {
        e.preventDefault()
        document.execCommand('underline', false, null)
        onInput()
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

const confirmDelete = () => {
    showDeleteModal.value = true
}

const deleteNote = async () => {
    deleting.value = true
    try {
        await notesStore.deleteNote(noteId)
        router.push('/notes')
    } catch (e) {
        console.error('Failed to delete note:', e)
    } finally {
        deleting.value = false
        showDeleteModal.value = false
    }
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.15s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
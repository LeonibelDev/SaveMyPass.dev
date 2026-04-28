import { defineStore } from 'pinia'
import { ref } from 'vue'
import { notesApi } from '@/api/notes'
import { useAuthStore } from './auth'
import { encrypt, decrypt } from '@/crypto/aes'

export const useNotesStore = defineStore('notes', () => {
    const notes = ref([])
    const loading = ref(false)
    const error = ref(null)

    async function fetchNotes() {
        const auth = useAuthStore()
        loading.value = true
        error.value = null

        try {
            const res = await notesApi.list()

            // Decrypt everything in parallel for better performance
            const decryptedData = await Promise.all(res.data.data.map(async (note) => {
                try {
                    return {
                        ...note,
                        title: await decrypt(auth.aesKey, note.title),
                        content: await decrypt(auth.aesKey, note.content),
                        category: await decrypt(auth.aesKey, note.category)
                    }
                } catch (e) {
                    console.error('Failed to decrypt note:', note.id, e)
                    return {
                        ...note,
                        title: '[DECRYPTION FAILED]',
                        content: '[DECRYPTION FAILED]',
                        category: '[DECRYPTION FAILED]'
                    }
                }
            }));

            notes.value = decryptedData;
        } catch (e) {
            error.value = e?.response?.data?.message || 'Failed to load notes'
        } finally {
            loading.value = false
        }
    }

    async function addNote(title, content, category) {
        const auth = useAuthStore()
        loading.value = true
        error.value = null

        try {
            const encryptedTitle = await encrypt(auth.aesKey, title)
            const encryptedContent = await encrypt(auth.aesKey, content)
            const encryptedCategory = await encrypt(auth.aesKey, category)

            const res = await notesApi.add({
                title: encryptedTitle,
                content: encryptedContent,
                category: encryptedCategory
            })

            const newNote = {
                ...res.data.data,
                title,
                content,
                category
            }
            notes.value.push(newNote)
            return newNote
        } catch (e) {
            error.value = e?.response?.data?.message || 'Failed to add note'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function updateNote(id, title, content, category) {
        const auth = useAuthStore()
        loading.value = true
        error.value = null

        try {
            const encryptedTitle = await encrypt(auth.aesKey, title)
            const encryptedContent = await encrypt(auth.aesKey, content)
            const encryptedCategory = await encrypt(auth.aesKey, category)

            const res = await notesApi.update(id, {
                title: encryptedTitle,
                content: encryptedContent,
                category: encryptedCategory
            })

            const idx = notes.value.findIndex(n => n.id === id)
            if (idx !== -1) {
                notes.value[idx] = {
                    ...res.data.data,
                    title,
                    content,
                    category
                }
            }
        } catch (e) {
            error.value = e?.response?.data?.message || 'Failed to update note'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function deleteNote(id) {
        loading.value = true
        error.value = null

        try {
            await notesApi.remove(id)
            notes.value = notes.value.filter(n => n.id !== id)
        } catch (e) {
            error.value = e?.response?.data?.message || 'Failed to delete note'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function getNoteById(id) {
        // First check if we already have it in the store
        let note = notes.value.find(n => n.id === id)
        if (note) return note

        // If not, fetch it (this will be encrypted from the API if called directly,
        // but our fetchNotes decrypts everything. For single fetch, we need decryption too)
        const auth = useAuthStore()
        try {
            const res = await notesApi.getById(id)
            const rawNote = res.data.data
            return {
                ...rawNote,
                title: await decrypt(auth.aesKey, rawNote.title),
                content: await decrypt(auth.aesKey, rawNote.content),
                category: await decrypt(auth.aesKey, rawNote.category)
            }
        } catch (e) {
            console.error('Failed to fetch/decrypt single note:', e)
            throw e
        }
    }

    return {
        notes, loading, error, fetchNotes, addNote, updateNote, deleteNote, getNoteById
    }
})
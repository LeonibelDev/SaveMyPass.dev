<template>
  <div class="flex-1 flex flex-col overflow-hidden bg-[#F7F8FA]">

    <!-- Header -->
    <header class="h-16 border-b border-slate-100 flex items-center px-6 justify-between flex-shrink-0 bg-white z-10">
      <div class="flex items-center flex-1 max-w-xl">
        <h1 class="text-sm font-black text-slate-800 tracking-tight uppercase">
          Secure Notes
        </h1>
      </div>

      <div class="flex items-center gap-2">
        <router-link to="/notes/new"
          class="h-9 px-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-lg shadow-orange-100 flex items-center gap-2 active:scale-95 text-xs uppercase tracking-widest">
          <i class="fa-solid fa-plus text-[12px]"></i>
          New Note
        </router-link>
      </div>
    </header>

    <!-- Main -->
    <main class="flex-1 overflow-y-auto" v-if="notes.length === 0">
      <div class="max-w-4xl mx-auto py-16 px-6">

        <!-- Empty State -->
        <div class="flex flex-col items-center justify-center text-center py-24">

          <!-- Icon -->
          <div
            class="w-20 h-20 bg-white rounded-[1.75rem] border border-slate-100 flex items-center justify-center mb-6">
            <i class="fa-solid fa-note-sticky text-3xl text-slate-200"></i>
          </div>

          <!-- Title -->
          <h2 class="font-black text-slate-800 text-xl tracking-tight mb-2">
            Your Secure Workspace
          </h2>

          <!-- Description -->
          <p class="text-sm text-slate-400 font-medium max-w-[280px] leading-relaxed mb-10">
            Store sensitive documents, recovery keys, or personal thoughts behind your master encryption.
          </p>

          <!-- CTA -->
          <router-link to="/notes/new"
            class="h-9 px-5 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-xl transition-all shadow-lg shadow-orange-100 flex items-center gap-2 text-xs uppercase tracking-widest mb-12">
            <i class="fa-solid fa-plus text-[11px]"></i>
            New Note
          </router-link>

          <!-- Features -->
          <div class="grid sm:grid-cols-2 gap-4 w-full max-w-xl">
            <div class="p-5 rounded-2xl border border-slate-100 bg-white">
              <h3 class="font-bold text-slate-800 text-sm mb-1">
                Zero-Knowledge
              </h3>
              <p class="text-[11px] text-slate-400 leading-relaxed font-medium">
                Notes are encrypted on your device before being uploaded. We can never read them.
              </p>
            </div>

            <div class="p-5 rounded-2xl border border-slate-100 bg-white">
              <h3 class="font-bold text-slate-800 text-sm mb-1">
                Rich Formatting
              </h3>
              <p class="text-[11px] text-slate-400 leading-relaxed font-medium">
                Coming soon: Markdown, attachments, and private sharing.
              </p>
            </div>
          </div>

        </div>

      </div>
    </main>

    <main v-else class="flex-1 overflow-y-auto">
      <div class="max-w-4xl mx-auto py-16 px-6">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="note in notes" :key="note.id" class="bg-white rounded-2xl border border-slate-100 p-5"
            @click="goToDetail(note.id)">
            <h3 class="font-bold text-slate-800 text-sm mb-1">
              {{ note.title }}
            </h3>
            <p class="text-[11px] text-slate-400 leading-relaxed font-medium" v-html="note.content.substring(0, 100)">
            </p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { notesApi } from '@/api/notes'
import { onMounted } from 'vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

let notes = ref([])

function getNotes() {
  notesApi.getAll().then(response => {
    notes.value = response.data.data
  })
}

function goToDetail(id) {
  router.push(`/notes/${id}`)
}

onMounted(() => {
  getNotes()
})
</script>
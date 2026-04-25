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

        <button @click="save" :disabled="!title.trim()"
          class="h-9 px-6 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold rounded-2xl transition-all shadow-lg shadow-orange-100 active:scale-[0.98] disabled:opacity-40 flex items-center gap-2 text-xs uppercase tracking-widest">
          <i class="fa-solid fa-floppy-disk text-[12px]"></i>
          Save note
        </button>
      </div>
    </header>

    <!-- Editor -->
    <main class="flex-1 overflow-y-auto">
      <div class="max-w-4xl mx-auto px-8 py-12">

        <!-- Title input -->
        <input v-model="title" type="text" placeholder="Note title…"
          class="w-full text-4xl font-black text-slate-900 tracking-tighter bg-transparent border-none outline-none placeholder:text-slate-200 mb-8" />

        <!-- Divider -->
        <div class="border-t border-slate-100 mb-8"></div>

        <!-- Body editor -->
        <div ref="bodyRef" contenteditable="true" @input="onInput" @keydown.enter.prevent="insertNewLine"
          data-placeholder="Start writing your secure note…"
          class="w-full min-h-[400px] outline-none text-base text-slate-700 leading-relaxed font-medium empty:before:content-[attr(data-placeholder)] empty:before:text-slate-300">
        </div>

      </div>
    </main>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { notesApi } from '@/api/notes'

const router = useRouter()

const title = ref('')
const body = ref('')
const bodyRef = ref(null)

const goBack = () => router.push('/notes')

const onInput = () => {
  body.value = bodyRef.value.innerHTML
}

const insertNewLine = () => {
  const selection = window.getSelection()
  const range = selection.getRangeAt(0)

  const br = document.createElement('br')
  range.insertNode(br)

  range.setStartAfter(br)
  range.setEndAfter(br)
  selection.removeAllRanges()
  selection.addRange(range)
}

const save = () => {
  notesApi.create({ title: title.value, content: body.value, category: 'note' }).then(() => {
    router.push('/notes')
  })
}
</script>
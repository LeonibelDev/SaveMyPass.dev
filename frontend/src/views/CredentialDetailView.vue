<template>
  <div class="flex-1 flex flex-col bg-[#F7F8FA] overflow-hidden selection:bg-orange-100 selection:text-orange-900">

    <!-- Toolbar -->
    <header class="h-16 border-b border-slate-100 flex items-center px-6 bg-white z-50 flex-shrink-0">
      <div class="max-w-4xl mx-auto w-full flex items-center justify-between">
        <router-link to="/" class="flex items-center gap-2 text-slate-400 hover:text-slate-900 transition-colors group">
          <i class="fa-solid fa-arrow-left text-sm transition-transform group-hover:-translate-x-1"></i>
          <span class="text-[10px] font-black uppercase tracking-[0.2em]">Back to vault</span>
        </router-link>

        <div class="flex items-center gap-2">
          <button @click="showEdit = true"
            class="h-9 px-5 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold text-[10px] rounded-xl transition-all uppercase tracking-widest shadow-lg shadow-orange-100 flex items-center gap-2">
            <i class="fa-solid fa-pen text-[10px]"></i>
            Edit
          </button>
          <button @click="confirmDelete = true"
            class="h-9 w-9 rounded-xl text-slate-300 hover:text-rose-500 hover:bg-rose-50 transition-all flex items-center justify-center border border-slate-100">
            <i class="fa-solid fa-trash-can text-[13px]"></i>
          </button>
        </div>
      </div>
    </header>

    <main class="flex-1 overflow-y-auto py-10 px-6">
      <div class="max-w-2xl mx-auto">

        <!-- Loading -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-40 gap-4">
          <i class="fa-solid fa-circle-notch fa-spin text-slate-300 text-2xl"></i>
          <p class="text-[10px] font-black text-slate-300 uppercase tracking-widest">Decrypting…</p>
        </div>

        <div v-else-if="credential" class="animate-fade-in">

          <!-- Hero card -->
          <div class="bg-white rounded-2xl border border-slate-100 px-8 py-7 mb-4 flex items-center gap-6">
            <div class="w-14 h-14 rounded-2xl bg-slate-50 border border-slate-100 flex items-center justify-center flex-shrink-0 overflow-hidden">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${credential.site}&size=128`"
                :alt="credential.site"
                class="w-8 h-8 object-contain"
                @error="$event.target.style.display='none'"
              />
            </div>
            <div class="flex-1 min-w-0">
              <h1 class="text-2xl font-black text-slate-900 tracking-tighter truncate mb-1.5">{{ credential.site }}</h1>
              <div class="flex items-center gap-3">
                <span class="text-[10px] font-black text-emerald-500 uppercase tracking-[0.2em] flex items-center gap-1.5">
                  <i class="fa-solid fa-circle-check"></i> Encrypted
                </span>
                <span class="text-slate-200 text-xs">|</span>
                <a :href="`https://${credential.site}`" target="_blank"
                  class="text-[10px] font-black text-slate-400 hover:text-brand-orange uppercase tracking-[0.2em] transition-colors">
                  Open site <i class="fa-solid fa-arrow-up-right-from-square ml-0.5 text-[8px]"></i>
                </a>
              </div>
            </div>
          </div>

          <!-- Fields card -->
          <div class="bg-white rounded-2xl border border-slate-100 overflow-hidden mb-4">

            <!-- Username row -->
            <div class="px-8 py-7 border-b border-slate-50 flex items-center justify-between gap-6">
              <div class="flex-1 min-w-0">
                <p class="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em] mb-2 flex items-center gap-2">
                  <i class="fa-solid fa-at text-[9px]"></i> Username or email
                </p>
                <p class="text-lg font-bold text-slate-900 tracking-tight truncate select-all">{{ credential.username }}</p>
              </div>
              <div class="flex items-center gap-1 flex-shrink-0">
                <transition enter-active-class="duration-200 ease-out" enter-from-class="opacity-0 -translate-x-1" enter-to-class="opacity-100 translate-x-0">
                  <span v-if="copyStatus === 'username'" class="text-[10px] font-black text-emerald-500 uppercase tracking-widest mr-2">Copied!</span>
                </transition>
                <button @click="copy(credential.username, 'username')"
                  class="w-9 h-9 rounded-xl text-slate-300 hover:text-brand-orange hover:bg-orange-50 transition-all flex items-center justify-center active:scale-90">
                  <i class="fa-solid fa-copy text-[13px]"></i>
                </button>
              </div>
            </div>

            <!-- Password row -->
            <div class="px-8 py-7 flex items-center justify-between gap-6">
              <div class="flex-1 min-w-0">
                <p class="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em] mb-2 flex items-center gap-2">
                  <i class="fa-solid fa-lock text-[9px]"></i> Password
                </p>
                <p class="text-xl font-bold text-slate-900 tracking-[0.15em] font-mono truncate">
                  {{ visible ? credential.password : '••••••••••••' }}
                </p>
              </div>
              <div class="flex items-center gap-1 flex-shrink-0">
                <transition enter-active-class="duration-200 ease-out" enter-from-class="opacity-0 -translate-x-1" enter-to-class="opacity-100 translate-x-0">
                  <span v-if="copyStatus === 'password'" class="text-[10px] font-black text-emerald-500 uppercase tracking-widest mr-2">Copied!</span>
                </transition>
                <button @click="visible = !visible"
                  class="w-9 h-9 rounded-xl text-slate-300 hover:text-brand-orange hover:bg-orange-50 transition-all flex items-center justify-center active:scale-90">
                  <i :class="['fa-solid', visible ? 'fa-eye-slash' : 'fa-eye']" class="text-[13px]"></i>
                </button>
                <button @click="copy(credential.password, 'password')"
                  class="w-9 h-9 rounded-xl text-slate-300 hover:text-brand-orange hover:bg-orange-50 transition-all flex items-center justify-center active:scale-90">
                  <i class="fa-solid fa-copy text-[13px]"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- Notes card -->
          <div v-if="credential.notes" class="bg-white rounded-2xl border border-slate-100 px-8 py-7 mb-4">
            <p class="text-[10px] font-black text-slate-400 uppercase tracking-[0.25em] mb-3 flex items-center gap-2">
              <i class="fa-solid fa-note-sticky text-[9px]"></i> Private note
            </p>
            <p class="text-sm text-slate-600 font-medium leading-relaxed">{{ credential.notes }}</p>
          </div>

          <!-- Meta -->
          <div class="flex items-center justify-between px-1 pt-2 text-[10px] font-black text-slate-300 uppercase tracking-widest">
            <span>Created: {{ formatDate(credential.createdAt) }}</span>
            <span>Modified: {{ formatDate(credential.updatedAt) }}</span>
          </div>

        </div>
      </div>
    </main>

    <!-- Edit modal -->
    <EditCredentialModal v-if="showEdit" :credential="credential" @close="handleEditClose" />

    <!-- Delete confirm -->
    <transition
      enter-active-class="duration-200 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="duration-150 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0">
      <div v-if="confirmDelete" class="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-slate-900/50 backdrop-blur-sm">
        <div class="bg-white rounded-[2.5rem] p-10 max-w-sm w-full shadow-2xl shadow-slate-200 border border-slate-100 text-center">
          <div class="w-16 h-16 bg-rose-50 border border-rose-100 rounded-2xl flex items-center justify-center mx-auto mb-6">
            <i class="fa-solid fa-triangle-exclamation text-rose-400 text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900 tracking-tighter mb-2">Delete entry?</h3>
          <p class="text-slate-400 text-sm font-medium leading-relaxed mb-8">
            This credential will be permanently removed from your encrypted vault. This action cannot be undone.
          </p>
          <div class="flex gap-3">
            <button @click="confirmDelete = false"
              class="flex-1 py-3.5 bg-slate-100 hover:bg-slate-200 rounded-xl font-bold text-slate-700 transition-all text-[10px] uppercase tracking-widest">
              Keep it
            </button>
            <button @click="handleDelete"
              class="flex-1 py-3.5 bg-rose-500 hover:bg-rose-600 text-white rounded-xl font-bold transition-all shadow-lg shadow-rose-100 text-[10px] uppercase tracking-widest">
              Delete
            </button>
          </div>
        </div>
      </div>
    </transition>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { credentialsApi } from '@/api/credentials'
import { useVaultStore } from '@/stores/vault'
import EditCredentialModal from '@/components/EditCredentialModal.vue'

const route = useRoute()
const router = useRouter()
const vault = useVaultStore()

const credential = ref(null)
const loading = ref(true)
const visible = ref(false)
const copyStatus = ref(null)
const showEdit = ref(false)
const confirmDelete = ref(false)

async function fetchDetails() {
  loading.value = true
  try {
    const res = await credentialsApi.get(route.params.id)
    const rawData = res.data.data
    const plain = await vault.decryptPassword(rawData.password)
    credential.value = { ...rawData, password: plain }

  } catch (err) {
    router.push('/')

    console.error('Fetch error')
  } finally {
    loading.value = false
  }
}

async function handleDelete() {
  try {
    await vault.removeCredential(credential.value.id)
    router.push('/')
  } catch (err) {
    console.error('Delete error')
  }
}

function handleEditClose() {
  showEdit.value = false
  fetchDetails()
}

function copy(text, type) {
  navigator.clipboard.writeText(text)
  copyStatus.value = type
  setTimeout(() => copyStatus.value = null, 3000)
}

function formatDate(dateStr) {
  if (!dateStr) return 'N/A'
  return new Date(dateStr).toLocaleDateString(undefined, { 
    month: 'short', 
    day: 'numeric', 
    year: 'numeric'
  })
}

onMounted(fetchDetails)
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.4s cubic-bezier(0.16, 1, 0.3, 1);
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>

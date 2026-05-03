<template>
  <div class="flex-1 flex flex-col bg-[#F2F2F7] overflow-hidden selection:bg-brand-orange/20">

    <!-- Toolbar -->
    <header
      class="h-16 border-b border-slate-200/60 flex items-center px-4 sm:px-6 bg-white/80 backdrop-blur-xl z-20 flex-shrink-0">
      <div class="max-w-4xl mx-auto w-full flex items-center justify-between">
        <router-link to="/" class="flex items-center gap-2 text-brand-orange hover:opacity-70 transition-opacity group">
          <i class="fa-solid fa-chevron-left text-sm"></i>
          <span class="text-[17px] font-medium">Vault</span>
        </router-link>

        <div class="flex items-center gap-2">
          <button @click="showEdit = true"
            class="h-9 px-4 text-brand-orange font-semibold text-[17px] hover:opacity-70 transition-opacity">
            Edit
          </button>
          <button @click="confirmDelete = true"
            class="w-9 h-9 rounded-full text-slate-300 hover:text-rose-500 hover:bg-rose-50 transition-all flex items-center justify-center">
            <i class="fa-solid fa-trash-can text-[13px]"></i>
          </button>
        </div>
      </div>
    </header>

    <main class="flex-1 overflow-y-auto py-6 sm:py-8 px-4 sm:px-6 no-scrollbar">
      <div class="max-w-2xl mx-auto">

        <!-- Loading -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-40">
          <div class="w-8 h-8 border-[3px] border-slate-200 border-t-brand-orange rounded-full animate-spin"></div>
        </div>

        <div v-else-if="credential" class="animate-fade-in">

          <!-- Hero Section -->
          <div class="flex flex-col items-center gap-4 mb-10 text-center">
            <div
              class="w-20 h-20 rounded-[22px] bg-white border border-slate-200/60 flex items-center justify-center shadow-sm overflow-hidden">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${credential.site}&size=128`"
                :alt="credential.site" class="w-12 h-12 object-contain" @error="$event.target.style.display = 'none'" />
            </div>
            <div class="min-w-0">
              <h1 class="text-[28px] font-bold text-slate-900 tracking-tight leading-tight mb-1">
                {{ credential.site }}
              </h1>
              <div class="flex items-center justify-center gap-2">
                <span class="text-[13px] font-semibold text-emerald-500 flex items-center gap-1.5">
                  <i class="fa-solid fa-lock text-[10px]"></i> Securely Encrypted
                </span>
                <span class="text-slate-300">•</span>
                <a :href="`https://${credential.site}`" target="_blank"
                  class="text-[13px] font-semibold text-brand-orange hover:opacity-70 transition-opacity">
                  Open Site
                </a>
              </div>
            </div>
          </div>

          <!-- Fields Group -->
          <div
            class="bg-white rounded-[20px] border border-slate-200/60 overflow-hidden mb-6 shadow-sm divide-y divide-slate-100">

            <!-- Username Row -->
            <div
              class="px-5 py-4 flex items-center justify-between gap-4 hover:bg-slate-50 transition-colors active:bg-slate-100">
              <div class="flex-1 min-w-0">
                <p class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1">
                  Username
                </p>
                <p class="text-[17px] font-medium text-slate-900 truncate select-all">
                  {{ credential.username }}
                </p>
              </div>
              <button @click="copy(credential.username, 'username')"
                class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 text-brand-orange flex items-center justify-center active:scale-90 transition-all">
                <i v-if="copyStatus === 'username'" class="fa-solid fa-check"></i>
                <i v-else class="fa-solid fa-copy text-[14px]"></i>
              </button>
            </div>

            <!-- Password Row -->
            <div
              class="px-5 py-4 flex items-center justify-between gap-4 hover:bg-slate-50 transition-colors active:bg-slate-100">
              <div class="flex-1 min-w-0">
                <p class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-1">
                  Password
                </p>
                <p class="text-[20px] font-bold text-slate-900 tracking-[0.2em] font-mono truncate">
                  {{ visible ? credential.password : '••••••••••••' }}
                </p>
              </div>
              <div class="flex items-center gap-2">
                <button @click="visible = !visible"
                  class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 text-slate-400 flex items-center justify-center active:scale-90 transition-all">
                  <i :class="['fa-solid', visible ? 'fa-eye-slash' : 'fa-eye']" class="text-[14px]"></i>
                </button>
                <button @click="copy(credential.password, 'password')"
                  class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 text-brand-orange flex items-center justify-center active:scale-90 transition-all">
                  <i v-if="copyStatus === 'password'" class="fa-solid fa-check"></i>
                  <i v-else class="fa-solid fa-copy text-[14px]"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- Notes Group -->
          <div v-if="credential.notes"
            class="bg-white rounded-[20px] border border-slate-200/60 px-5 py-4 mb-6 shadow-sm">
            <p class="text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2">
              Private Note
            </p>
            <p class="text-[15px] text-slate-700 leading-relaxed">{{ credential.notes }}</p>
          </div>

          <!-- Meta Info -->
          <div class="flex flex-col items-center gap-1.5 px-1 py-4 text-[12px] font-medium text-slate-400 text-center">
            <p>Created {{ formatDate(credential.createdAt) }}</p>
            <p>Last modified {{ formatDate(credential.updatedAt) }}</p>
          </div>

        </div>
      </div>
    </main>

    <!-- Modals -->
    <EditCredentialModal v-if="showEdit" :credential="credential" @close="handleEditClose" />

    <!-- Delete Alert -->
    <transition name="fade">
      <div v-if="confirmDelete"
        class="fixed inset-0 z-[100] flex items-center justify-center p-6 bg-black/30 backdrop-blur-sm">
        <div
          class="bg-white/90 backdrop-blur-xl rounded-[24px] p-8 max-w-sm w-full shadow-2xl text-center border border-white/20">
          <h3 class="text-[19px] font-bold text-slate-900 mb-2">Delete Credential?</h3>
          <p class="text-[14px] text-slate-500 font-medium leading-relaxed mb-8">
            This action cannot be undone. Your encrypted data will be removed.
          </p>
          <div class="flex flex-col gap-2">
            <button @click="handleDelete"
              class="w-full py-3.5 bg-rose-500 text-white rounded-xl font-bold text-[16px] active:opacity-70 transition-all">
              Delete
            </button>
            <button @click="confirmDelete = false"
              class="w-full py-3.5 text-brand-orange font-semibold text-[16px] active:opacity-70 transition-all">
              Cancel
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
    credential.value = {
      ...rawData,
      site: await vault.decryptField(rawData.site),
      password: await vault.decryptField(rawData.password),
      username: await vault.decryptField(rawData.username)
    }

  } catch (err) {
    console.error('Fetch error:', err)
    error.value = true
    router.push('/')
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

async function copy(text, type) {
  try {
    await navigator.clipboard.writeText(text)
    copyStatus.value = type

    setTimeout(() => {
      copyStatus.value = null
    }, 3000)

    // clean clipboard after 6 seconds
    setTimeout(async () => {
      try {
        const current = await navigator.clipboard.readText()

        if (current === text) {
          await navigator.clipboard.writeText('')
        }
      } catch (err) {
        console.warn('Error cleaning clipboard:', err)
      }
    }, 6000)

  } catch (err) {
    console.error('Error copying to clipboard:', err)
  }
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
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

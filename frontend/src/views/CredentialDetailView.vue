<template>
  <div class="view-root">

    <!-- Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar island-toolbar--titled">
        <div class="flex items-center gap-3">
          <router-link to="/" class="btn-back">
            <i class="fa-solid fa-chevron-left text-sm"></i>
            <span>Vault</span>
          </router-link>
        </div>
        <div class="toolbar-actions">
          <button @click="showEdit = true" class="btn-ghost" style="color: #f97316; border-color: rgba(249,115,22,0.2);">
            <i class="fa-solid fa-pen-to-square text-sm"></i>
            Edit
          </button>
          <button @click="confirmDelete = true" class="btn-danger">
            <i class="fa-solid fa-trash-can text-sm"></i>
          </button>
        </div>
      </header>
    </div>

    <!-- Main -->
    <main class="view-main">
      <div class="content-container">

        <!-- Loading -->
        <div v-if="loading" class="flex items-center justify-center py-40">
          <div class="loading-spinner"></div>
        </div>

        <div v-else-if="credential" class="space-y-4 max-w-2xl mx-auto" style="animation: fadeUp 0.3s ease forwards;">

          <!-- Hero -->
          <div class="flex flex-col items-center gap-3 py-8 text-center">
            <div class="hero-favicon">
              <img
                :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${credential.site}&size=128`"
                :alt="credential.site"
                class="w-12 h-12 object-contain"
                @error="$event.target.style.display = 'none'"
              />
            </div>
            <div>
              <h1 class="hero-title">{{ credential.site }}</h1>
              <div class="hero-badges">
                <span class="badge-encrypted">
                  <i class="fa-solid fa-lock text-[9px]"></i> Encrypted
                </span>
                <span class="badge-dot">·</span>
                <a :href="`https://${credential.site}`" target="_blank" class="badge-link">
                  Open Site <i class="fa-solid fa-arrow-up-right-from-square text-[10px]"></i>
                </a>
              </div>
            </div>
          </div>

          <!-- Fields island -->
          <div class="island-card">
            <!-- Username -->
            <div class="info-row">
              <div class="flex-1 min-w-0">
                <p class="info-label">Username</p>
                <p class="info-value">{{ credential.username }}</p>
              </div>
              <button @click="copy(credential.username, 'username')" class="icon-btn icon-btn--orange">
                <i v-if="copyStatus === 'username'" class="fa-solid fa-circle-check"></i>
                <i v-else class="fa-solid fa-copy"></i>
              </button>
            </div>

            <!-- Password -->
            <div class="info-row">
              <div class="flex-1 min-w-0">
                <p class="info-label">Password</p>
                <p class="info-value info-value--mono" style="font-size:20px; letter-spacing: 0.18em;">
                  {{ visible ? credential.password : '••••••••••••' }}
                </p>
              </div>
              <div class="flex items-center gap-2">
                <button @click="visible = !visible" class="icon-btn">
                  <i :class="['fa-solid', visible ? 'fa-eye-slash' : 'fa-eye']"></i>
                </button>
                <button @click="copy(credential.password, 'password')" class="icon-btn icon-btn--orange">
                  <i v-if="copyStatus === 'password'" class="fa-solid fa-circle-check"></i>
                  <i v-else class="fa-solid fa-copy"></i>
                </button>
              </div>
            </div>
          </div>

          <!-- Notes island -->
          <div v-if="credential.notes" class="island-card island-card--padded">
            <p class="info-label">Private Note</p>
            <p style="font-size: 14px; color: #94a3b8; font-weight: 500; line-height: 1.6; margin-top: 6px;">
              {{ credential.notes }}
            </p>
          </div>

          <!-- Meta -->
          <div class="meta-row">
            <span>Created {{ formatDate(credential.createdAt) }}</span>
            <span>·</span>
            <span>Modified {{ formatDate(credential.updatedAt) }}</span>
          </div>

        </div>
      </div>
    </main>

    <!-- Modals -->
    <EditCredentialModal v-if="showEdit" :credential="credential" @close="handleEditClose" />

    <!-- Floating island delete dialog (no backdrop!) -->
    <Transition name="dialog-slide">
      <div v-if="confirmDelete" class="island-dialog-wrap">
        <div class="island-dialog">
          <div class="dialog-icon">
            <i class="fa-solid fa-trash-can"></i>
          </div>
          <h3 class="dialog-title">Delete Credential?</h3>
          <p class="dialog-desc">This action cannot be undone. Your encrypted data will be permanently removed.</p>
          <div class="dialog-actions">
            <button @click="confirmDelete = false" class="dialog-btn-cancel">Cancel</button>
            <button @click="handleDelete" class="dialog-btn-delete">Delete</button>
          </div>
        </div>
      </div>
    </Transition>

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
const error = ref(false)

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
    setTimeout(() => { copyStatus.value = null }, 3000)
    setTimeout(async () => {
      try {
        const current = await navigator.clipboard.readText()
        if (current === text) await navigator.clipboard.writeText('')
      } catch (err) {}
    }, 6000)
  } catch (err) {}
}

function formatDate(dateStr) {
  if (!dateStr) return 'N/A'
  return new Date(dateStr).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' })
}

onMounted(fetchDetails)
</script>

<style scoped>
@import '@/assets/island-theme.css';

.hero-favicon {
  width: 72px;
  height: 72px;
  border-radius: 20px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-title {
  font-family: 'Outfit', sans-serif;
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
  letter-spacing: -0.03em;
  margin-bottom: 8px;
}

.hero-badges {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.badge-encrypted {
  font-size: 11px;
  font-weight: 700;
  color: #4ade80;
  background: rgba(34, 197, 94, 0.1);
  border: 1px solid rgba(34, 197, 94, 0.2);
  padding: 2px 8px;
  border-radius: 20px;
  font-family: 'Outfit', sans-serif;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  display: flex;
  align-items: center;
  gap: 4px;
}

.badge-dot { color: #94a3b8; font-size: 14px; }

.badge-link {
  font-size: 12px;
  font-weight: 700;
  color: #f97316;
  font-family: 'Outfit', sans-serif;
  text-decoration: none;
  transition: opacity 0.15s;
  display: flex;
  align-items: center;
  gap: 4px;
}
.badge-link:hover { opacity: 0.7; }

.meta-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 11px;
  font-weight: 600;
  color: #94a3b8;
  font-family: 'Outfit', sans-serif;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  padding: 8px 0;
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(10px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>

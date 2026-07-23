<template>
  <div class="view-root">

    <!-- Island Toolbar -->
    <div class="island-toolbar-wrap">
      <header class="island-toolbar island-toolbar--titled">
        <h1 class="toolbar-title">Settings</h1>
        <div class="account-avatar-mini">{{ initials }}</div>
      </header>
    </div>

    <!-- Main -->
    <main class="view-main">
      <div class="content-container" style="max-width: 680px;">

        <!-- Status alerts -->
        <Transition name="dialog-slide">
          <div v-if="error || success" class="mb-3">
            <div v-if="error" class="island-alert island-alert--error">
              <i class="fa-solid fa-triangle-exclamation"></i> {{ error }}
            </div>
            <div v-if="success" class="island-alert island-alert--success">
              <i class="fa-solid fa-circle-check"></i> {{ success }}
            </div>
          </div>
        </Transition>

        <!-- Account section -->
        <p class="section-group-label">Account</p>
        <div class="island-card" style="margin-bottom: 14px;">
          <div class="info-row">
            <div class="flex items-center gap-3">
              <div class="info-icon info-icon--blue"><i class="fa-solid fa-user"></i></div>
              <div>
                <p class="info-label">Name</p>
                <p class="info-value">{{ auth.name }}</p>
              </div>
            </div>
          </div>
          <div class="info-row">
            <div class="flex items-center gap-3">
              <div class="info-icon info-icon--emerald"><i class="fa-solid fa-envelope text-sm"></i></div>
              <div>
                <p class="info-label">Email</p>
                <p class="info-value">{{ auth.email }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Security section -->
        <p class="section-group-label">Security &amp; Master Password</p>
        <div class="island-card island-card--padded" style="margin-bottom: 14px;">
          <form @submit.prevent="handleChangePassword" class="form-group">
            <div class="form-field">
              <label class="island-input-label">Current Password</label>
              <input v-model="form.oldPassword" type="password" required placeholder="••••••••" class="island-input" />
            </div>
            <div class="form-row-2">
              <div class="form-field">
                <label class="island-input-label">New Password</label>
                <input v-model="form.newPassword" type="password" required placeholder="••••••••" class="island-input" />
              </div>
              <div class="form-field">
                <label class="island-input-label">Confirm New</label>
                <input v-model="form.confirmNewPassword" type="password" required placeholder="••••••••" class="island-input" />
              </div>
            </div>
            <button type="submit" :disabled="loading" class="btn-primary" style="width:100%; justify-content:center; height:44px; font-size:14px;">
              <i v-if="loading" class="fa-solid fa-circle-notch fa-spin"></i>
              <i v-else class="fa-solid fa-key"></i>
              {{ loading ? 'Updating…' : 'Change Master Password' }}
            </button>
            <p class="form-hint">Changing your password will re-encrypt your entire vault. This may take a moment.</p>
          </form>
        </div>

        <!-- Privacy & Help section -->
        <p class="section-group-label">Privacy &amp; Help</p>
        <div class="island-card" style="margin-bottom: 14px;">
          <a
            href="https://savemypass.dev/security"
            target="_blank"
            class="info-row"
            style="text-decoration:none; cursor:pointer;"
          >
            <div class="flex items-center gap-3 flex-1">
              <div class="info-icon info-icon--dark"><i class="fa-solid fa-shield"></i></div>
              <span class="info-value">Security Whitepaper</span>
            </div>
            <i class="fa-solid fa-chevron-right" style="color:#334155; font-size:11px;"></i>
          </a>
          <div class="info-row">
            <div class="flex items-center gap-3 flex-1">
              <div class="info-icon" style="background: rgba(255,255,255,0.05); border-color: rgba(255,255,255,0.07); color: #64748b;">
                <i class="fa-solid fa-code-branch"></i>
              </div>
              <span class="info-value">Version</span>
            </div>
            <span style="font-size:13px; font-weight:700; color:#475569; font-family:'Outfit',sans-serif;">1.2.0-stable</span>
          </div>
        </div>

        <div style="text-align:center; padding: 8px 0 24px;">
          <p style="font-size:10px; font-weight:700; color:#334155; letter-spacing:0.18em; text-transform:uppercase; font-family:'Outfit',sans-serif;">
            SaveMyPass Zero-Knowledge Architecture
          </p>
        </div>

      </div>
    </main>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useVaultStore } from '@/stores/vault'

const auth = useAuthStore()
const vault = useVaultStore()
const form = reactive({ oldPassword: '', newPassword: '', confirmNewPassword: '' })
const loading = ref(false)
const error = ref(null)
const success = ref(null)

const initials = computed(() => {
  if (!auth.name) return '??'
  return auth.name.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()
})

async function handleChangePassword() {
  if (form.newPassword !== form.confirmNewPassword) {
    error.value = 'Passwords do not match.'
    return
  }
  if (form.newPassword.length < 8) {
    error.value = 'New password must be at least 8 characters.'
    return
  }
  loading.value = true
  error.value = null
  success.value = null
  try {
    await vault.rotateVault(form.newPassword)
    success.value = 'Vault re-encrypted and password updated successfully.'
    form.oldPassword = ''
    form.newPassword = ''
    form.confirmNewPassword = ''
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || 'Failed to update password.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
@import '@/assets/island-theme.css';

.account-avatar-mini {
  width: 32px;
  height: 32px;
  border-radius: 9px;
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(255,255,255,0.09);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  font-size: 11px;
  font-weight: 700;
  font-family: 'Outfit', sans-serif;
}

.info-icon {
  width: 34px;
  height: 34px;
  border-radius: 9px;
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.07);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  flex-shrink: 0;
}
.info-icon--blue    { background: rgba(59,130,246,0.1); border-color: rgba(59,130,246,0.15); color: #60a5fa; }
.info-icon--emerald { background: rgba(34,197,94,0.1);  border-color: rgba(34,197,94,0.15);  color: #4ade80; }
.info-icon--dark    { background: rgba(255,255,255,0.06); border-color: rgba(255,255,255,0.09); color: #94a3b8; }

.form-group { display: flex; flex-direction: column; gap: 14px; }

.form-field { display: flex; flex-direction: column; }

.form-row-2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

@media (max-width: 520px) {
  .form-row-2 { grid-template-columns: 1fr; }
}

.form-hint {
  font-size: 12px;
  color: #475569;
  font-weight: 500;
  text-align: center;
  line-height: 1.5;
  padding: 0 8px;
}
</style>
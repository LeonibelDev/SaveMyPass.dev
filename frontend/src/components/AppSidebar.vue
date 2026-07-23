<template>
  <!-- Mobile backdrop -->
  <Transition name="backdrop-fade">
    <div
      v-if="ui.isSidebarOpen"
      @click="ui.closeSidebar"
      class="lg:hidden fixed inset-0 bg-black/15 backdrop-blur-[2px] z-40"
    />
  </Transition>

  <!-- Island Sidebar -->
  <aside
    class="sidebar-island fixed inset-y-0 left-0 z-50 flex flex-col transition-all duration-300 ease-out lg:static lg:translate-x-0"
    :class="ui.isSidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'"
  >
    <div class="island-wrapper flex flex-col h-full">

      <!-- Logo / Header -->
      <div class="island-header">
        <div class="logo-icon">
          <i class="fa-solid fa-folder-open"></i>
        </div>
        <div class="logo-text">
          SaveMyPass<span class="logo-accent">.dev</span>
        </div>
        <button class="close-btn lg:hidden" @click="ui.closeSidebar">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <div class="island-divider"></div>

      <!-- Navigation -->
      <nav class="island-nav">
        <template v-for="item in menuItems" :key="item.name">
          <p v-if="item.header" class="nav-section-label">{{ item.header }}</p>

          <router-link
            :to="item.to"
            @click="onNavItemClick"
            class="nav-item"
            :class="route.path.startsWith(item.to) ? 'nav-item--active' : 'nav-item--idle'"
          >
            <div class="nav-icon-wrap">
              <i :class="item.icon"></i>
            </div>
            <span class="nav-label">{{ item.displayName }}</span>
            <span
              v-if="item.name === 'vault'"
              class="nav-badge"
              :class="route.path.startsWith(item.to) ? 'nav-badge--active' : ''"
            >
              {{ vault.credentials.length }}
            </span>
          </router-link>
        </template>
      </nav>

      <div class="flex-1"></div>
      <div class="island-divider"></div>

      <!-- Account -->
      <div class="island-footer">
        <div class="account-row">
          <div class="account-avatar">{{ initials }}</div>
          <div class="account-info">
            <p class="account-name">{{ auth.name || 'Account' }}</p>
            <button class="account-logout" @click="handleLogout">
              <i class="fa-solid fa-right-from-bracket text-[10px]"></i>
              Sign out
            </button>
          </div>
          <div class="account-status-dot"></div>
        </div>
      </div>

    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useVaultStore } from '@/stores/vault'
import { useUiStore } from '@/stores/ui'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const vault = useVaultStore()
const ui = useUiStore()

const initials = computed(() => {
  if (!auth.name) return '??'
  return auth.name.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()
})

const menuItems = [
  { name: 'vault',   displayName: 'Vault',    to: '/vault',   icon: 'fa-solid fa-folder-closed', header: 'Main' },
  { name: 'notes',   displayName: 'Notes',    to: '/notes',   icon: 'fa-solid fa-file-lines' },
  { name: 'export',  displayName: 'Export',   to: '/export',  icon: 'fa-solid fa-share-from-square', header: 'Management' },
  { name: 'profile', displayName: 'Settings', to: '/profile', icon: 'fa-solid fa-circle-user' },
]

function onNavItemClick() {
  if (window.innerWidth < 1024) ui.closeSidebar()
}

function handleLogout() {
  auth.logout()
  router.push('/login')
  ui.closeSidebar()
}
</script>

<style scoped>
/* ─── Island container ───────────────────────────────────── */
.sidebar-island {
  width: 260px;
  padding: 16px 12px 16px 16px;
  background: transparent;
}

.island-wrapper {
  background: rgba(255, 255, 255, 0.88);
  border: 1px solid rgba(0, 0, 0, 0.07);
  border-radius: 16px;
  box-shadow:
    0 4px 24px rgba(0, 0, 0, 0.08),
    0 1px 6px rgba(0, 0, 0, 0.05),
    inset 0 1px 0 rgba(255, 255, 255, 1);
  overflow: hidden;
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

/* ─── Header ─────────────────────────────────────────────── */
.island-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 14px 14px;
}

.logo-icon {
  width: 30px;
  height: 30px;
  background: linear-gradient(135deg, #f97316, #ea580c);
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.28);
}

.logo-text {
  font-family: 'Outfit', sans-serif;
  font-size: 14px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
  flex: 1;
}

.logo-accent { color: #f97316; }

.close-btn {
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
  border-radius: 8px;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: color 0.15s, background 0.15s;
}
.close-btn:hover { color: #0f172a; background: #f1f5f9; }

/* ─── Divider ─────────────────────────────────────────────── */
.island-divider {
  height: 1px;
  background: linear-gradient(to right, transparent, rgba(0,0,0,0.06) 20%, rgba(0,0,0,0.06) 80%, transparent);
  margin: 0 14px;
}

/* ─── Navigation ─────────────────────────────────────────── */
.island-nav {
  padding: 10px 8px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-section-label {
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #94a3b8;
  padding: 10px 8px 4px;
  font-family: 'Outfit', sans-serif;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 9px;
  padding: 8px 10px;
  border-radius: 9px;
  font-size: 13.5px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.15s ease;
  cursor: pointer;
  position: relative;
}

.nav-item--idle { color: #64748b; }
.nav-item--idle:hover { color: #0f172a; background: rgba(0, 0, 0, 0.04); }
.nav-item--idle:hover .nav-icon-wrap { color: #64748b; }

.nav-item--active {
  color: #ea580c;
  background: rgba(249, 115, 22, 0.08);
  box-shadow: inset 0 0 0 1px rgba(249, 115, 22, 0.15);
}

.nav-item--active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 60%;
  background: linear-gradient(to bottom, #f97316, #ea580c);
  border-radius: 0 3px 3px 0;
}

.nav-icon-wrap {
  width: 26px;
  height: 26px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  flex-shrink: 0;
  transition: color 0.15s;
}

.nav-item--idle .nav-icon-wrap { color: #94a3b8; }
.nav-item--active .nav-icon-wrap { color: #f97316; }

.nav-label { flex: 1; letter-spacing: -0.01em; }

.nav-badge {
  font-size: 10px;
  font-weight: 700;
  padding: 1px 6px;
  border-radius: 6px;
  background: #f1f5f9;
  color: #94a3b8;
  border: 1px solid #e2e8f0;
}

.nav-badge--active {
  background: rgba(249, 115, 22, 0.1);
  color: #f97316;
  border-color: rgba(249, 115, 22, 0.2);
}

/* ─── Footer / Account ───────────────────────────────────── */
.island-footer { padding: 12px 8px 14px; }

.account-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 10px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  transition: background 0.15s;
}
.account-row:hover { background: #f1f5f9; }

.account-avatar {
  width: 30px;
  height: 30px;
  border-radius: 8px;
  background: linear-gradient(135deg, #e2e8f0, #cbd5e1);
  border: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #64748b;
  font-size: 11px;
  font-weight: 700;
  flex-shrink: 0;
}

.account-info { flex: 1; min-width: 0; }

.account-name {
  font-size: 13px;
  font-weight: 600;
  color: #0f172a;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.2;
}

.account-logout {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  font-weight: 500;
  color: #94a3b8;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  margin-top: 2px;
  transition: color 0.15s;
}
.account-logout:hover { color: #ef4444; }

.account-status-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 6px rgba(34, 197, 94, 0.4);
  flex-shrink: 0;
}

/* ─── Backdrop ───────────────────────────────────────────── */
.backdrop-fade-enter-active,
.backdrop-fade-leave-active { transition: opacity 0.25s ease; }
.backdrop-fade-enter-from,
.backdrop-fade-leave-to { opacity: 0; }
</style>

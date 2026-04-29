<template>
  <aside
    class="fixed inset-y-0 left-0 z-50 w-72 bg-white/90 backdrop-blur-2xl border-r border-slate-200/50 flex flex-col h-screen transition-all duration-500 lg:static lg:translate-x-0"
    :class="ui.isSidebarOpen ? 'translate-x-0 shadow-2xl' : '-translate-x-full lg:translate-x-0 shadow-none'">

    <!-- Header / Logo -->
    <div class="h-16 px-6 flex items-center justify-between flex-shrink-0">
      <div class="flex items-center gap-3">
        <div
          class="w-8 h-8 bg-brand-orange rounded-[9px] flex items-center justify-center text-white shadow-sm flex-shrink-0">
          <i class="fa-solid fa-key text-[13px]"></i>
        </div>
        <span class="text-[17px] font-bold text-slate-900 tracking-tight">SaveMyPass</span>
      </div>

      <button @click="ui.closeSidebar"
        class="lg:hidden w-8 h-8 flex items-center justify-center text-slate-400 active:scale-90 transition-transform">
        <i class="fa-solid fa-xmark"></i>
      </button>
    </div>

    <!-- Navigation -->
    <nav class="flex-1 overflow-y-auto px-4 py-4 space-y-1 no-scrollbar">
      <div v-for="item in menuItems" :key="item.name">
        <p v-if="item.header" class="text-[11px] font-bold text-slate-400 uppercase tracking-wider px-3 py-3 mt-4 mb-1">
          {{ item.header }}
        </p>

        <router-link :to="item.to" @click="onNavItemClick"
          class="flex items-center gap-3 px-3 py-2 rounded-xl text-[15px] font-medium transition-all duration-200 group active:scale-[0.97]"
          :class="route.path.startsWith(item.to)
            ? 'bg-brand-orange text-white shadow-md shadow-orange-200/40'
            : 'text-slate-600 hover:bg-slate-100/80'">

          <div class="w-7 h-7 flex items-center justify-center rounded-lg transition-colors"
            :class="route.path.startsWith(item.to) ? 'text-white' : 'text-slate-400 group-hover:text-slate-600'">
            <i class="fa-solid text-[14px]" :class="item.icon"></i>
          </div>

          <span class="flex-1 truncate">{{ item.displayName }}</span>

          <span v-if="item.name === 'vault'" class="text-[12px] font-semibold px-2 py-0.5 rounded-lg transition-colors"
            :class="route.path.startsWith(item.to) ? 'text-white/80' : 'text-slate-300'">
            {{ vault.credentials.length }}
          </span>
        </router-link>
      </div>
    </nav>

    <!-- Account Section -->
    <div class="p-4 border-t border-slate-100/50 bg-white/50 backdrop-blur-xl flex-shrink-0">
      <div class="flex items-center gap-3 p-3 rounded-2xl hover:bg-slate-100/80 transition-all cursor-default group">
        <div
          class="w-10 h-10 bg-slate-900 rounded-2xl flex items-center justify-center text-white font-bold text-[13px] flex-shrink-0 shadow-sm group-hover:scale-105 transition-transform">
          {{ initials }}
        </div>
        <div class="flex-1 min-w-0">
          <p class="text-[15px] font-bold text-slate-900 truncate leading-tight">
            {{ auth.name || 'Account' }}
          </p>
          <button @click="handleLogout"
            class="text-[13px] font-medium text-slate-400 hover:text-rose-500 transition-colors flex items-center gap-1 mt-0.5">
            <i class="fa-solid fa-arrow-right-from-bracket text-[11px]"></i>
            Sign out
          </button>
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
  { name: 'vault', displayName: 'Vault', to: '/vault', icon: 'fa-vault', header: 'Main' },
  { name: 'notes', displayName: 'Notes', to: '/notes', icon: 'fa-note-sticky' },
  { name: 'export', displayName: 'Export', to: '/export', icon: 'fa-file-export', header: 'Management' },
  { name: 'profile', displayName: 'Settings', to: '/profile', icon: 'fa-sliders' },
]

function onNavItemClick() {
  if (window.innerWidth < 1024) {
    ui.closeSidebar()
  }
}

function handleLogout() {
  auth.logout()
  router.push('/login')
  ui.closeSidebar()
}
</script>

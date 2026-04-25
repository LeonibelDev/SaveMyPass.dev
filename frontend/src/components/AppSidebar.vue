<template>
  <aside class="w-60 bg-white border-r border-slate-100 flex flex-col h-screen flex-shrink-0">

    <!-- Logo -->
    <div class="h-16 px-5 flex items-center gap-3 border-b border-slate-100 flex-shrink-0">
      <div class="w-8 h-8 bg-brand-orange rounded-xl flex items-center justify-center text-white shadow-lg shadow-orange-200 flex-shrink-0">
        <i class="fa-solid fa-key text-[13px]"></i>
      </div>
      <span class="text-sm font-black text-slate-900 tracking-tight">SaveMyPass</span>
    </div>

    <!-- Nav -->
    <nav class="flex-1 overflow-y-auto p-3 space-y-0.5">
      <p class="text-[10px] font-black text-slate-300 uppercase tracking-[0.25em] px-3 py-3">Menu</p>

      <router-link
        v-for="item in menuItems"
        :key="item.name"
        :to="item.to"
        class="flex items-center gap-3 px-3 py-2.5 rounded-xl text-[13px] font-bold transition-all duration-150 group relative"
        :class="route.path === item.to
          ? 'bg-orange-50 text-brand-orange'
          : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'"
      >
        <i
          class="w-4 text-center text-[13px] fa-solid transition-colors flex-shrink-0"
          :class="[item.icon, route.path === item.to ? 'text-brand-orange' : 'text-slate-300 group-hover:text-slate-500']"
        ></i>

        <span class="flex-1 truncate">{{ item.displayName }}</span>

        <span
          v-if="item.name === 'vault'"
          class="text-[10px] font-black px-2 py-0.5 rounded-lg"
          :class="route.path === item.to ? 'bg-orange-100 text-brand-orange' : 'bg-slate-100 text-slate-400'"
        >
          {{ vault.credentials.length }}
        </span>
      </router-link>
    </nav>

    <!-- User -->
    <div class="p-3 border-t border-slate-100 flex-shrink-0">
      <div class="flex items-center gap-3 px-3 py-2.5 rounded-xl hover:bg-slate-50 transition-colors cursor-default group">
        <div class="w-8 h-8 bg-slate-900 rounded-xl flex items-center justify-center text-white font-black text-[11px] flex-shrink-0">
          {{ initials }}
        </div>
        <div class="flex-1 min-w-0">
          <p class="text-[13px] font-bold text-slate-800 truncate leading-tight">{{ auth.name || 'Account' }}</p>
          <button
            @click="handleLogout"
            class="text-[10px] font-black text-slate-400 hover:text-rose-500 transition-colors uppercase tracking-widest flex items-center gap-1 mt-0.5"
          >
            <i class="fa-solid fa-arrow-right-from-bracket text-[9px]"></i>
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

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const vault = useVaultStore()

const initials = computed(() => {
  if (!auth.name) return '??'
  return auth.name.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()
})

const menuItems = [
  { name: 'vault',   displayName: 'Vault',        to: '/vault',   icon: 'fa-vault' },
  { name: 'notes',   displayName: 'Secure notes',  to: '/notes',   icon: 'fa-note-sticky' },
  { name: 'export',  displayName: 'Data export',   to: '/export',  icon: 'fa-file-export' },
  { name: 'profile', displayName: 'Settings',      to: '/profile', icon: 'fa-sliders' },
]

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>
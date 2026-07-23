<template>
  <div :class="isAppLayout ? 'app-root' : 'public-root'">
    <!-- Mobile Header (for authenticated views) -->
    <header v-if="showMobileHeader"
      class="lg:hidden fixed top-0 left-0 right-0 h-14 bg-white/80 backdrop-blur-xl border-b border-black/[0.06] px-4 flex items-center justify-between z-30"
      style="box-shadow: 0 1px 8px rgba(0,0,0,0.06);">
      <div class="flex items-center gap-2.5">
        <div
          class="w-7 h-7 bg-gradient-to-r from-orange-500 to-orange-600 rounded-[8px] flex items-center justify-center text-white shadow-sm">
          <i class="fa-solid fa-folder-open text-[11px]"></i>
        </div>
        <span class="text-[14px] font-bold text-slate-900 tracking-tight font-outfit">SaveMyPass<span
            class="text-orange-500">.dev</span></span>
      </div>
      <button @click="ui.toggleSidebar"
        class="w-9 h-9 flex items-center justify-center text-slate-400 hover:text-slate-700 transition-colors active:scale-95">
        <i class="fa-solid fa-bars-staggered text-base"></i>
      </button>
    </header>

    <!-- Sidebar (manages its own backdrop internally) -->
    <AppSidebar v-if="auth.isAuthenticated && (route.meta.requiresAuth || route.name === 'Vault')" />

    <div :class="[isAppLayout ? 'flex-1 flex flex-col overflow-y-auto' : 'w-full', { 'pt-14 lg:pt-0': showMobileHeader }]">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useUiStore } from '@/stores/ui'
import AppSidebar from '@/components/AppSidebar.vue'

const auth = useAuthStore()
const route = useRoute()
const ui = useUiStore()

const isAppLayout = computed(() => {
  return route.meta.requiresAuth || route.name === 'Vault'
})

const showMobileHeader = computed(() => {
  return auth.isAuthenticated && isAppLayout.value
})

auth.restoreSession()
</script>

<style>
.app-root {
  display: flex;
  height: 100vh;
  overflow: hidden;
  position: relative;
  background: #f0f4f8;
  background-image:
    radial-gradient(ellipse 80% 60% at 10% 0%, rgba(249, 115, 22, 0.06) 0%, transparent 55%),
    radial-gradient(ellipse 60% 50% at 90% 100%, rgba(99, 102, 241, 0.04) 0%, transparent 55%),
    radial-gradient(ellipse 50% 40% at 50% 50%, rgba(255, 255, 255, 0.5) 0%, transparent 70%);
}

.public-root {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  position: relative;
}

/* Page transitions */
.page-enter-active,
.page-leave-active {
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-enter-from {
  opacity: 0;
  transform: translateY(8px) scale(0.99);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-8px) scale(1.01);
}

html {
  font-family: 'Inter', -apple-system, system-ui, sans-serif;
  -webkit-font-smoothing: antialiased;
}

input,
button,
textarea {
  font-family: inherit;
}
</style>

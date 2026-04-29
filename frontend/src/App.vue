<template>
  <div class="flex h-screen bg-[#F2F2F7] overflow-hidden relative selection:bg-brand-orange/20">
    
    <!-- Mobile Header -->
    <header v-if="auth.isAuthenticated && (route.meta.requiresAuth || route.name === 'Home')" 
      class="lg:hidden fixed top-0 left-0 right-0 h-16 bg-white/80 backdrop-blur-xl border-b border-slate-100 px-5 flex items-center justify-between z-30">
      <div class="flex items-center gap-3">
        <div class="w-8 h-8 bg-brand-orange rounded-[10px] flex items-center justify-center text-white shadow-sm">
          <i class="fa-solid fa-key text-[13px]"></i>
        </div>
        <span class="text-[15px] font-bold text-slate-900 tracking-tight">SaveMyPass</span>
      </div>
      <button @click="ui.toggleSidebar" class="w-10 h-10 flex items-center justify-center text-slate-500 hover:text-slate-900 transition-colors active:scale-95">
        <i class="fa-solid fa-bars-staggered text-lg"></i>
      </button>
    </header>

    <!-- Sidebar -->
    <AppSidebar v-if="auth.isAuthenticated && (route.meta.requiresAuth || route.name === 'Home')" />
    
    <div class="flex-1 flex flex-col overflow-hidden pt-16 lg:pt-0">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>

    <!-- Mobile Backdrop -->
    <Transition name="fade">
      <div v-if="ui.isSidebarOpen" @click="ui.closeSidebar" 
        class="lg:hidden fixed inset-0 bg-black/20 backdrop-blur-[2px] z-40"></div>
    </Transition>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useUiStore } from '@/stores/ui'
import AppSidebar from '@/components/AppSidebar.vue'

const auth = useAuthStore()
const route = useRoute()
const ui = useUiStore()

auth.restoreSession()
</script>

<style>
.page-enter-active,
.page-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-enter-from {
  opacity: 0;
  transform: translateY(10px) scale(0.98);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(1.02);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

html {
  font-family: 'Inter', -apple-system, system-ui, sans-serif;
  -webkit-font-smoothing: antialiased;
}

input, button, textarea {
  font-family: inherit;
}
</style>

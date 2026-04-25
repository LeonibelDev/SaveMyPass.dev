<template>
  <div class="flex h-screen bg-slate-50 overflow-hidden">
    <AppSidebar v-if="auth.isAuthenticated && (route.meta.requiresAuth || route.name === 'Home')" />
    
    <div class="flex-1 flex flex-col overflow-y-auto">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AppSidebar from '@/components/AppSidebar.vue'

const auth = useAuthStore()
const route = useRoute()

// Restore AES key from sessionStorage on every page load/refresh.
// This runs synchronously before any route component renders.
auth.restoreSession()
</script>

<style>

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 260px;
  z-index: 50;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>

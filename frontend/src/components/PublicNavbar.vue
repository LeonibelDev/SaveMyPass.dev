<template>
  <nav
    class="sticky top-0 z-[100] w-full border-b border-slate-200/60 bg-white/80 backdrop-blur-xl transition-all duration-300">
    <div class="max-w-7xl mx-auto px-6 h-16 sm:h-20 flex items-center justify-between">
      <!-- Logo -->
      <router-link to="/" @click="isMenuOpen = false" class="flex items-center gap-3 active:scale-95 transition-transform group">
        <div
          class="w-9 h-9 sm:w-10 sm:h-10 bg-brand-orange text-white flex items-center justify-center rounded-xl shadow-sm transition-all group-hover:rotate-3">
          <i class="fa-solid fa-key text-[15px] sm:text-lg"></i>
        </div>
        <span class="font-bold text-slate-900 text-lg sm:text-xl tracking-tight">SaveMyPass<span
            class="text-brand-orange">.dev</span></span>
      </router-link>

      <!-- Navigation Links -->
      <div class="hidden md:flex items-center gap-8">
        <router-link v-if="isLanding" to="/about"
          class="text-[14px] font-semibold text-slate-500 hover:text-brand-orange transition-colors">
          Security
        </router-link>
        <router-link to="/login"
          class="text-[14px] font-semibold text-slate-500 hover:text-brand-orange transition-colors">
          Log In
        </router-link>
        <router-link to="/signup"
          class="bg-slate-900 text-white text-[14px] font-bold px-6 py-2.5 rounded-xl hover:bg-slate-800 transition-all active:scale-95">
          Sign Up
        </router-link>
      </div>

      <!-- Mobile Toggle -->
      <div class="md:hidden">
        <button @click="isMenuOpen = !isMenuOpen" 
          class="w-10 h-10 flex items-center justify-center text-slate-500 active:scale-90 transition-transform">
          <i class="fa-solid text-xl" :class="isMenuOpen ? 'fa-xmark' : 'fa-bars-staggered'"></i>
        </button>
      </div>
    </div>

    <!-- Mobile Menu Overlay -->
    <Transition name="mobile-menu">
      <div v-if="isMenuOpen" class="md:hidden fixed inset-x-0 top-16 bottom-0 bg-white/95 backdrop-blur-2xl z-[90] flex flex-col p-6 gap-6">
        <router-link v-if="isLanding" to="/about" @click="isMenuOpen = false"
          class="text-xl font-bold text-slate-900 flex items-center justify-between border-b border-slate-100 pb-4">
          Security <i class="fa-solid fa-chevron-right text-slate-300 text-sm"></i>
        </router-link>
        <router-link to="/login" @click="isMenuOpen = false"
          class="text-xl font-bold text-slate-900 flex items-center justify-between border-b border-slate-100 pb-4">
          Log In <i class="fa-solid fa-chevron-right text-slate-300 text-sm"></i>
        </router-link>
        <router-link to="/signup" @click="isMenuOpen = false"
          class="w-full bg-slate-900 text-white text-center py-4 rounded-2xl font-bold text-lg shadow-xl shadow-slate-200 mt-auto">
          Get Started
        </router-link>
      </div>
    </Transition>
  </nav>
</template>


<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isLanding = computed(() => route.name === 'Landing')
const isMenuOpen = ref(false)
</script>

<style scoped>
.mobile-menu-enter-active,
.mobile-menu-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.mobile-menu-enter-from,
.mobile-menu-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>

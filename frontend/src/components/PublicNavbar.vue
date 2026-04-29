<template>
  <nav
    class="sticky top-0 z-[100] w-full border-b border-slate-200/60 bg-white/80 backdrop-blur-xl transition-all duration-300">
    <div class="max-w-7xl mx-auto px-6 h-16 sm:h-20 flex items-center justify-between">
      <!-- Logo -->
      <router-link to="/" class="flex items-center gap-3 active:scale-95 transition-transform group">
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
        <button class="w-10 h-10 flex items-center justify-center text-slate-500 active:scale-90 transition-transform">
          <i class="fa-solid fa-bars-staggered text-xl"></i>
        </button>
      </div>
    </div>
  </nav>
</template>


<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isLanding = computed(() => route.name === 'Landing')
</script>

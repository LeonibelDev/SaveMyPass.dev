<template>
  <nav class="sticky top-0 z-[100] w-full border-b border-slate-200/60 bg-white/70 backdrop-blur-xl transition-all duration-300">
    <div class="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
      <!-- Logo -->
      <router-link to="/" class="flex items-center gap-3 transition-transform hover:scale-[1.02] active:scale-95 group">
        <div class="w-10 h-10 bg-brand-orange text-white flex items-center justify-center rounded-xl shadow-lg shadow-orange-200 transition-all group-hover:rotate-6">
          <i class="fa-solid fa-key text-lg"></i>
        </div>
        <span class="font-black text-slate-900 text-xl tracking-tighter">SaveMyPass<span class="text-brand-orange">.dev</span></span>
      </router-link>

      <!-- Navigation Links -->
      <div class="hidden md:flex items-center gap-10">
        <router-link v-if="isLanding" to="/about" class="text-[10px] font-black text-slate-400 hover:text-brand-orange uppercase tracking-[0.2em] transition-colors">
          Security Protocol
        </router-link>
        <div class="h-4 w-px bg-slate-200"></div>
        <router-link to="/login" class="text-[10px] font-black text-slate-500 hover:text-brand-orange uppercase tracking-[0.2em] transition-colors">
          Log In
        </router-link>
        <router-link to="/signup" class="bg-slate-900 text-white text-[10px] font-black uppercase tracking-[0.2em] px-8 py-3.5 rounded-xl hover:bg-slate-800 transition-all shadow-xl shadow-slate-200 active:scale-95">
          Sign Up
        </router-link>
      </div>

      <!-- Mobile Toggle (Placeholder) -->
      <div class="md:hidden">
        <button class="w-10 h-10 flex items-center justify-center text-slate-500">
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

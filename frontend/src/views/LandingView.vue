<template>
  <div class="bg-black min-h-screen text-white font-sans selection:bg-orange-500 selection:text-white overflow-hidden relative">
    
    <!-- Background grid effect -->
    <div class="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iMSIgY3k9IjEiIHI9IjEiIGZpbGw9InJnYmEoMjU1LCAyNTUsIDI1NSwgMC4wNykiLz48L3N2Zz4=')] opacity-20 pointer-events-none"></div>

    <!-- Navbar -->
    <nav class="relative z-10 border-b border-white/10 backdrop-blur-md bg-black/50">
      <div class="max-w-6xl mx-auto px-6 h-20 flex items-center justify-between">
        <div class="flex items-center gap-2 group">
          <div class="w-8 h-8 bg-white text-black flex items-center justify-center rounded-lg font-bold group-hover:bg-orange-500 group-hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <span class="font-bold text-white text-lg tracking-tight">SaveMyPass<span class="text-orange-500">.dev</span></span>
        </div>

        <div class="flex gap-6 items-center">
          <router-link to="/about" class="text-sm font-semibold text-slate-400 hover:text-white transition-colors hidden sm:block">How it works</router-link>
          <div class="h-4 w-px bg-white/20 hidden sm:block"></div>
          <router-link to="/login" class="text-sm font-semibold text-slate-400 hover:text-white transition-colors">Log In</router-link>
          <router-link to="/signup" class="text-sm font-semibold bg-white text-black hover:bg-slate-200 px-5 py-2 rounded-full transition-colors">Sign Up</router-link>
        </div>
      </div>
    </nav>

    <!-- Hero Section -->
    <main class="relative z-10 max-w-6xl mx-auto px-6 pt-32 pb-24 text-center">
      
      <!-- Badge -->
      <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full border border-orange-500/30 bg-orange-500/10 text-orange-500 text-xs font-bold uppercase tracking-widest mb-8">
        <span class="w-2 h-2 rounded-full bg-orange-500 animate-pulse"></span>
        Zero-Knowledge
      </div>

      <h1 class="text-5xl md:text-7xl font-extrabold tracking-tight mb-8">
        Secure your secrets.<br>
        <span class="text-transparent bg-clip-text bg-gradient-to-r from-orange-400 to-rose-500">
          Trust the math.
        </span>
      </h1>
      
      <p class="text-lg md:text-2xl text-slate-400 font-medium max-w-2xl mx-auto mb-12 leading-relaxed">
        An open-source, mathematically enforced password manager. 
        AES-256-GCM encryption natively in your browser. The server never learns your master key.
      </p>

      <div class="flex flex-col sm:flex-row items-center justify-center gap-4">
        <router-link to="/signup" class="w-full sm:w-auto bg-white text-black font-bold h-14 px-8 rounded-full flex items-center justify-center gap-2 hover:scale-105 transition-transform">
          Create your Vault
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3" />
          </svg>
        </router-link>
        <router-link to="/about" class="w-full sm:w-auto bg-white/5 border border-white/10 text-white font-bold h-14 px-8 rounded-full flex items-center justify-center hover:bg-white/10 transition-colors">
          Read Technical Audit
        </router-link>
      </div>

      <!-- Tech stack logos -->
      <div class="mt-32 pt-12 border-t border-white/10">
        <p class="text-xs font-bold text-slate-600 uppercase tracking-widest mb-8">Powered by Industry Standards</p>
        <div class="flex flex-wrap justify-center items-center gap-12 sm:gap-20 opacity-50 grayscale hover:grayscale-0 transition-all duration-500">
          <!-- AES -->
          <span class="text-xl font-extrabold tracking-tight">AES-GCM</span>
          <!-- PBKDF2 -->
          <span class="text-xl font-extrabold tracking-tight">PBKDF2</span>
          <!-- Vue -->
          <span class="text-xl font-extrabold tracking-tight flex items-center gap-1">
            <svg class="w-6 h-6 text-[#42b883]" viewBox="0 0 256 221" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M204.8 0H256L128 221.2 0 0h97.92L128 51.52 158.08 0z"/><path fill="currentColor" d="M0 0h51.2L128 132.48 204.8 0h51.2L128 221.2H0z"/></svg>
            Vue 3
          </span>
          <!-- Spring -->
          <span class="text-xl font-extrabold tracking-tight flex items-center gap-1 text-[#6db33f]">
            Spring
          </span>
        </div>
      </div>

    </main>

    <!-- Footer -->
    <footer class="relative z-10 border-t border-white/10 py-12 text-center text-slate-600 text-sm font-medium">
      <p>SaveMyPass.dev &copy; 2026. Data sovereignty achieved.</p>
    </footer>
  </div>
</template>

<script setup>
</script>

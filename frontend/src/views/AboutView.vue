<template>
  <div class="bg-[#FAFBFD] min-h-screen text-slate-900 font-sans selection:bg-orange-100 selection:text-orange-950">
    
    <!-- Navbar -->
    <PublicNavbar />

    <!-- Main -->
    <main class="max-w-3xl mx-auto px-6 py-24 animate-fade-in">
      
      <!-- Header -->
      <div class="mb-16 border-b border-slate-100 pb-12 text-center md:text-left">
        
        <div class="inline-flex px-3.5 py-1 bg-brand-orange-light border border-orange-100 text-brand-orange text-[12px] font-bold rounded-full mb-6 uppercase tracking-wider">
          Technical Architecture
        </div>

        <h1 class="text-4xl md:text-5xl font-black text-slate-950 tracking-tight mb-6 leading-tight font-outfit">
          Security Architecture
        </h1>

        <p class="text-lg md:text-xl text-slate-500 leading-relaxed max-w-2xl font-medium">
          SaveMyPass.dev is built on absolute cryptographic guarantees. The server cannot read your data — by design.
        </p>
      </div>

      <!-- Sections -->
      <div class="space-y-12">

        <!-- Section -->
        <section class="bg-white p-8 rounded-3xl border border-slate-100 shadow-sm hover:shadow-md transition-shadow group">
          <div class="flex items-center gap-4 mb-5">
            <div class="w-12 h-12 rounded-2xl bg-orange-50 flex items-center justify-center text-brand-orange transition-all group-hover:bg-brand-orange group-hover:text-white shadow-sm">
              <i class="fa-solid fa-folder-closed text-xl"></i>
            </div>

            <h2 class="text-2xl font-bold text-slate-950 font-outfit">
              Zero-Knowledge Vault
            </h2>
          </div>

          <p class="text-[15px] leading-relaxed text-slate-500 font-medium">
            Your master password never leaves your browser. It is transformed into an encryption key locally, and only encrypted data is stored. Even in the event of a database breach, attackers gain nothing usable.
          </p>
        </section>

        <!-- Section -->
        <section class="bg-white p-8 rounded-3xl border border-slate-100 shadow-sm hover:shadow-md transition-shadow group">
          <div class="flex items-center gap-4 mb-5">
            <div class="w-12 h-12 rounded-2xl bg-emerald-50 flex items-center justify-center text-emerald-500 transition-all group-hover:bg-emerald-500 group-hover:text-white shadow-sm">
              <i class="fa-solid fa-key text-xl"></i>
            </div>

            <h2 class="text-2xl font-bold text-slate-950 font-outfit">
              PBKDF2-HMAC-SHA256
            </h2>
          </div>

          <p class="text-[15px] leading-relaxed text-slate-500 font-medium mb-4">
            Uses a massive factor of <span class="bg-emerald-50 text-emerald-600 px-2 py-0.5 rounded-lg text-[13px] font-bold border border-emerald-100">600,000 iterations</span> with a secure random salt, aligned with modern OWASP recommendations to resist brute-force attacks.
          </p>
        </section>

        <!-- Section -->
        <section class="bg-white p-8 rounded-3xl border border-slate-100 shadow-sm hover:shadow-md transition-shadow group">
          <div class="flex items-center gap-4 mb-5">
            <div class="w-12 h-12 rounded-2xl bg-blue-50 flex items-center justify-center text-blue-500 transition-all group-hover:bg-blue-500 group-hover:text-white shadow-sm">
              <i class="fa-solid fa-shield-halved text-xl"></i>
            </div>

            <h2 class="text-2xl font-bold text-slate-950 font-outfit">
              AES-256-GCM
            </h2>
          </div>

          <p class="text-[15px] leading-relaxed text-slate-500 font-medium">
            Uses AES-GCM with 256-bit keys and authentication tags to ensure both confidentiality and absolute integrity of your data.
          </p>
        </section>

      </div>

      <!-- CTA -->
      <div class="mt-20 text-center">
        <router-link 
          to="/signup"
          class="inline-flex items-center justify-center gap-2 bg-gradient-to-r from-orange-500 to-orange-600 text-white font-bold px-8 py-4 rounded-2xl hover:shadow-[0_10px_25px_rgba(249,115,22,0.3)] transition-all hover:scale-105 active:scale-95 shadow-[0_4px_12px_rgba(249,115,22,0.15)] text-base">
          Start Your Vault
          <i class="fa-solid fa-circle-right text-[15px]"></i>
        </router-link>
      </div>

    </main>

    <!-- Footer -->
    <footer class="border-t border-slate-100 mt-28 py-12 text-center text-slate-400 text-[13px] font-medium bg-white">
      <p>&copy; 2026 SaveMyPass.dev • Designed for sovereignty</p>
    </footer>

  </div>
</template>

<script setup>
import PublicNavbar from '@/components/PublicNavbar.vue'
</script>
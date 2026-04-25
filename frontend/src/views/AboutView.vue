<template>
  <div class="bg-[#F8FAF9] min-h-screen text-slate-900 font-sans selection:bg-orange-100 selection:text-orange-950">
    
    <!-- Navbar -->
    <PublicNavbar />

    <!-- Main -->
    <main class="max-w-3xl mx-auto px-6 py-20">
      
      <!-- Header -->
      <div class="mb-14 border-b border-slate-200 pb-10">
        
        <div class="inline-flex px-3 py-1 bg-orange-100 text-orange-600 text-xs font-semibold rounded-full mb-5">
          Technical Standards
        </div>

        <h1 class="text-4xl md:text-5xl font-bold text-slate-950 tracking-tight mb-6 leading-tight">
          Security Architecture
        </h1>

        <p class="text-lg text-slate-500 leading-relaxed max-w-xl">
          SaveMyPass.dev is built on strong cryptographic guarantees. The server cannot read your data—by design.
        </p>
      </div>

      <!-- Sections -->
      <div class="space-y-14">

        <!-- Section -->
        <section class="group">
          <div class="flex items-center gap-4 mb-4">
            <div class="w-11 h-11 rounded-xl bg-white border border-slate-200 flex items-center justify-center text-orange-500 transition group-hover:bg-orange-500 group-hover:text-white">
              <!-- icon -->
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
            </div>

            <h2 class="text-xl font-semibold text-slate-900">
              Zero-Knowledge Vault
            </h2>
          </div>

          <p class="text-base leading-relaxed text-slate-600">
            Your master password never leaves your browser. It is transformed into an encryption key locally, and only encrypted data is stored. Even in the event of a breach, attackers gain nothing usable.
          </p>
        </section>

        <!-- Section -->
        <section class="group">
          <div class="flex items-center gap-4 mb-4">
            <div class="w-11 h-11 rounded-xl bg-white border border-slate-200 flex items-center justify-center text-emerald-500 transition group-hover:bg-emerald-500 group-hover:text-white">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
            </div>

            <h2 class="text-xl font-semibold text-slate-900">
              PBKDF2-HMAC-SHA256
            </h2>
          </div>

          <p class="text-base leading-relaxed text-slate-600">
            Uses <span class="bg-emerald-50 text-emerald-600 px-1 py-0.5 rounded text-sm font-medium">600,000 iterations</span> with a secure random salt, aligned with modern OWASP recommendations to resist brute-force attacks.
          </p>
        </section>

        <!-- Section -->
        <section class="group">
          <div class="flex items-center gap-4 mb-4">
            <div class="w-11 h-11 rounded-xl bg-white border border-slate-200 flex items-center justify-center text-blue-500 transition group-hover:bg-blue-500 group-hover:text-white">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>

            <h2 class="text-xl font-semibold text-slate-900">
              AES-256-GCM
            </h2>
          </div>

          <p class="text-base leading-relaxed text-slate-600">
            Uses AES-GCM with 256-bit keys and authentication tags to ensure both encryption and integrity of your data.
          </p>
        </section>

      </div>

      <!-- CTA -->
      <div class="mt-20 text-center">
        <router-link 
          to="/signup"
          class="inline-flex items-center justify-center gap-2 bg-orange-500 text-white font-semibold px-8 py-3 rounded-xl hover:bg-orange-600 transition text-sm">
          Start Your Vault
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3" />
          </svg>
        </router-link>
      </div>

    </main>

    <!-- Footer -->
    <footer class="border-t border-slate-200 mt-20 py-10 text-center text-slate-400 text-xs">
      <p>&copy; 2026 SaveMyPass.dev • Designed for sovereignty</p>
    </footer>

  </div>
</template>

<script setup>
import PublicNavbar from '@/components/PublicNavbar.vue'
</script>
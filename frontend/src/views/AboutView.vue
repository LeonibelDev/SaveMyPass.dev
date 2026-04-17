<template>
  <div class="bg-black min-h-screen text-slate-300 font-sans selection:bg-orange-500 selection:text-white">
    <!-- Navbar -->
    <nav class="border-b border-white/10">
      <div class="max-w-5xl mx-auto px-6 h-20 flex items-center justify-between">
        <router-link to="/" class="flex items-center gap-2 group">
          <div class="w-8 h-8 bg-white text-black flex items-center justify-center rounded-lg font-bold group-hover:bg-orange-500 group-hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <span class="font-bold text-white text-lg tracking-tight">SaveMyPass<span class="text-orange-500">.dev</span></span>
        </router-link>

        <div class="flex gap-6 items-center">
          <router-link to="/about" class="text-sm font-semibold hover:text-white transition-colors">Architecture</router-link>
          <router-link to="/login" class="text-sm font-semibold text-white bg-white/10 hover:bg-white/20 px-5 py-2 rounded-full transition-colors">Sign In</router-link>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main class="max-w-3xl mx-auto px-6 py-24">
      <div class="mb-16 border-b border-white/10 pb-12">
        <h1 class="text-4xl md:text-5xl font-extrabold text-white tracking-tight mb-6">Security Architecture</h1>
        <p class="text-xl text-slate-400 leading-relaxed font-medium">
          SaveMyPass.dev is built upon absolute cryptographic guarantees. Trust is not required, because the server simply cannot read your data.
        </p>
      </div>

      <div class="space-y-16">
        
        <!-- Section 1 -->
        <section>
          <div class="flex items-center gap-4 mb-4">
            <div class="w-12 h-12 rounded-2xl bg-orange-500/10 border border-orange-500/20 flex items-center justify-center text-orange-500">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-white tracking-tight">Zero-Knowledge Architecture</h2>
          </div>
          <p class="text-lg leading-relaxed text-slate-400">
            End-to-end encryption means exactly what it says. Your master password never leaves your browser. It is mathematically transformed into an encryption key locally. The server only receives and stores the meaningless, encrypted ciphertext. A database leak would yield absolutely nothing usable to an attacker.
          </p>
        </section>

        <!-- Section 2 -->
        <section>
          <div class="flex items-center gap-4 mb-4">
            <div class="w-12 h-12 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-emerald-500">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-white tracking-tight">600,000 PBKDF2 Iterations</h2>
          </div>
          <p class="text-lg leading-relaxed text-slate-400 mb-6">
            Even if an attacker obtains the server database, they would need to brute-force your master password. SaveMyPass.dev uses the <strong>PBKDF2-HMAC-SHA256</strong> algorithm with <strong>600,000 iterations</strong> running against a cryptographically secure random salt. This matches the OWASP 2026 standard, rendering GPU-accelerated brute-force attacks computationally unfeasible.
          </p>
        </section>

        <!-- Section 3 -->
        <section>
          <div class="flex items-center gap-4 mb-4">
            <div class="w-12 h-12 rounded-2xl bg-blue-500/10 border border-blue-500/20 flex items-center justify-center text-blue-500">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-white tracking-tight">AES-256-GCM Natively</h2>
          </div>
          <p class="text-lg leading-relaxed text-slate-400">
            For individual credentials, the application uses <strong>AES-GCM</strong> (Advanced Encryption Standard with Galois/Counter Mode) utilizing 256-bit keys and 128-bit authentication tags. This not only encrypts the data but guarantees tamper-resistance. Any unauthorized modification to the database ciphertext will result in an absolute decryption failure in the browser.
          </p>
        </section>

        <!-- Section 4 -->
        <section>
          <div class="flex items-center gap-4 mb-4">
            <div class="w-12 h-12 rounded-2xl bg-indigo-500/10 border border-indigo-500/20 flex items-center justify-center text-indigo-500">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7v8a2 2 0 002 2h6M8 7V5a2 2 0 012-2h4.586a1 1 0 01.707.293l4.414 4.414a1 1 0 01.293.707V15a2 2 0 01-2 2h-2M8 7H6a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2v-2" />
              </svg>
            </div>
            <h2 class="text-2xl font-bold text-white tracking-tight">Local PDF Exports</h2>
          </div>
          <p class="text-lg leading-relaxed text-slate-400">
            Data sovereignty is paramount. The vault export feature generates a high-quality PDF entirely within your local browser memory layer using WebAssembly tools. Your plaintext clipboard and export files never touch a network socket.
          </p>
        </section>

      </div>

      <div class="mt-24 text-center">
        <router-link to="/signup" class="inline-flex items-center justify-center gap-2 bg-white text-black font-bold px-8 py-4 rounded-full hover:bg-slate-200 transition-colors shadow-[0_0_40px_rgba(255,255,255,0.1)]">
          Create Secure Vault
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3" />
          </svg>
        </router-link>
      </div>

    </main>

    <!-- Footer -->
    <footer class="border-t border-white/10 mt-24 py-12 text-center text-slate-500 text-sm">
      <p>&copy; 2026 SaveMyPass.dev. Designed for developers.</p>
    </footer>
  </div>
</template>

<script setup>
</script>

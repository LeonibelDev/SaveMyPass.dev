<template>
  <div
    class="bg-[#F8FAF9] h-full flex flex-col text-slate-900 font-sans selection:bg-orange-100 selection:text-orange-950">
    <!-- Navbar -->
    <PublicNavbar />

    <!-- Scrollable Content -->
    <div class="flex-1 overflow-y-auto no-scrollbar">
      <main class="max-w-5xl mx-auto px-6 py-20">

        <!-- Hero -->
        <section class="mb-24 border-b border-slate-200 pb-14">

          <div class="inline-flex px-3 py-1 bg-orange-100 text-orange-600 text-xs font-semibold rounded-full mb-6">
            Technical Standards
          </div>

          <h1 class="text-4xl md:text-6xl font-bold text-slate-950 tracking-tight mb-8 leading-tight max-w-4xl">
            Security Architecture
          </h1>

          <p class="text-lg md:text-xl text-slate-500 leading-relaxed max-w-2xl">
            SaveMyPass.dev is designed around a zero-knowledge architecture where encryption happens entirely on the
            client side. The server never has access to your master password, encryption keys, or decrypted vault data.
          </p>

        </section>

        <!-- Core Security -->
        <section class="space-y-14">

          <!-- Zero Knowledge -->
          <section class="group">
            <div class="flex items-start gap-5">

              <div
                class="w-12 h-12 rounded-2xl bg-white border border-slate-200 flex items-center justify-center text-orange-500 shrink-0 transition group-hover:bg-orange-500 group-hover:text-white">

                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                </svg>

              </div>

              <div>

                <h2 class="text-2xl font-semibold text-slate-950 mb-4">
                  Zero-Knowledge Vault
                </h2>

                <p class="text-slate-600 leading-relaxed text-base md:text-lg">
                  Your master password never leaves your browser. Encryption keys are derived locally and used entirely
                  client-side before any network transmission occurs.
                </p>

                <p class="text-slate-600 leading-relaxed text-base md:text-lg mt-4">
                  SaveMyPass.dev servers only store authenticated ciphertext and encrypted vault metadata. Vault
                  contents
                  remain inaccessible to the backend infrastructure by design.
                </p>

              </div>

            </div>
          </section>

          <!-- PBKDF2 -->
          <section class="group">
            <div class="flex items-start gap-5">

              <div
                class="w-12 h-12 rounded-2xl bg-white border border-slate-200 flex items-center justify-center text-emerald-500 shrink-0 transition group-hover:bg-emerald-500 group-hover:text-white">

                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>

              </div>

              <div>

                <h2 class="text-2xl font-semibold text-slate-950 mb-4">
                  PBKDF2-HMAC-SHA256
                </h2>

                <p class="text-slate-600 leading-relaxed text-base md:text-lg">
                  Master passwords are transformed into cryptographic keys using
                  <span
                    class="bg-emerald-50 text-emerald-700 px-2 py-1 rounded-md text-sm font-medium">PBKDF2-HMAC-SHA256</span>
                  with
                  <span class="bg-slate-100 text-slate-700 px-2 py-1 rounded-md text-sm font-medium">600,000
                    iterations</span>
                  and a securely generated random salt.
                </p>

                <p class="text-slate-600 leading-relaxed text-base md:text-lg mt-4">
                  This significantly increases computational cost for brute-force attempts and aligns with modern
                  password
                  hardening recommendations.
                </p>

              </div>

            </div>
          </section>

          <!-- AES -->
          <section class="group">
            <div class="flex items-start gap-5">

              <div
                class="w-12 h-12 rounded-2xl bg-white border border-slate-200 flex items-center justify-center text-blue-500 shrink-0 transition group-hover:bg-blue-500 group-hover:text-white">

                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                </svg>

              </div>

              <div>

                <h2 class="text-2xl font-semibold text-slate-950 mb-4">
                  AES-256-GCM Encryption
                </h2>

                <p class="text-slate-600 leading-relaxed text-base md:text-lg">
                  Vault entries are encrypted using AES-256-GCM, providing both confidentiality and integrity through
                  authenticated encryption.
                </p>

                <p class="text-slate-600 leading-relaxed text-base md:text-lg mt-4">
                  Every encryption operation uses a unique initialization vector and authentication tag to prevent data
                  tampering and cryptographic replay attacks.
                </p>

              </div>

            </div>
          </section>

        </section>

        <!-- Architecture Flow -->
        <section class="mt-28">

          <div class="mb-10">

            <div class="inline-flex px-3 py-1 bg-slate-100 text-slate-600 text-xs font-semibold rounded-full mb-5">
              System Design
            </div>

            <h2 class="text-3xl md:text-4xl font-bold tracking-tight text-slate-950 mb-5">
              End-to-End Encryption Flow
            </h2>

            <p class="text-slate-600 text-lg leading-relaxed max-w-2xl">
              Encryption and decryption operations occur entirely within the browser before communication with the
              backend
              infrastructure.
            </p>

          </div>

          <div class="rounded-3xl border border-slate-200 bg-white p-8 md:p-10 shadow-sm">

            <div class="grid md:grid-cols-3 gap-10">

              <!-- Step -->
              <div class="text-center">

                <div
                  class="w-16 h-16 rounded-3xl bg-orange-100 text-orange-600 flex items-center justify-center text-2xl mx-auto mb-5">
                  🔑
                </div>

                <h3 class="font-semibold text-lg text-slate-900 mb-3">
                  Key Derivation
                </h3>

                <p class="text-sm text-slate-500 leading-relaxed">
                  The master password is transformed into a secure encryption key using PBKDF2-HMAC-SHA256 and a random
                  cryptographic salt.
                </p>

              </div>

              <!-- Step -->
              <div class="text-center">

                <div
                  class="w-16 h-16 rounded-3xl bg-blue-100 text-blue-600 flex items-center justify-center text-2xl mx-auto mb-5">
                  🔒
                </div>

                <h3 class="font-semibold text-lg text-slate-900 mb-3">
                  Local Encryption
                </h3>

                <p class="text-sm text-slate-500 leading-relaxed">
                  Vault data is encrypted client-side using AES-256-GCM before any information is transmitted
                  externally.
                </p>

              </div>

              <!-- Step -->
              <div class="text-center">

                <div
                  class="w-16 h-16 rounded-3xl bg-emerald-100 text-emerald-600 flex items-center justify-center text-2xl mx-auto mb-5">
                  ☁️
                </div>

                <h3 class="font-semibold text-lg text-slate-900 mb-3">
                  Secure Storage
                </h3>

                <p class="text-sm text-slate-500 leading-relaxed">
                  Only encrypted ciphertext is stored within backend systems and PostgreSQL databases.
                </p>

              </div>

            </div>

          </div>

        </section>

        <!-- Server Knowledge -->
        <section class="mt-28">

          <div class="rounded-3xl border border-slate-200 bg-white p-8 md:p-10 shadow-sm">

            <div class="mb-10">

              <div class="inline-flex px-3 py-1 bg-slate-100 text-slate-600 text-xs font-semibold rounded-full mb-5">
                Threat Model
              </div>

              <h2 class="text-3xl font-bold tracking-tight text-slate-950 mb-4">
                What the Server Knows
              </h2>

              <p class="text-slate-600 leading-relaxed text-lg max-w-2xl">
                SaveMyPass.dev is intentionally designed to minimize backend visibility into user vault data.
              </p>

            </div>

            <div class="grid md:grid-cols-2 gap-10">

              <!-- Accessible -->
              <div>

                <div class="flex items-center gap-3 mb-5">

                  <div class="w-3 h-3 rounded-full bg-emerald-500"></div>

                  <h3 class="font-semibold text-emerald-600">
                    Accessible to the server
                  </h3>

                </div>

                <ul class="space-y-4 text-slate-600">

                  <li>• Encrypted vault ciphertext</li>
                  <li>• Account email address</li>
                  <li>• Metadata timestamps</li>
                  <li>• Authentication-related hashes</li>
                  <li>• Session and infrastructure logs</li>

                </ul>

              </div>

              <!-- Inaccessible -->
              <div>

                <div class="flex items-center gap-3 mb-5">

                  <div class="w-3 h-3 rounded-full bg-red-500"></div>

                  <h3 class="font-semibold text-red-500">
                    Never accessible
                  </h3>

                </div>

                <ul class="space-y-4 text-slate-600">

                  <li>• Master passwords</li>
                  <li>• Decrypted vault contents</li>
                  <li>• Raw encryption keys</li>
                  <li>• Plaintext credentials</li>
                  <li>• Sensitive vault notes</li>

                </ul>

              </div>

            </div>

          </div>

        </section>

        <!-- Stack -->
        <section class="mt-28">

          <div class="mb-10">

            <div class="inline-flex px-3 py-1 bg-slate-100 text-slate-600 text-xs font-semibold rounded-full mb-5">
              Technology
            </div>

            <h2 class="text-3xl md:text-4xl font-bold tracking-tight text-slate-950 mb-5">
              Modern Security Stack
            </h2>

            <p class="text-slate-600 text-lg leading-relaxed max-w-2xl">
              Built using modern technologies focused on reliability, performance, and cryptographic safety.
            </p>

          </div>

          <div class="grid sm:grid-cols-2 lg:grid-cols-3 gap-5">

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
              <h3 class="font-semibold text-slate-950 mb-2">Vue.js</h3>
              <p class="text-sm text-slate-500 leading-relaxed">
                Reactive frontend architecture optimized for secure client-side cryptographic operations.
              </p>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
              <h3 class="font-semibold text-slate-950 mb-2">Spring Boot Backend</h3>
              <p class="text-sm text-slate-500 leading-relaxed">
                High-performance backend infrastructure focused on scalability and minimal attack surface.
              </p>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
              <h3 class="font-semibold text-slate-950 mb-2">PostgreSQL</h3>
              <p class="text-sm text-slate-500 leading-relaxed">
                Reliable encrypted data persistence for vault metadata and infrastructure operations.
              </p>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
              <h3 class="font-semibold text-slate-950 mb-2">AES-256-GCM</h3>
              <p class="text-sm text-slate-500 leading-relaxed">
                Authenticated encryption standard providing confidentiality and integrity guarantees.
              </p>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
              <h3 class="font-semibold text-slate-950 mb-2">PBKDF2</h3>
              <p class="text-sm text-slate-500 leading-relaxed">
                Password-based key derivation resistant to large-scale brute-force attacks.
              </p>
            </div>

            <div class="bg-white border border-slate-200 rounded-2xl p-6">
              <h3 class="font-semibold text-slate-950 mb-2">JWT Authentication</h3>
              <p class="text-sm text-slate-500 leading-relaxed">
                Stateless session management with secure authentication token validation.
              </p>
            </div>

          </div>

        </section>

        <!-- Open Source -->
        <section class="mt-28">

          <div class="rounded-3xl bg-slate-950 text-white p-10 border border-slate-800 overflow-hidden">

            <div class="max-w-2xl">

              <div class="inline-flex px-3 py-1 rounded-full bg-white/10 text-white text-xs font-medium mb-6">
                Transparency
              </div>

              <h2 class="text-3xl md:text-4xl font-bold mb-6 leading-tight">
                Security Through Transparency
              </h2>

              <p class="text-slate-300 leading-relaxed text-lg mb-8">
                SaveMyPass.dev follows an open and verifiable security architecture. Cryptographic implementations,
                infrastructure decisions, and core security principles are publicly documented for community review and
                independent verification.
              </p>

              <a href="https://github.com/leonibeldev/savemypass.dev" target="_blank"
                class="inline-flex items-center gap-3 bg-white text-slate-950 px-6 py-3 rounded-2xl font-medium hover:bg-slate-100 transition">

                View Source Code

                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3" />
                </svg>

              </a>

            </div>

          </div>

        </section>

        <!-- CTA -->
        <section class="mt-28 text-center">

          <div class="max-w-2xl mx-auto">

            <h2 class="text-3xl md:text-4xl font-bold tracking-tight text-slate-950 mb-6">
              Designed for Privacy Sovereignty
            </h2>

            <p class="text-slate-600 text-lg leading-relaxed mb-10">
              Your passwords belong to you — not to the server, the infrastructure provider, or the platform itself.
            </p>

            <router-link to="/signup"
              class="inline-flex items-center justify-center gap-3 bg-orange-500 text-white font-semibold px-8 py-4 rounded-2xl hover:bg-orange-600 transition text-sm shadow-sm hover:shadow-md">

              Create Your Vault

              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3" />
              </svg>

            </router-link>

          </div>

        </section>

        <!-- Footer -->
        <footer class="border-t border-slate-200 mt-28 py-10 text-center text-slate-400 text-xs">
          <p>&copy; 2026 SaveMyPass.dev • Designed for sovereignty</p>
        </footer>
      </main>
    </div>
  </div>
</template>

<script setup>
import PublicNavbar from '@/components/PublicNavbar.vue'
</script>
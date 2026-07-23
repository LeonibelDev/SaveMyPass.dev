<template>
  <div class="apple-landing selection:bg-brand-orange/20 overflow-x-hidden bg-[#FAFBFD] text-[#1d1d1f] antialiased">

    <PublicNavbar />

    <!-- Hero Section -->
    <section
      class="relative min-h-[90vh] flex flex-col items-center justify-center pt-24 pb-20 px-6 overflow-hidden bg-gradient-to-b from-slate-50 via-slate-50 to-[#FAFBFD]">
      <div
        class="absolute inset-0 z-0 bg-[radial-gradient(circle_at_center,rgba(249,115,22,0.04)_0,transparent_65%)] pointer-events-none">
      </div>
      <div class="absolute -top-40 -left-40 w-96 h-96 rounded-full bg-orange-100/30 blur-3xl pointer-events-none"></div>
      <div class="absolute top-1/3 -right-40 w-96 h-96 rounded-full bg-amber-100/20 blur-3xl pointer-events-none"></div>

      <div class="z-10 text-center max-w-5xl mx-auto flex flex-col items-center mt-10">
        <div class="inline-flex items-center gap-2 px-3 py-1 bg-brand-orange-light border border-orange-100 rounded-full mb-6 scale-up-anim">
          <span class="w-2 h-2 rounded-full bg-brand-orange animate-pulse"></span>
          <span class="text-[12px] font-bold text-brand-orange uppercase tracking-wider">Zero-Knowledge Protocol</span>
        </div>

        <h1
          class="text-5xl sm:text-7xl md:text-[5.75rem] font-black tracking-tight mb-8 text-slate-955 text-balance leading-[1.08] scale-up-anim">
          Zero-Knowledge.<br />
          <span
            class="text-transparent bg-clip-text bg-gradient-to-r from-orange-500 via-orange-600 to-red-600">Infinite
            Security.</span>
        </h1>

        <p
          class="text-lg md:text-[22px] text-slate-500 font-medium tracking-tight mb-12 max-w-3xl text-balance leading-relaxed fade-in-up-anim delay-100">
          SaveMyPass.dev is the ultimate vault for your digital identity. Engineered with client-side cryptography,
          designed for everyday humans.
        </p>

        <div class="flex flex-col sm:flex-row gap-4 fade-in-up-anim delay-200">
          <router-link to="/signup"
            class="bg-gradient-to-r from-orange-500 to-orange-600 text-white px-8 py-4 rounded-2xl font-bold text-lg hover:shadow-[0_10px_25px_rgba(249,115,22,0.35)] transition-all hover:scale-105 active:scale-95 shadow-[0_4px_12px_rgba(249,115,22,0.15)] flex items-center justify-center gap-2">
            Create Free Vault <i class="fa-solid fa-circle-right text-[15px]"></i>
          </router-link>
          <a href="#architecture"
            class="bg-white/80 backdrop-blur-sm text-slate-700 hover:text-slate-900 px-8 py-4 rounded-2xl font-bold text-lg hover:bg-white border border-slate-200 transition-all hover:shadow-sm active:scale-95 flex items-center justify-center">
            Learn architecture
          </a>
        </div>
      </div>

      <!-- Hero Mockup -->
      <div class="mt-28 relative w-full max-w-5xl mx-auto fade-in-up-anim delay-300 px-4">
        <div class="absolute inset-0 bg-gradient-to-b from-orange-500/10 to-transparent blur-3xl rounded-full"></div>
        <div
          class="relative bg-white/40 backdrop-blur-xl border border-slate-200/50 rounded-[32px] p-3 shadow-[0_30px_80px_rgba(0,0,0,0.06)] overflow-hidden">
          <!-- Mockup UI -->
          <div
            class="w-full bg-white rounded-[24px] border border-slate-100 shadow-sm flex flex-col md:flex-row overflow-hidden min-h-[500px]">

            <!-- Mock Sidebar (Light Theme to match the actual app sidebar) -->
            <div class="w-64 border-r border-slate-200/50 bg-[#F8FAFC] p-6 hidden md:flex flex-col gap-6 text-slate-500">
              <div class="flex items-center gap-3">
                <div class="w-9 h-9 bg-gradient-to-br from-orange-500 to-orange-600 rounded-[10px] flex items-center justify-center text-white shadow-[0_4px_10px_rgba(249,115,22,0.2)]">
                  <i class="fa-solid fa-folder-open text-sm"></i>
                </div>
                <span class="font-bold text-slate-900 tracking-tight font-outfit">SaveMyPass</span>
              </div>
              <div class="space-y-1.5 mt-6">
                <div
                  class="w-full h-11 bg-orange-50 border border-orange-100/50 text-brand-orange rounded-xl flex items-center px-4 gap-3 font-bold shadow-[inset_0_0_0_1px_rgba(249,115,22,0.15)]">
                  <i class="fa-solid fa-folder-closed text-[14px]"></i>
                  <span class="text-[14px]">Vault</span>
                </div>
                <div
                  class="w-full h-11 flex items-center px-4 gap-3 text-slate-500 font-medium hover:bg-slate-100/50 rounded-xl cursor-default transition-colors">
                  <i class="fa-solid fa-file-lines text-[14px]"></i>
                  <span class="text-[14px]">Secure Notes</span>
                </div>
                <div
                  class="w-full h-11 flex items-center px-4 gap-3 text-slate-500 font-medium hover:bg-slate-100/50 rounded-xl cursor-default transition-colors">
                  <i class="fa-solid fa-circle-user text-[14px]"></i>
                  <span class="text-[14px]">Settings</span>
                </div>
              </div>
            </div>

            <!-- Content -->
            <div class="flex-1 bg-[#F8FAFC] flex flex-col">
              <!-- Header -->
              <div class="h-16 border-b border-slate-200/60 bg-white/80 flex items-center px-6 justify-between">
                <div class="w-full max-w-sm h-9 bg-[#E2E8F0] rounded-xl flex items-center px-3 gap-2">
                  <i class="fa-solid fa-magnifying-glass text-slate-400 text-xs"></i>
                  <span class="text-[13px] text-slate-400 font-medium">Search vault...</span>
                </div>
                <div class="w-8 h-8 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-xl flex items-center justify-center shadow-sm">
                  <i class="fa-solid fa-plus text-xs"></i>
                </div>
              </div>

              <!-- List Area -->
              <div class="p-6 flex-1">
                <div class="flex items-center justify-between mb-4">
                  <h3 class="text-[11px] font-bold text-slate-450 uppercase tracking-widest">All Items</h3>
                </div>

                <!-- List Model based on HomeView -->
                <div
                  class="bg-white rounded-[20px] border border-slate-200/50 overflow-hidden divide-y divide-slate-100 shadow-sm">

                  <div class="flex items-center gap-4 px-5 py-4 hover:bg-slate-50 cursor-default transition-colors">
                    <div
                      class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 flex items-center justify-center flex-shrink-0">
                      <i class="fa-brands fa-github text-slate-700 text-lg"></i>
                    </div>
                    <div class="flex-1">
                      <p class="font-semibold text-[15px] text-slate-900 tracking-tight leading-tight">github.com</p>
                      <p class="text-[13px] text-slate-400 font-medium mt-0.5">developer@savemypass.dev</p>
                    </div>
                    <i class="fa-solid fa-chevron-right text-[11px] text-slate-200"></i>
                  </div>

                  <div class="flex items-center gap-4 px-5 py-4 hover:bg-slate-50 cursor-default transition-colors">
                    <div
                      class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 flex items-center justify-center flex-shrink-0">
                      <i class="fa-brands fa-aws text-slate-700 text-lg"></i>
                    </div>
                    <div class="flex-1">
                      <p class="font-semibold text-[15px] text-slate-900 tracking-tight leading-tight">aws.amazon.com</p>
                      <p class="text-[13px] text-slate-400 font-medium mt-0.5">admin-root</p>
                    </div>
                    <i class="fa-solid fa-chevron-right text-[11px] text-slate-200"></i>
                  </div>

                  <div class="flex items-center gap-4 px-5 py-4 hover:bg-slate-50 cursor-default transition-colors">
                    <div
                      class="w-10 h-10 rounded-xl bg-slate-50 border border-slate-100 flex items-center justify-center flex-shrink-0">
                      <i class="fa-brands fa-google text-slate-700 text-lg"></i>
                    </div>
                    <div class="flex-1">
                      <p class="font-semibold text-[15px] text-slate-900 tracking-tight leading-tight">google.com</p>
                      <p class="text-[13px] text-slate-400 font-medium mt-0.5">personal@gmail.com</p>
                    </div>
                    <i class="fa-solid fa-chevron-right text-[11px] text-slate-200"></i>
                  </div>

                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- The Problem Section -->
    <section class="py-36 bg-white text-slate-900 border-t border-slate-100">
      <div class="max-w-5xl mx-auto px-6">
        <h2 class="text-4xl md:text-6xl font-bold tracking-tight mb-8 text-slate-955">The digital identity crisis.</h2>
        <p class="text-[20px] md:text-2xl font-medium text-slate-500 leading-relaxed mb-20 max-w-4xl text-balance">
          In today's interconnected digital environment, managing multiple identities presents critical security
          challenges. We identified three major flaws in traditional password managers and set out to fix them.
        </p>

        <div class="grid md:grid-cols-3 gap-8">
          <div class="bg-[#F8FAFC] p-10 rounded-[32px] hover:bg-slate-50 transition-all duration-300 border border-slate-100/80 hover:shadow-lg group">
            <div class="w-12 h-12 rounded-2xl bg-orange-50 text-brand-orange flex items-center justify-center mb-8 group-hover:scale-110 transition-transform">
              <i class="fa-solid fa-lightbulb text-2xl"></i>
            </div>
            <h3 class="text-2xl font-bold mb-4 tracking-tight text-slate-955">Password Fatigue</h3>
            <p class="text-slate-500 text-[15px] leading-relaxed font-medium">The cognitive load of remembering complex
              combinations invariably leads to password reuse, drastically increasing your vulnerability to credential
              stuffing and brute force attacks across all your accounts.</p>
          </div>
          <div class="bg-[#F8FAFC] p-10 rounded-[32px] hover:bg-slate-50 transition-all duration-300 border border-slate-100/80 hover:shadow-lg group">
            <div class="w-12 h-12 rounded-2xl bg-orange-50 text-brand-orange flex items-center justify-center mb-8 group-hover:scale-110 transition-transform">
              <i class="fa-solid fa-file-lines text-2xl"></i>
            </div>
            <h3 class="text-2xl font-bold mb-4 tracking-tight text-slate-955">Insecure Storage</h3>
            <p class="text-slate-500 text-[15px] leading-relaxed font-medium">Many users resort to plain text documents,
              physical notes, or native browser storage without robust, independent encryption. This leaves your most
              sensitive data exposed to local malware and unauthorized access.</p>
          </div>
          <div class="bg-[#F8FAFC] p-10 rounded-[32px] hover:bg-slate-50 transition-all duration-300 border border-slate-100/80 hover:shadow-lg group">
            <div class="w-12 h-12 rounded-2xl bg-orange-50 text-brand-orange flex items-center justify-center mb-8 group-hover:scale-110 transition-transform">
              <i class="fa-solid fa-handshake text-2xl"></i>
            </div>
            <h3 class="text-2xl font-bold mb-4 tracking-tight text-slate-955">Risk Centralization</h3>
            <p class="text-slate-500 text-[15px] leading-relaxed font-medium">If a traditional centralized server is
              compromised and data isn't encrypted entirely on the client side, millions of credentials are exposed
              instantly. Your security shouldn't depend on a server you don't control.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Architecture Section -->
    <section id="architecture" class="py-36 bg-[#F8FAFC] text-slate-900 relative overflow-hidden border-t border-slate-100">
      <div class="max-w-5xl mx-auto px-6 relative z-10">
        <div class="text-center mb-32">
          <h2 class="text-5xl md:text-[5.5rem] font-bold tracking-tight mb-8 leading-none text-slate-955">Zero-Knowledge.<br />By design.
          </h2>
          <p class="text-xl md:text-2xl text-slate-500 font-medium max-w-3xl mx-auto text-balance leading-relaxed">
            The fundamental pillar of SaveMyPass is its cryptographic model. Our server acts solely as a repository
            for encrypted data. Encryption and decryption operations occur exclusively in your browser.
            <span class="text-brand-orange font-bold">We couldn't read your passwords even if we wanted to.</span>
          </p>
        </div>

        <div class="space-y-40">
          <!-- Feature 1 -->
          <div class="flex flex-col md:flex-row items-center gap-16 md:gap-24">
            <div class="flex-1 space-y-6">
              <div class="text-orange-500 font-bold tracking-widest uppercase text-xs">Military Grade Encryption</div>
              <h3 class="text-4xl md:text-5xl font-bold tracking-tight text-slate-955">AES-256-GCM.</h3>
              <p class="text-lg text-slate-500 leading-relaxed font-medium">
                Every credential and note is encrypted using the Advanced Encryption Standard (AES) with 256-bit keys in
                Galois/Counter Mode (GCM). This modern cryptographic standard provides both absolute confidentiality and
                tamper-proof integrity for every single item in your vault. Data never travels in plain text over the
                network.
              </p>
            </div>
            <div
              class="flex-1 w-full aspect-square bg-white border border-slate-200/50 rounded-[48px] flex items-center justify-center p-12 shadow-[0_20px_50px_rgba(0,0,0,0.03)] relative overflow-hidden">
              <div class="absolute inset-0 bg-orange-500/5 blur-3xl rounded-full"></div>
              <i
                class="fa-solid fa-shield-halved text-[10rem] md:text-[14rem] text-transparent bg-clip-text bg-gradient-to-br from-orange-400 to-red-500 relative z-10"></i>
            </div>
          </div>

          <!-- Feature 2 -->
          <div class="flex flex-col md:flex-row-reverse items-center gap-16 md:gap-24">
            <div class="flex-1 space-y-6">
              <div class="text-orange-500 font-bold tracking-widest uppercase text-xs">Key Derivation Function</div>
              <h3 class="text-4xl md:text-5xl font-bold tracking-tight text-slate-955">PBKDF2 SHA-256.</h3>
              <p class="text-lg text-slate-500 leading-relaxed font-medium">
                Your Master Password never leaves your device. Instead, we use PBKDF2 (Password-Based Key Derivation
                Function 2) with a SHA-256 hash and a massive factor of 600,000 iterations to derive your encryption
                key. This computationally expensive process renders dictionary and specialized hardware attacks
                mathematically unfeasible.
              </p>
            </div>
            <div
              class="flex-1 w-full aspect-square bg-white border border-slate-200/50 rounded-[48px] flex items-center justify-center p-12 shadow-[0_20px_50px_rgba(0,0,0,0.03)] relative overflow-hidden">
              <div class="absolute inset-0 bg-orange-500/5 blur-3xl rounded-full"></div>
              <i
                class="fa-solid fa-key text-[10rem] md:text-[14rem] text-transparent bg-clip-text bg-gradient-to-br from-orange-400 to-red-500 relative z-10"></i>
            </div>
          </div>

          <!-- Feature 3 -->
          <div class="flex flex-col md:flex-row items-center gap-16 md:gap-24">
            <div class="flex-1 space-y-6">
              <div class="text-orange-500 font-bold tracking-widest uppercase text-xs">Data Uniqueness</div>
              <h3 class="text-4xl md:text-5xl font-bold tracking-tight text-slate-955">Unique Initialization Vectors.</h3>
              <p class="text-lg text-slate-500 leading-relaxed font-medium">
                Encryption alone isn't enough if patterns can be detected. Each encrypted entry in SaveMyPass uses its
                own unique 12-byte Initialization Vector (IV). This ensures that even if you store the exact same
                password for two different accounts, the resulting encrypted data will look completely different to any
                observer or database admin.
              </p>
            </div>
            <div
              class="flex-1 w-full aspect-square bg-white border border-slate-200/50 rounded-[48px] flex items-center justify-center p-12 shadow-[0_20px_50px_rgba(0,0,0,0.03)] relative overflow-hidden">
              <div class="absolute inset-0 bg-orange-500/5 blur-3xl rounded-full"></div>
              <i
                class="fa-solid fa-copy text-[10rem] md:text-[14rem] text-transparent bg-clip-text bg-gradient-to-br from-orange-400 to-red-500 relative z-10"></i>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Technology Stack -->
    <section class="py-36 bg-white text-slate-900 border-t border-slate-100">
      <div class="max-w-6xl mx-auto px-6">
        <div class="text-center mb-24">
          <h2 class="text-4xl md:text-[4.5rem] font-bold tracking-tight mb-8 leading-none text-slate-955">Built on a robust foundation.
          </h2>
          <p class="text-xl text-slate-500 font-medium max-w-3xl mx-auto text-balance leading-relaxed">
            We leverage modern, battle-tested technologies to deliver a decoupled, reactive, and hyper-secure platform.
            Performance meets security.
          </p>
        </div>

        <div class="grid grid-cols-2 md:grid-cols-4 gap-6 md:gap-8">
          <div class="bg-[#F8FAFC] p-8 rounded-[32px] border border-slate-100/80 flex flex-col items-center text-center hover:shadow-lg transition-shadow">
            <i class="fa-brands fa-java text-5xl text-[#5382a1] mb-6"></i>
            <h4 class="text-xl font-bold tracking-tight text-slate-955">Java 17</h4>
            <p class="text-slate-400 mt-2 font-semibold text-[13px] uppercase tracking-wider">Robust Backend</p>
          </div>
          <div class="bg-[#F8FAFC] p-8 rounded-[32px] border border-slate-100/80 flex flex-col items-center text-center hover:shadow-lg transition-shadow">
            <i class="fa-solid fa-leaf text-5xl text-[#6db33f] mb-6"></i>
            <h4 class="text-xl font-bold tracking-tight text-slate-955">Spring Boot</h4>
            <p class="text-slate-400 mt-2 font-semibold text-[13px] uppercase tracking-wider">REST API</p>
          </div>
          <div class="bg-[#F8FAFC] p-8 rounded-[32px] border border-slate-100/80 flex flex-col items-center text-center hover:shadow-lg transition-shadow">
            <i class="fa-brands fa-vuejs text-5xl text-[#42b883] mb-6"></i>
            <h4 class="text-xl font-bold tracking-tight text-slate-955">Vue.js 3</h4>
            <p class="text-slate-400 mt-2 font-semibold text-[13px] uppercase tracking-wider">Reactive UI</p>
          </div>
          <div class="bg-[#F8FAFC] p-8 rounded-[32px] border border-slate-100/80 flex flex-col items-center text-center hover:shadow-lg transition-shadow">
            <i class="fa-solid fa-database text-5xl text-[#336791] mb-6"></i>
            <h4 class="text-xl font-bold tracking-tight text-slate-955">PostgreSQL</h4>
            <p class="text-slate-400 mt-2 font-semibold text-[13px] uppercase tracking-wider">Relational Storage</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Call to action -->
    <section class="py-44 bg-[#0B0F19] text-white text-center relative overflow-hidden border-t border-slate-800">
      <div
        class="absolute inset-0 bg-[radial-gradient(ellipse_at_bottom,rgba(249,115,22,0.1)_0,transparent_60%)] pointer-events-none">
      </div>
      <div class="max-w-4xl mx-auto px-6 relative z-10">
        <h2 class="text-5xl md:text-[5.5rem] font-bold tracking-tight mb-8 leading-none font-outfit">Take control of your identity.
        </h2>
        <p class="text-xl md:text-2xl text-slate-400 mb-12 font-medium tracking-tight">
          Open source. Transparent. Uncompromising security.
        </p>
        <router-link to="/signup"
          class="inline-block bg-gradient-to-r from-orange-500 to-orange-600 text-white px-12 py-5 rounded-2xl font-bold text-xl hover:scale-105 transition-transform shadow-[0_10px_30px_rgba(249,115,22,0.3)]">
          Create your Vault
        </router-link>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-white py-16 text-slate-500 text-[14px] border-t border-slate-100 font-medium">
      <div class="max-w-6xl mx-auto px-6 flex flex-col md:flex-row justify-between items-center gap-8">
        <div class="flex items-center gap-2">
          <div class="w-7 h-7 bg-brand-orange rounded-lg flex items-center justify-center text-white shadow-sm">
            <i class="fa-solid fa-folder-open text-xs"></i>
          </div>
          <span class="font-bold text-slate-900 tracking-tight font-outfit text-base">SaveMyPass<span class="text-brand-orange">.dev</span></span>
          <span class="ml-4 text-slate-400 text-[13px]">© 2026 SaveMyPass Team. All rights reserved.</span>
        </div>
        <div class="flex gap-8">
          <a href="#" class="hover:text-brand-orange transition-colors">Security Model</a>
          <a href="#" class="hover:text-brand-orange transition-colors">Privacy Policy</a>
          <a href="https://github.com/leonibeldev" target="_blank"
            class="hover:text-brand-orange transition-colors flex items-center gap-1">
            <i class="fa-brands fa-github text-base"></i> GitHub
          </a>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import PublicNavbar from '@/components/PublicNavbar.vue'
</script>

<style scoped>
/* Apple-like Typography Enforcement */
.apple-landing {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  letter-spacing: -0.015em;
}

/* Animations */
@keyframes scaleUp {
  0% {
    opacity: 0;
    transform: scale(0.96) translateY(20px);
  }

  100% {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.scale-up-anim {
  opacity: 0;
  animation: scaleUp 1.2s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.fade-in-up-anim {
  opacity: 0;
  animation: fadeInUp 1.2s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.delay-100 {
  animation-delay: 150ms;
}

.delay-200 {
  animation-delay: 300ms;
}

.delay-300 {
  animation-delay: 450ms;
}
</style>
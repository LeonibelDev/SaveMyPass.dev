<template>
  <div class="bg-white min-h-screen flex flex-col selection:bg-brand-orange/20 overflow-x-hidden relative font-sans antialiased">

    <!-- background -->
    <div class="absolute top-0 z-0 h-full w-full bg-[radial-gradient(100%_50%_at_50%_0%,rgba(255,107,0,0.05)_0,rgba(255,107,0,0)_50%)] pointer-events-none"></div>

    <PublicNavbar />

    <!-- hero -->
    <main class="relative pt-16 pb-24 z-10 overflow-x-clip">
      <div class="max-w-7xl mx-auto px-6 text-center">
        
        <!-- badge -->
        <div class="flex justify-center mb-8">
          <div class="inline-flex items-center gap-2.5 px-4 py-2 rounded-full border border-slate-100 bg-slate-50/50 text-[13px] font-bold text-slate-500 uppercase tracking-widest animate-fade-in shadow-sm">
            <span class="w-1.5 h-1.5 rounded-full bg-brand-orange animate-pulse"></span>
            Zero-Knowledge Architecture 2.0
          </div>
        </div>

        <div class="max-w-4xl mx-auto mb-16">
          <h1 class="text-5xl sm:text-6xl md:text-[84px] font-bold tracking-tight text-slate-900 leading-[0.95] mb-8">
            The Secure Way to<br>
            <span class="text-transparent bg-clip-text bg-gradient-to-r from-brand-orange to-rose-500">
              Manage Secrets.
            </span>
          </h1>

          <p class="text-lg md:text-[21px] text-slate-500 max-w-2xl mx-auto leading-relaxed font-medium">
            The world's most transparent password manager. We don't just say your data is safe—we make it mathematically impossible for us to see it.
          </p>
        </div>

        <!-- actions -->
        <div class="flex flex-col sm:flex-row items-center justify-center gap-4 mb-24">
          <router-link to="/signup"
            class="w-full sm:w-auto h-14 px-10 bg-slate-900 text-white rounded-2xl flex items-center justify-center gap-3 hover:bg-black transition-all hover:shadow-xl active:scale-95 group">
            <span class="text-[17px] font-bold">Start for Free</span>
            <i class="fa-solid fa-arrow-right text-xs group-hover:translate-x-1 transition-transform"></i>
          </router-link>

          <router-link to="/about"
            class="w-full sm:w-auto h-14 px-10 bg-white border border-slate-200 text-slate-900 rounded-2xl flex items-center justify-center font-bold text-[17px] hover:bg-slate-50 transition-all active:scale-95">
            Learn More
          </router-link>
        </div>

        <!-- mockup -->
        <div class="relative max-w-5xl mx-auto animate-float">
          <div class="absolute -inset-10 bg-gradient-to-tr from-brand-orange/10 via-rose-500/5 to-blue-500/10 rounded-full blur-3xl opacity-50 pointer-events-none"></div>
          
          <div class="relative bg-white/40 backdrop-blur-md border border-slate-200/60 rounded-[32px] p-2 shadow-2xl overflow-hidden">
            <div class="bg-slate-50/50 rounded-[24px] overflow-hidden flex flex-col md:flex-row min-h-[500px] border border-slate-100">
              
              <!-- sidebar mockup -->
              <aside class="hidden md:flex w-60 bg-white/80 backdrop-blur-xl border-r border-slate-100 p-6 flex-col gap-8">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 bg-brand-orange text-white rounded-[10px] flex items-center justify-center shadow-sm">
                    <i class="fa-solid fa-key text-xs"></i>
                  </div>
                  <span class="font-bold text-slate-900 text-[15px] tracking-tight">SaveMyPass</span>
                </div>
                <div class="space-y-1">
                  <div class="px-3 py-2 bg-brand-orange text-white rounded-xl font-bold text-[14px] flex items-center gap-3 shadow-sm">
                    <i class="fa-solid fa-vault"></i> Vault
                  </div>
                  <div class="px-3 py-2 text-slate-400 font-medium text-[14px] flex items-center gap-3">
                    <i class="fa-solid fa-note-sticky"></i> Notes
                  </div>
                  <div class="px-3 py-2 text-slate-400 font-medium text-[14px] flex items-center gap-3">
                    <i class="fa-solid fa-sliders"></i> Settings
                  </div>
                </div>
              </aside>

              <!-- content mockup -->
              <main class="flex-1 flex flex-col bg-white">
                <header class="h-16 border-b border-slate-50 flex items-center px-6">
                  <div class="w-full max-w-xs h-9 bg-slate-100 rounded-xl flex items-center px-4 gap-3">
                    <i class="fa-solid fa-magnifying-glass text-slate-400 text-xs"></i>
                    <span class="text-slate-400 text-[13px] font-medium">Search...</span>
                  </div>
                </header>
                <div class="p-6">
                  <div class="flex items-center justify-between mb-6">
                    <h3 class="text-[19px] font-bold text-slate-900">Recent Logins</h3>
                    <div class="w-8 h-8 rounded-full bg-slate-100"></div>
                  </div>
                  <div class="space-y-2">
                    <div v-for="i in 3" :key="i" class="flex items-center justify-between p-4 rounded-2xl bg-white border border-slate-50 shadow-sm">
                      <div class="flex items-center gap-4">
                        <div class="w-10 h-10 rounded-xl bg-slate-100"></div>
                        <div class="w-24 h-3 bg-slate-100 rounded-full"></div>
                      </div>
                      <div class="w-4 h-4 bg-slate-50 rounded-full"></div>
                    </div>
                  </div>
                </div>
              </main>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- features -->
    <section class="py-32 bg-white relative">
      <div class="max-w-7xl mx-auto px-6">
        <div class="text-center mb-20">
          <h2 class="text-[42px] font-bold text-slate-900 tracking-tight mb-4">Security Redefined</h2>
          <p class="text-slate-500 font-medium max-w-2xl mx-auto text-lg leading-relaxed">
            We combine cutting-edge cryptography with an uncompromising approach to privacy.
          </p>
        </div>
        
        <div class="grid md:grid-cols-3 gap-8">
          <div v-for="f in features" :key="f.title" class="p-10 rounded-[32px] bg-slate-50 border border-slate-100 hover:bg-white hover:shadow-2xl transition-all duration-500 group">
            <div :class="['w-14 h-14 rounded-2xl flex items-center justify-center mb-8 shadow-sm transition-transform group-hover:scale-110', f.bg]">
              <i :class="[f.icon, f.text, 'text-[22px]']"></i>
            </div>
            <h3 class="text-[22px] font-bold text-slate-900 mb-4">{{ f.title }}</h3>
            <p class="text-slate-500 font-medium leading-relaxed">{{ f.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- FAQ -->
    <section class="py-32 bg-[#F2F2F7]">
      <div class="max-w-3xl mx-auto px-6">
        <h2 class="text-[32px] font-bold text-slate-900 text-center mb-16">Common Questions</h2>
        <div class="space-y-3">
          <div v-for="(faq, idx) in faqs" :key="idx" class="bg-white rounded-[20px] overflow-hidden shadow-sm border border-slate-200/60">
            <button @click="activeFaq = activeFaq === idx ? null : idx" class="w-full px-6 py-5 text-left flex justify-between items-center group">
              <span class="text-[16px] font-bold text-slate-800">{{ faq.q }}</span>
              <i class="fa-solid fa-chevron-down text-[12px] text-slate-300 transition-transform" :class="activeFaq === idx ? 'rotate-180' : ''"></i>
            </button>
            <div v-if="activeFaq === idx" class="px-6 pb-6">
              <p class="text-[15px] text-slate-500 leading-relaxed font-medium">{{ faq.a }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- footer -->
    <footer class="bg-white py-16 px-6 border-t border-slate-100">
      <div class="max-w-7xl mx-auto flex flex-col md:flex-row justify-between items-center gap-10">
        <div class="flex items-center gap-3">
          <div class="w-8 h-8 bg-slate-900 text-white rounded-lg flex items-center justify-center">
            <i class="fa-solid fa-key text-xs"></i>
          </div>
          <span class="font-bold text-slate-900 tracking-tight">SaveMyPass.dev</span>
        </div>
        <div class="flex gap-8 text-[14px] font-semibold text-slate-400">
          <a href="#" class="hover:text-brand-orange transition-colors">Security</a>
          <a href="#" class="hover:text-brand-orange transition-colors">Privacy</a>
          <a href="#" class="hover:text-brand-orange transition-colors">GitHub</a>
        </div>
        <div class="text-[13px] font-medium text-slate-300">
          © 2026 SaveMyPass Team.
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PublicNavbar from '@/components/PublicNavbar.vue'

const activeFaq = ref(null)

const features = [
  { title: 'AES-256 Encryption', desc: 'Encrypted locally. We never see your data.', icon: 'fa-solid fa-microchip', bg: 'bg-orange-50', text: 'text-orange-500' },
  { title: 'Zero-Knowledge', desc: 'Only you can decrypt your vault.', icon: 'fa-solid fa-user-shield', bg: 'bg-emerald-50', text: 'text-emerald-500' },
  { title: 'Open Source', desc: 'Transparent and verifiable code.', icon: 'fa-solid fa-code', bg: 'bg-indigo-50', text: 'text-indigo-500' }
]

const faqs = [
  { q: "Is SaveMyPass really secure?", a: "Yes. We use a Zero-Knowledge architecture. Your data is encrypted locally on your device." },
  { q: "What if I forget my password?", a: "We cannot reset your Master Password. If you lose it, your data is permanently inaccessible." },
  { q: "Can I export my data?", a: "Absolutely. You can export your entire vault as a secure PDF, CSV, or JSON at any time." },
  { q: "How much does it cost?", a: "SaveMyPass is free for personal use. Privacy is a right, not a luxury." }
]
</script>

<style scoped>
.animate-fade-in { animation: fadeIn 0.8s ease-out; }
.animate-float { animation: float 6s ease-in-out infinite; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
@keyframes float { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-10px); } }
</style>
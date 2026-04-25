<template>
  <div
    class="bg-white min-h-screen flex flex-col text-slate-900 selection:bg-orange-100 selection:text-orange-950 overflow-x-hidden relative font-sans antialiased">

    <!-- subtle background -->
    <div
      class="absolute top-0 z-[-1] h-full w-full bg-[radial-gradient(80%_40%_at_50%_0%,rgba(0,163,255,0.08)_0,rgba(0,163,255,0)_60%)]">
    </div>

    <PublicNavbar />

    <main class="relative pt-20 pb-20">
      <div class="max-w-7xl mx-auto px-6 relative z-10">

        <!-- badge -->
        <div class="flex justify-center mb-8">
          <div
            class="inline-flex items-center gap-2 px-4 py-2 rounded-full border border-orange-100 bg-orange-50 text-xs font-semibold text-orange-600">
            <span class="w-2 h-2 rounded-full bg-orange-500"></span>
            Zero-Knowledge 2.0 Released
          </div>
        </div>

        <!-- hero -->
        <div class="text-center max-w-4xl mx-auto mb-12">
          <h1 class="text-5xl md:text-7xl font-black tracking-tight text-slate-950 leading-tight mb-6">
            Secure your secrets.<br>
            <span class="text-transparent bg-clip-text bg-gradient-to-r from-orange-500 to-rose-500">
              Trust the math.
            </span>
          </h1>

          <p class="text-base md:text-xl text-slate-500 max-w-xl mx-auto">
            An open-source password manager where the server never sees your keys.
          </p>
        </div>

        <!-- actions -->
        <div class="flex flex-col sm:flex-row items-center justify-center gap-4 mb-16">
          <router-link to="/signup"
            class="w-full sm:w-auto h-14 px-8 bg-slate-950 text-white rounded-2xl flex items-center justify-center gap-2 hover:bg-slate-800 transition">
            <span class="text-sm font-semibold">Build your Vault</span>
            <i class="fa-solid fa-arrow-right text-xs"></i>
          </router-link>

          <router-link to="/about"
            class="w-full sm:w-auto h-14 px-8 bg-white border border-slate-200 text-slate-900 rounded-2xl flex items-center justify-center gap-2 hover:bg-slate-50 transition text-sm font-semibold">
            <i class="fa-solid fa-shield-halved text-slate-400"></i>
            Code Audit
          </router-link>
        </div>

        <!-- mockup -->
        <div class="relative max-w-5xl mx-auto">
          <div
            class="absolute -inset-4 bg-gradient-to-tr from-orange-200/30 to-blue-200/30 rounded-3xl blur-2xl opacity-40">
          </div>

          <div class="relative bg-white border border-slate-200 rounded-3xl p-2 shadow-xl overflow-hidden">

            <div
              class="bg-white rounded-2xl overflow-hidden flex flex-col md:flex-row min-h-[500px] border border-slate-100">

              <!-- sidebar -->
              <aside class="hidden md:flex w-60 bg-slate-50 border-r border-slate-100 p-6 flex-col justify-between">
                <div class="space-y-8">

                  <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-orange-500 text-white rounded-lg flex items-center justify-center">
                      <i class="fa-solid fa-key text-xs"></i>
                    </div>
                    <span class="font-semibold text-sm">My Vault</span>
                  </div>

                  <nav class="space-y-1 text-sm">
                    <div class="px-3 py-2 bg-orange-100 text-orange-600 rounded-lg font-medium">
                      All Items
                    </div>
                    <div
                      class="px-3 py-2 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-lg cursor-pointer">
                      Security Audit
                    </div>
                    <div
                      class="px-3 py-2 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-lg cursor-pointer">
                      Sharing
                    </div>
                  </nav>
                </div>
              </aside>

              <!-- content -->
              <main class="flex-1 flex flex-col">

                <!-- header -->
                <header class="h-16 border-b border-slate-100 flex items-center px-6 justify-between">

                  <div class="relative w-full max-w-xs">
                    <i
                      class="fa-solid fa-magnifying-glass absolute left-3 top-1/2 -translate-y-1/2 text-slate-300 text-xs"></i>
                    <input type="text" placeholder="Search..."
                      class="w-full bg-slate-50 border border-slate-100 rounded-lg py-2 pl-9 text-sm focus:outline-none" />
                  </div>

                  <button
                    class="ml-4 px-4 py-2 bg-slate-900 text-white rounded-lg text-sm hover:bg-orange-500 transition">
                    + New
                  </button>
                </header>

                <!-- list -->
                <section class="flex-1 p-6 overflow-y-auto">

                  <div class="flex justify-between mb-6">
                    <h3 class="text-lg font-semibold">Recent Items</h3>
                    <span class="text-xs text-slate-400">35 records</span>
                  </div>

                  <div class="space-y-2">
                    <div v-for="(entry, idx) in demoEntries" :key="idx"
                      class="flex items-center justify-between p-3 rounded-xl hover:bg-slate-50 transition cursor-pointer">

                      <div class="flex items-center gap-3">
                        <div :class="['w-10 h-10 rounded-lg flex items-center justify-center', entry.color]">
                          <i :class="['fa-brands', entry.icon]"></i>
                        </div>
                        <div>
                          <p class="font-medium text-sm">{{ entry.name }}</p>
                          <p class="text-xs text-slate-400">{{ entry.user }}</p>
                        </div>
                      </div>

                      <span class="text-xs text-slate-400 hidden sm:block">{{ entry.date }}</span>
                    </div>
                  </div>

                </section>
              </main>

            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- features -->
    <section class="bg-slate-50 py-20 border-y border-slate-100">
      <div class="max-w-7xl mx-auto px-6 grid md:grid-cols-3 gap-6">

        <div v-for="f in features" :key="f.title"
          class="bg-white p-8 rounded-2xl border border-slate-100 hover:shadow-lg transition">

          <div :class="['w-12 h-12 rounded-xl flex items-center justify-center mb-6', f.bg]">
            <i :class="[f.icon, f.text]"></i>
          </div>

          <h3 class="text-lg font-semibold mb-2">{{ f.title }}</h3>
          <p class="text-sm text-slate-500">{{ f.desc }}</p>
        </div>

      </div>
    </section>

    <!-- footer -->
    <footer class="bg-white py-10 px-6">
      <div
        class="max-w-7xl mx-auto flex flex-col md:flex-row justify-between items-center gap-6 text-sm text-slate-400">
        <span>© 2026 SaveMyPass</span>
        <div class="flex gap-6">
          <a href="#" class="hover:text-orange-500">GitHub</a>
          <a href="#" class="hover:text-orange-500">Twitter</a>
        </div>
      </div>
    </footer>

  </div>
</template>

<script setup>
import PublicNavbar from '@/components/PublicNavbar.vue'

const demoEntries = [
  { name: 'Google Workspace', user: 'alex.dev@gmail.com', date: '2 mins ago', icon: 'fa-google', color: 'text-blue-500 bg-blue-50' },
  { name: 'YouTube', user: 'family-plan@google.com', date: '1 hour ago', icon: 'fa-youtube', color: 'text-rose-500 bg-rose-50' },
  { name: 'GitHub', user: 'github-bot', date: 'Oct 12', icon: 'fa-github', color: 'text-slate-900 bg-slate-100' },
  { name: 'AWS', user: 'cloud-root', date: 'Oct 08', icon: 'fa-aws', color: 'text-orange-500 bg-orange-50' }
]

const features = [
  { title: 'AES-256 Encryption', desc: 'Encrypted locally. We never see your data.', icon: 'fa-solid fa-microchip', bg: 'bg-orange-50', text: 'text-orange-500' },
  { title: 'Zero-Knowledge', desc: 'Only you can decrypt your vault.', icon: 'fa-solid fa-user-shield', bg: 'bg-emerald-50', text: 'text-emerald-500' },
  { title: 'Open Source', desc: 'Transparent and verifiable.', icon: 'fa-solid fa-code', bg: 'bg-indigo-50', text: 'text-indigo-500' }
]
</script>
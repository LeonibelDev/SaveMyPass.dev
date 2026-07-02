<template>
  <div class="fixed inset-0 z-[100] flex items-center justify-center p-4 sm:p-6">
    <!-- Backdrop -->
    <div class="absolute inset-0 bg-slate-900/60 backdrop-blur-md transition-opacity"></div>

    <!-- Modal Content -->
    <div class="relative w-full max-w-lg bg-white rounded-[32px] shadow-2xl overflow-hidden animate-in fade-in zoom-in duration-300">
      <div class="p-8 sm:p-10 text-center">
        
        <!-- Step 1: Explanation -->
        <div v-if="step === 1" class="animate-fade-in">
          <div class="w-20 h-20 bg-brand-orange/10 text-brand-orange flex items-center justify-center rounded-[24px] mx-auto mb-8">
            <i class="fa-solid fa-shield-halved text-4xl"></i>
          </div>
          <h2 class="text-2xl font-bold text-slate-900 mb-4 tracking-tight">Security Upgrade Required</h2>
          <p class="text-[15px] text-slate-500 font-medium leading-relaxed mb-8">
            We've upgraded our encryption architecture to support secure password resets. 
            We need to migrate your vault to use a new <strong>Master Vault Key</strong>. 
            No action is needed on your part, but you will receive a <strong>Recovery Key</strong> to keep in a safe place.
          </p>
          <button @click="startMigration" :disabled="loading"
            class="w-full bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-4 rounded-2xl transition-all active:scale-[0.98] shadow-sm flex items-center justify-center gap-3">
            <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
            {{ loading ? 'Migrating Vault...' : 'Upgrade Now' }}
          </button>
        </div>

        <!-- Step 2: Show Recovery Key -->
        <div v-if="step === 2" class="animate-fade-in">
          <div class="w-20 h-20 bg-emerald-50 text-emerald-500 flex items-center justify-center rounded-[24px] mx-auto mb-8">
            <i class="fa-solid fa-key text-4xl"></i>
          </div>
          <h2 class="text-2xl font-bold text-slate-900 mb-4 tracking-tight">Migration Successful!</h2>
          <p class="text-[15px] text-slate-500 font-medium leading-relaxed mb-6">
            This is your <strong>Recovery Key</strong>. If you ever forget your master password, you will need this code to regain access to your data.
          </p>
          
          <div class="bg-slate-50 border-2 border-dashed border-slate-200 rounded-2xl p-6 mb-6 group relative">
            <span class="text-xl font-mono font-bold text-slate-800 tracking-wider break-all">
              {{ recoveryKey }}
            </span>
            <button @click="copyKey" 
              class="absolute top-2 right-2 p-2 text-slate-400 hover:text-brand-orange transition-colors">
              <i class="fa-solid" :class="copied ? 'fa-check text-emerald-500' : 'fa-copy'"></i>
            </button>
          </div>

          <div class="flex items-start gap-3 text-left p-4 bg-rose-50 rounded-2xl border border-rose-100 mb-8">
            <i class="fa-solid fa-triangle-exclamation text-rose-500 mt-1"></i>
            <p class="text-[13px] text-rose-600 font-semibold leading-relaxed">
              Save this code immediately. We do not store it, and we cannot recover it for you if lost.
            </p>
          </div>

          <button @click="finish" :disabled="!copied"
            class="w-full bg-slate-900 hover:bg-slate-800 text-white font-bold py-4 rounded-2xl transition-all active:scale-[0.98] disabled:opacity-40">
            I've saved my recovery key
          </button>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useVaultStore } from '@/stores/vault'

const vault = useVaultStore()
const step = ref(1)
const loading = ref(false)
const recoveryKey = ref('')
const copied = ref(false)

async function startMigration() {
  loading.value = true
  try {
    const key = await vault.migrateToVaultKey()
    recoveryKey.value = key
    step.value = 2
  } catch (e) {
    alert("Migration failed. Please try again later.")
  } finally {
    loading.value = false
  }
}

async function copyKey() {
  await navigator.clipboard.writeText(recoveryKey.value)
  copied.value = true
}

function finish() {
  window.location.reload() // Reload to ensure all state is fresh with the new VaultKey
}
</script>

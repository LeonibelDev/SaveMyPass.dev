<template>
  <div class="fixed inset-0 z-[60] flex items-center justify-center p-6">
    <Transition name="fade">
      <div class="absolute inset-0 bg-black/20 backdrop-blur-sm" @click="$emit('close')" />
    </Transition>

    <div class="relative w-full max-w-[400px] animate-scale-in">
      <div class="bg-white/90 backdrop-blur-xl rounded-[28px] p-8 shadow-2xl border border-white/20">

        <h2 class="text-[22px] font-bold text-slate-900 tracking-tight mb-1 text-center">Edit Credential</h2>
        <p class="text-[14px] text-slate-500 font-medium mb-8 text-center">
          Update the info for this account
        </p>

        <!-- Error -->
        <div v-if="error"
          class="mb-6 p-3 bg-rose-50 border border-rose-100 rounded-xl text-rose-500 text-[13px] font-semibold text-center">
          {{ error }}
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-4">

          <div class="space-y-1">
            <label class="block text-[12px] font-bold text-slate-400 ml-1">Platform</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                <img v-if="form.site"
                  :src="`https://t2.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=http://${form.site}&size=64`"
                  :alt="form.site" class="w-5 h-5 object-contain" @error="$event.target.style.display = 'none'" />
                <i v-else class="fa-solid fa-globe text-[14px]"></i>
              </span>
              <input v-model="form.site" type="text" required placeholder="e.g. GitHub.com"
                class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all" />
            </div>
          </div>

          <div class="space-y-1">
            <label class="block text-[12px] font-bold text-slate-400 ml-1">Username / Email</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-at text-[14px]"></i>
              </span>
              <input v-model="form.username" type="text" required placeholder="john@example.com"
                class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-4 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all" />
            </div>
          </div>

          <div class="space-y-1">
            <label class="block text-[12px] font-bold text-slate-400 ml-1">Password</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 pointer-events-none">
                <i class="fa-solid fa-lock text-[14px]"></i>
              </span>
              <input v-model="form.password" :type="showPw ? 'text' : 'password'" required placeholder="Your password"
                class="w-full bg-slate-100/50 border-none rounded-xl pl-10 pr-24 py-3 text-[15px] font-medium text-slate-900 focus:bg-white focus:ring-4 focus:ring-brand-orange/10 focus:outline-none transition-all" />
              <div class="absolute inset-y-0 right-0 flex items-center gap-1 pr-2">
                <button type="button" @click="showPw = !showPw"
                  class="p-2 text-slate-400 hover:text-brand-orange transition-colors">
                  <i :class="showPw ? 'fa-solid fa-eye-slash' : 'fa-solid fa-eye'" class="text-[14px]"></i>
                </button>
              </div>
            </div>
          </div>

          <button type="submit" :disabled="loading"
            class="w-full mt-4 bg-brand-orange hover:bg-brand-orange-hover text-white font-bold py-3.5 rounded-xl transition-all shadow-sm active:scale-[0.98] flex items-center justify-center gap-2 text-[15px] disabled:opacity-60">
            <i v-if="loading" class="fa-solid fa-circle-notch animate-spin"></i>
            {{ loading ? 'Saving…' : 'Save Changes' }}
          </button>

          <button type="button" @click="$emit('close')"
            class="w-full py-2 text-brand-orange font-semibold text-[15px] hover:opacity-70 transition-opacity">
            Cancel
          </button>

        </form>

      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useVaultStore } from '@/stores/vault'

const props = defineProps({ credential: { type: Object, required: true } })
const emit = defineEmits(['close'])
const vault = useVaultStore()

const form = reactive({
  site: props.credential.site,
  username: props.credential.username,
  password: props.credential.password,
})
const loading = ref(false)
const error = ref(null)
const showPw = ref(false)

async function handleSubmit() {
  loading.value = true
  error.value = null
  try {
    await vault.updateCredential(props.credential.id, form.site, form.username, form.password)
    emit('close')
  } catch (e) {
    error.value = e?.response?.data?.message || 'Failed to update credential.'
  } finally {
    loading.value = false
  }
}
</script>

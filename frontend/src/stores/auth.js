import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { exportKey, importKey } from '@/crypto/aes'
import router from '../router'

const SESSION_KEY = 'smp_aeskey'   // sessionStorage key for the AES key bytes
const LOCAL_TOKEN = 'smp_token'
const LOCAL_USER = 'smp_user'

export const useAuthStore = defineStore('auth', () => {
  // JWT persisted in localStorage (survives browser restarts)
  const token = ref(localStorage.getItem(LOCAL_TOKEN) || null)

  // User metadata persisted in localStorage
  const _user = JSON.parse(localStorage.getItem(LOCAL_USER) || 'null')
  const userId = ref(_user?.userId || null)
  const email = ref(_user?.email || null)
  const name = ref(_user?.name || null)
  const salt = ref(_user?.salt || null)

  // AES CryptoKey — lives in memory; raw bytes cached in sessionStorage
  // so page refreshes don't force re-login, but closing the tab/browser does.
  const aesKey = ref(null)

  const isAuthenticated = computed(() => !!token.value && !!aesKey.value)

  /**
   * Try to restore the AES key from sessionStorage on app start.
   * Call this once in main.js / App.vue before the router resolves.
   */
  async function restoreSession() {
    const stored = sessionStorage.getItem(SESSION_KEY)
    if (stored && token.value) {
      try {
        aesKey.value = await importKey(stored)
      } catch {
        // Corrupted — force re-login
        logout()
      }
    }
  }

  async function setSession(authData, cryptoKey) {
    token.value = authData.token
    userId.value = authData.userId
    email.value = authData.email
    name.value = authData.name
    salt.value = authData.salt
    aesKey.value = cryptoKey

    localStorage.setItem(LOCAL_TOKEN, authData.token)
    localStorage.setItem(LOCAL_USER, JSON.stringify({
      userId: authData.userId,
      email: authData.email,
      name: authData.name,
      salt: authData.salt,
    }))

    // Persist key bytes to sessionStorage so refreshes don't require re-login
    const exported = await exportKey(cryptoKey)
    sessionStorage.setItem(SESSION_KEY, exported)

    setTimeout(() => {
      logout()
      router.push('/login')
    }, 1 * 60 * 60 * 1000)
  }

  async function updateKey(newAuthData, newCryptoKey) {
    token.value = newAuthData.token
    salt.value = newAuthData.salt
    aesKey.value = newCryptoKey

    localStorage.setItem(LOCAL_TOKEN, newAuthData.token)
    const user = JSON.parse(localStorage.getItem(LOCAL_USER) || '{}')
    localStorage.setItem(LOCAL_USER, JSON.stringify({ ...user, salt: newAuthData.salt }))

    const exported = await exportKey(newCryptoKey)
    sessionStorage.setItem(SESSION_KEY, exported)
  }

  function logout() {
    token.value = null
    userId.value = null
    email.value = null
    name.value = null
    salt.value = null
    aesKey.value = null
    localStorage.removeItem(LOCAL_TOKEN)
    localStorage.removeItem(LOCAL_USER)
    sessionStorage.removeItem(SESSION_KEY)
  }

  return {
    token, userId, email, name, salt, aesKey,
    isAuthenticated,
    restoreSession, setSession, updateKey, logout,
  }
})

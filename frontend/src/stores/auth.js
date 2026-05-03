import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { exportKey, importKey } from '@/crypto/aes'
import router from '../router'

const SESSION_KEY = 'smp_aeskey'   // sessionStorage key for the AES key bytes
const LOCAL_TOKEN = 'smp_token'
const LOCAL_USER = 'smp_user'
const SESSION_EXPIRY = 'smp_expiry'

// Create a broadcast channel to synchronize session across tabs
const syncChannel = new BroadcastChannel('smp_auth_sync')

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
  const aesKey = ref(null)

  const isAuthenticated = computed(() => !!token.value && !!aesKey.value)

  // Listen for session updates from other tabs
  syncChannel.onmessage = async (event) => {
    const { type, payload } = event.data
    if (type === 'SESSION_UPDATE') {
      token.value = payload.token
      userId.value = payload.userId
      email.value = payload.email
      name.value = payload.name
      salt.value = payload.salt
      aesKey.value = await importKey(payload.exportedKey)

      sessionStorage.setItem(SESSION_KEY, payload.exportedKey)
      sessionStorage.setItem(SESSION_EXPIRY, payload.expiry)

      localStorage.setItem(LOCAL_TOKEN, payload.token)
      localStorage.setItem(LOCAL_USER, JSON.stringify({
        userId: payload.userId,
        email: payload.email,
        name: payload.name,
        salt: payload.salt,
      }))

      setupTimeout(payload.expiry)
    } else if (type === 'LOGOUT') {
      _logoutInternal()
    }
  }

  let logoutTimer = null

  function setupTimeout(expiryTime) {
    if (logoutTimer) clearTimeout(logoutTimer)
    const now = Date.now()
    const remaining = expiryTime - now
    if (remaining <= 0) {
      logout()
    } else {
      logoutTimer = setTimeout(() => {
        logout()
        router.push('/login')
      }, remaining)
    }
  }

  /**
   * Try to restore the AES key from sessionStorage on app start.
   */
  async function restoreSession() {
    const stored = sessionStorage.getItem(SESSION_KEY)
    const expiry = sessionStorage.getItem(SESSION_EXPIRY)

    if (stored && token.value) {
      try {
        aesKey.value = await importKey(stored)
        if (expiry) {
          setupTimeout(parseInt(expiry))
        }
      } catch {
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

    const expiry = Date.now() + (1 * 60 * 60 * 1000) // 1 hour from now

    localStorage.setItem(LOCAL_TOKEN, authData.token)
    localStorage.setItem(LOCAL_USER, JSON.stringify({
      userId: authData.userId,
      email: authData.email,
      name: authData.name,
      salt: authData.salt,
    }))

    const exported = await exportKey(cryptoKey)
    sessionStorage.setItem(SESSION_KEY, exported)
    sessionStorage.setItem(SESSION_EXPIRY, expiry.toString())

    syncChannel.postMessage({
      type: 'SESSION_UPDATE',
      payload: { ...authData, exportedKey: exported, expiry }
    })

    setupTimeout(expiry)
  }

  async function updateKey(newAuthData, newCryptoKey) {
    token.value = newAuthData.token
    salt.value = newAuthData.salt
    aesKey.value = newCryptoKey

    localStorage.setItem(LOCAL_TOKEN, newAuthData.token)
    const user = JSON.parse(localStorage.getItem(LOCAL_USER) || '{}')
    const updatedUser = { ...user, salt: newAuthData.salt }
    localStorage.setItem(LOCAL_USER, JSON.stringify(updatedUser))

    const exported = await exportKey(newCryptoKey)
    sessionStorage.setItem(SESSION_KEY, exported)

    const expiry = sessionStorage.getItem(SESSION_EXPIRY) || (Date.now() + (1 * 60 * 60 * 1000)).toString()

    syncChannel.postMessage({
      type: 'SESSION_UPDATE',
      payload: {
        token: newAuthData.token,
        salt: newAuthData.salt,
        userId: userId.value,
        email: email.value,
        name: name.value,
        exportedKey: exported,
        expiry: parseInt(expiry)
      }
    })
  }

  function _logoutInternal() {
    if (logoutTimer) clearTimeout(logoutTimer)
    token.value = null
    userId.value = null
    email.value = null
    name.value = null
    salt.value = null
    aesKey.value = null
    localStorage.removeItem(LOCAL_TOKEN)
    localStorage.removeItem(LOCAL_USER)
    sessionStorage.removeItem(SESSION_KEY)
    sessionStorage.removeItem(SESSION_EXPIRY)
  }

  function logout(broadcast = true) {
    _logoutInternal()
    if (broadcast) {
      syncChannel.postMessage({ type: 'LOGOUT' })
    }
  }

  return {
    token, userId, email, name, salt, aesKey,
    isAuthenticated,
    restoreSession, setSession, updateKey, logout,
  }
})

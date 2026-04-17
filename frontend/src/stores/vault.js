import { defineStore } from 'pinia'
import { ref } from 'vue'
import { credentialsApi } from '@/api/credentials'
import { useAuthStore } from './auth'
import { decrypt, encrypt } from '@/crypto/aes'
import { jsPDF } from 'jspdf'
import autoTable from 'jspdf-autotable'

export const useVaultStore = defineStore('vault', () => {
  const credentials = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchCredentials() {
    const auth = useAuthStore()
    loading.value = true
    error.value = null

    try {
      const res = await credentialsApi.list()
      const rawList = res.data.data

      // Decrypt each password locally using the in-memory AES key
      credentials.value = await Promise.all(
        rawList.map(async (item) => {
          try {
            return {
              ...item,
              password: await decrypt(auth.aesKey, item.password),
            }
          } catch (e) {
            console.error(`Decryption failed for item ${item.site}:`, e)
            return {
              ...item,
              password: '[DECRYPTION FAILED - INVALID KEY]',
            }
          }
        })
      )
    } catch (e) {
      error.value = e?.response?.data?.message || 'Failed to load credentials'
    } finally {
      loading.value = false
    }
  }

  async function addCredential(site, username, plainPassword) {
    const auth = useAuthStore()
    const encryptedPassword = await encrypt(auth.aesKey, plainPassword)

    const res = await credentialsApi.add({ site, username, password: encryptedPassword })
    const newItem = res.data.data

    credentials.value.push({
      ...newItem,
      password: plainPassword,  // show decrypted in UI
    })
  }

  async function updateCredential(id, site, username, plainPassword) {
    const auth = useAuthStore()
    const encryptedPassword = await encrypt(auth.aesKey, plainPassword)

    await credentialsApi.update(id, { site, username, password: encryptedPassword })

    const idx = credentials.value.findIndex((c) => c.id === id)
    if (idx !== -1) {
      credentials.value[idx] = { ...credentials.value[idx], site, username, password: plainPassword }
    }
  }

  async function removeCredential(id) {
    await credentialsApi.remove(id)
    credentials.value = credentials.value.filter((c) => c.id !== id)
  }


  async function exportVault() {
    const doc = new jsPDF()
    const auth = useAuthStore()

    doc.setFontSize(18)
    doc.text('SaveMyPass.dev - Password Export', 14, 22)
    doc.setFontSize(11)
    doc.setTextColor(100)
    doc.text(`User: ${auth.name || 'User'} (${auth.email || 'N/A'})`, 14, 30)
    doc.text(`Exported: ${new Date().toLocaleString()}`, 14, 36)

    const tableData = credentials.value.map((c) => [
      c.site,
      c.username,
      c.password,
    ])

    autoTable(doc, {
      startY: 42,
      head: [['Site', 'Username', 'Password']],
      body: tableData,
      theme: 'striped',
      headStyles: { fillColor: [249, 115, 22] }, // orange-500
    })

    doc.save('savemypass-export.pdf')
  }

  return {
    credentials, loading, error,
    fetchCredentials, addCredential, updateCredential, removeCredential, exportVault,
  }
})

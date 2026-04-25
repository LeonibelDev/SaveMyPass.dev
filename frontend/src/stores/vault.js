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

  async function exportVaultCSV() {
    const escapeCSV = (value) => {
      if (value === null || value === undefined) return "";
      const strValue = String(value);
      // If it contains quotes, commas, or newlines, wrap in quotes and escape internal quotes
      if (/[",\n\r]/.test(strValue)) {
        return `"${strValue.replace(/"/g, '""')}"`;
      }
      return strValue;
    };

    const rows = [
      ['Site', 'Username', 'Password'], // Header
      ...credentials.value.map(c => [
        escapeCSV(c.site),
        escapeCSV(c.username),
        escapeCSV(c.password)
      ])
    ];

    const csvContent = rows.map(row => row.join(',')).join('\n');

    // Use \ufeff (BOM) to force Excel to read UTF-8 correctly
    const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = `savemypass-vault-${new Date().toISOString().split('T')[0]}.csv`;

    document.body.appendChild(link); // Better practice for cross-browser compatibility
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(url);
  }

  async function decryptPassword(encryptedText) {
    const auth = useAuthStore()
    try {
      return await decrypt(auth.aesKey, encryptedText)
    } catch (e) {
      console.error('Decryption failed:', e)
      return '[DECRYPTION FAILED]'
    }
  }

  return {
    credentials, loading, error, exportVaultCSV,
    fetchCredentials, addCredential, updateCredential, removeCredential, exportVault, decryptPassword
  }
})

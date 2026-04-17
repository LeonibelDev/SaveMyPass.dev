<template>
  <router-view />
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'

// Restore AES key from sessionStorage on every page load/refresh.
// This runs synchronously before any route component renders.
const auth = useAuthStore()
auth.restoreSession()
</script>

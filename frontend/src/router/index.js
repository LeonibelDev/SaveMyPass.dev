import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/',
    name: 'Landing',
    component: () => import('@/views/LandingView.vue'),
    meta: { guest: true },
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/AboutView.vue'),
    meta: {}, // public
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { guest: true },
  },
  {
    path: '/signup',
    name: 'Signup',
    component: () => import('@/views/SignupView.vue'),
    meta: { guest: true },
  },
  {
    path: '/vault',
    name: 'Vault',
    component: () => import('@/views/HomeView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/notes',
    name: 'Notes',
    component: () => import('@/views/NotesView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/export',
    name: 'Export',
    component: () => import('@/views/ExportView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/ProfileView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/credentials/:id',
    name: 'CredentialDetail',
    component: () => import('@/views/CredentialDetailView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/notes/new',
    name: 'NewNote',
    component: () => import('@/views/NewNoteView.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/notes/:id',
    name: 'NoteDetail',
    component: () => import('@/views/NoteDetailView.vue'),
    meta: { requiresAuth: true },
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes,
})

// Run once — tracks whether restoreSession has been awaited already
let sessionRestored = false

router.beforeEach(async (to) => {
  const auth = useAuthStore()

  // On first navigation (including page refresh), restore the AES key
  // from sessionStorage before evaluating auth guards.
  if (!sessionRestored) {
    await auth.restoreSession()
    sessionRestored = true
  }

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'Login' }
  }

  if (to.meta.guest && auth.isAuthenticated) {
    return { name: 'Vault' }
  }
})

export default router

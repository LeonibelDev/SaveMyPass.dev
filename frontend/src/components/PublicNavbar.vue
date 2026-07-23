<template>
  <!-- Floating island navbar -->
  <div class="navbar-island-container">
    <nav class="navbar-island" :class="{ 'navbar-island--scrolled': isScrolled }">

      <!-- Logo -->
      <router-link to="/" @click="isMenuOpen = false" class="nav-logo">
        <div class="nav-logo-icon">
          <i class="fa-solid fa-folder-open"></i>
        </div>
        <span class="nav-logo-text">SaveMyPass<span class="nav-logo-accent">.dev</span></span>
      </router-link>

      <!-- Desktop Links -->
      <div class="nav-links">
        <router-link v-if="isLanding" to="/about" class="nav-link">
          <i class="fa-solid fa-shield-halved nav-link-icon"></i>
          Security
        </router-link>
        <router-link to="/login" class="nav-link">
          Log In
        </router-link>
        <router-link to="/signup" class="nav-cta">
          Get Started
          <i class="fa-solid fa-arrow-right text-[11px]"></i>
        </router-link>
      </div>

      <!-- Mobile toggle -->
      <button class="mobile-toggle" @click="isMenuOpen = !isMenuOpen" :aria-label="isMenuOpen ? 'Close menu' : 'Open menu'">
        <i class="fa-solid" :class="isMenuOpen ? 'fa-xmark' : 'fa-bars-staggered'"></i>
      </button>
    </nav>

    <!-- Mobile dropdown island -->
    <Transition name="mobile-drop">
      <div v-if="isMenuOpen" class="mobile-island">
        <router-link v-if="isLanding" to="/about" @click="isMenuOpen = false" class="mobile-link">
          <i class="fa-solid fa-shield-halved"></i>
          Security
          <i class="fa-solid fa-chevron-right mobile-link-arrow"></i>
        </router-link>
        <router-link to="/login" @click="isMenuOpen = false" class="mobile-link">
          <i class="fa-solid fa-right-to-bracket"></i>
          Log In
          <i class="fa-solid fa-chevron-right mobile-link-arrow"></i>
        </router-link>
        <div class="mobile-link-divider"></div>
        <router-link to="/signup" @click="isMenuOpen = false" class="mobile-cta">
          <i class="fa-solid fa-bolt"></i>
          Get Started — it's free
        </router-link>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isLanding = computed(() => route.name === 'Landing')
const isMenuOpen = ref(false)
const isScrolled = ref(false)

function handleScroll() {
  isScrolled.value = window.scrollY > 20
}

onMounted(() => window.addEventListener('scroll', handleScroll, { passive: true }))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<style scoped>
/* ─── Outer container ─────────────────────────────────────── */
.navbar-island-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 20px 0;
  pointer-events: none;
}

/* ─── Island nav ─────────────────────────────────────────── */
.navbar-island {
  pointer-events: all;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 860px;
  padding: 0 8px 0 14px;
  height: 52px;
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(226, 232, 240, 0.7);
  border-radius: 16px;
  box-shadow:
    0 4px 20px rgba(0, 0, 0, 0.06),
    0 1px 4px rgba(0, 0, 0, 0.04),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
  transition: box-shadow 0.3s ease, background 0.3s ease;
}

.navbar-island--scrolled {
  background: rgba(255, 255, 255, 0.93);
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 2px 8px rgba(0, 0, 0, 0.06),
    inset 0 1px 0 rgba(255, 255, 255, 1);
}

/* ─── Logo ───────────────────────────────────────────────── */
.nav-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  transition: transform 0.2s ease;
}

.nav-logo:active {
  transform: scale(0.96);
}

.nav-logo-icon {
  width: 30px;
  height: 30px;
  background: linear-gradient(135deg, #f97316, #ea580c);
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  box-shadow: 0 2px 8px rgba(249, 115, 22, 0.25);
  transition: transform 0.2s ease;
}

.nav-logo:hover .nav-logo-icon {
  transform: rotate(4deg);
}

.nav-logo-text {
  font-family: 'Outfit', sans-serif;
  font-size: 15px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
}

.nav-logo-accent {
  color: #f97316;
}

/* ─── Desktop links ──────────────────────────────────────── */
.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
}

@media (max-width: 640px) {
  .nav-links {
    display: none;
  }
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 10px;
  font-size: 13.5px;
  font-weight: 600;
  color: #64748b;
  text-decoration: none;
  transition: color 0.15s, background 0.15s;
}

.nav-link:hover {
  color: #0f172a;
  background: rgba(15, 23, 42, 0.05);
}

.nav-link-icon {
  font-size: 11px;
}

.nav-cta {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 7px 16px;
  margin-left: 4px;
  border-radius: 11px;
  font-size: 13px;
  font-weight: 700;
  color: white;
  text-decoration: none;
  background: linear-gradient(135deg, #f97316, #ea580c);
  box-shadow: 0 3px 10px rgba(249, 115, 22, 0.25);
  transition: box-shadow 0.2s, transform 0.15s;
}

.nav-cta:hover {
  box-shadow: 0 6px 18px rgba(249, 115, 22, 0.35);
  transform: translateY(-1px);
}

.nav-cta:active {
  transform: scale(0.97);
}

/* ─── Mobile toggle ──────────────────────────────────────── */
.mobile-toggle {
  display: none;
  width: 36px;
  height: 36px;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  border: none;
  background: transparent;
  color: #64748b;
  font-size: 16px;
  cursor: pointer;
  transition: color 0.15s, background 0.15s;
}

.mobile-toggle:hover {
  color: #0f172a;
  background: rgba(15, 23, 42, 0.06);
}

@media (max-width: 640px) {
  .mobile-toggle {
    display: flex;
  }
}

/* ─── Mobile dropdown island ─────────────────────────────── */
.mobile-island {
  pointer-events: all;
  width: 100%;
  max-width: 860px;
  margin-top: 8px;
  padding: 8px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 16px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 2px 8px rgba(0, 0, 0, 0.05),
    inset 0 1px 0 rgba(255, 255, 255, 1);
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.mobile-link {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 14px;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  color: #0f172a;
  text-decoration: none;
  transition: background 0.15s;
}

.mobile-link:hover {
  background: rgba(15, 23, 42, 0.04);
}

.mobile-link-arrow {
  margin-left: auto;
  font-size: 11px;
  color: #cbd5e1;
}

.mobile-link-divider {
  height: 1px;
  background: rgba(226, 232, 240, 0.8);
  margin: 4px 6px;
}

.mobile-cta {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 14px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 700;
  color: white;
  text-decoration: none;
  background: linear-gradient(135deg, #f97316, #ea580c);
  box-shadow: 0 4px 14px rgba(249, 115, 22, 0.25);
  transition: box-shadow 0.2s;
  margin-top: 2px;
}

.mobile-cta:active {
  transform: scale(0.98);
}

/* ─── Mobile dropdown animation ─────────────────────────── */
.mobile-drop-enter-active,
.mobile-drop-leave-active {
  transition: all 0.22s cubic-bezier(0.4, 0, 0.2, 1);
  transform-origin: top center;
}

.mobile-drop-enter-from,
.mobile-drop-leave-to {
  opacity: 0;
  transform: scaleY(0.9) translateY(-8px);
}
</style>

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  
  // Validation: Ensure API URL is present during build
  if (mode === 'production' && !env.VITE_API_URL) {
    console.error('\x1b[31m%s\x1b[0m', 'ERR! VITE_API_URL is not defined in the environment variables.');
    console.error('\x1b[33m%s\x1b[0m', 'Make sure to add it to GitHub Settings > Secrets and variables > Actions > Variables');
    // We don't throw here to avoid breaking local production builds if intended, 
    // but it will be visible in the logs.
  }

  return {
    base: env.VITE_BASE_PATH || '/SaveMyPass.dev/', // Fallback to repo name
    plugins: [vue()],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      port: 5173,
      host: true,
      proxy: {
        // Proxy API calls to Spring Boot during development
        '/api': {
          target: env.VITE_API_URL,
          changeOrigin: true,
        },
      },
    },
  }
})

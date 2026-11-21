import tailwindcss from '@tailwindcss/vite'

export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: false },
  css: ['~/assets/css/main.css'],
  routeRules: {
    '/api/**': {
      proxy: 'http://127.0.0.1:3002/api/**'
    }
  },
  vite: {
    plugins: [tailwindcss()]
  }
})

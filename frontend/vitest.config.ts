import { resolve } from 'path'
import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    // ... Specify options here
    globals: true,
    environment: 'happy-dom',
    setupFiles: './src/testSetup.js',
    env: {
      VITE_API_BASE_URL: 'http://localhost:8080'
    }
  },
  resolve: {
    alias: [{ find: '@', replacement: resolve(__dirname, './src') }]
  }
})

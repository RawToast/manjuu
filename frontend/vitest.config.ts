import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    // ... Specify options here
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/testSetup.js',
  },
})

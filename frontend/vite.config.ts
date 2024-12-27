/// <reference types="vitest" />
import { defineConfig } from 'vite'
import path from 'path'
import react from '@vitejs/plugin-react-swc'
import checker from 'vite-plugin-checker'

const isCi = process.env.NODE_ENV === 'ci'

const plugins = [
  react(),
  isCi
    ? null
    : checker({
        typescript: true,
        overlay: false
      })
]

export default defineConfig({
  plugins: plugins,
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
})

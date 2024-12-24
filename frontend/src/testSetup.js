import '@testing-library/jest-dom'
import { afterEach, afterAll, beforeAll } from 'vitest'
import { cleanup } from '@testing-library/react'

import { server } from '@/lib/mock-server'

// expect.extend(matchers)

beforeAll(() => server.listen())

// runs a cleanup after each test case (e.g. clearing jsdom)
afterEach(() => {
  cleanup()
  server.resetHandlers()
})

afterAll(() => server.close())

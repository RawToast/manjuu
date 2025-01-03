import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { expect, test } from 'vitest'

import { SearchBar } from '../searchbar'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { createTestRouter } from '@/lib/test-utils'
import { RouterProvider } from '@tanstack/react-router'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false
    },
    mutations: {
      retry: false
    }
  }
})

test('Renders searchbar', async () => {
  const testRouter = createTestRouter(() => <SearchBar />, queryClient)
  render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={testRouter} />,
    </QueryClientProvider>
  )
  const childText = await waitFor(() => screen.getByLabelText(/Search for an authority/i))
  expect(childText).toBeVisible()
  expect(childText).toMatchSnapshot()
})

test('Highlights best answer in search results', async () => {
  const user = userEvent.setup()
  const testRouter = createTestRouter(() => <SearchBar />, queryClient)
  render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={testRouter} />,
    </QueryClientProvider>
  )

  const label = await screen.findByPlaceholderText(/Search for an authority/i)
  const text = 'Edi'

  await user.type(label, text)
  const newLabel = await screen.findByPlaceholderText(/Search for an authority/i)
  expect((newLabel as HTMLInputElement).value).toEqual(text)

  const loading = await screen.findByRole('presentation')
  expect(loading).toBeVisible()

  const option = await screen.findByRole('option', { name: /Fake Edinburgh/i, selected: true })
  expect(option).toBeVisible()
})

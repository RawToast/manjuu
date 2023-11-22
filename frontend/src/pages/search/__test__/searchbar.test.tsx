import { render, screen } from '@testing-library/react'
import renderer from 'react-test-renderer'
import { expect, test } from 'vitest'

import { SearchBar } from '../searchbar'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

test('Renders', () => {
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

  const underTest = (
    <QueryClientProvider client={queryClient}>
      <SearchBar />
    </QueryClientProvider>
  )

  render(underTest)
  const childText = screen.getByPlaceholderText(/Type a command or search/i)
  expect(childText).toBeInTheDocument()

  expect(renderer.create(underTest).toJSON()).toMatchSnapshot()
})

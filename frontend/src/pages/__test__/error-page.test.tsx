import { act, render, screen } from '@testing-library/react'
import { expect, test } from 'vitest'

import { ErrorCard, ErrorPage } from '../error-page'

test('Renders error page with card', async () => {
  await act(async () => render(<ErrorPage />))

  const page = screen.getByTestId('error-page')
  expect(page).toBeVisible()

  const card = screen.getByTestId('error-card')
  expect(card).toBeVisible()

  const reloadButton = screen.getByText('Reload')
  expect(reloadButton).toBeVisible()
})

test('Renders error card without reload button', () => {
  render(<ErrorCard reload={false} />)
  const errorText = screen.getByText('Error')
  expect(errorText).toBeVisible()
  const reloadButton = screen.queryByRole('button')
  expect(reloadButton).toBeNull()
})

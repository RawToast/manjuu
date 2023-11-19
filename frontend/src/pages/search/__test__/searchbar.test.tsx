import { render, screen } from '@testing-library/react'
import renderer from 'react-test-renderer'
import { expect, test } from 'vitest'

import { SearchBar } from '../searchbar'

test('Renders', () => {
  const underTest = <SearchBar />

  render(underTest)
  const childText = screen.getByPlaceholderText(/Type a command or search/i)
  expect(childText).toBeInTheDocument()

  expect(renderer.create(underTest).toJSON()).toMatchSnapshot()
})

import './index.css'
import { QueryClientProvider } from '@tanstack/react-query'
import { RouterProvider } from '@tanstack/react-router'
import { queryClient, router } from './lib/router'
import { ErrorPage } from './pages/error-page'

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} defaultErrorComponent={ErrorPage} />
    </QueryClientProvider>
  )
}

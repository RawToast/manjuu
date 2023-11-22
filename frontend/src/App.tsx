import { RouterProvider, createBrowserRouter } from 'react-router-dom'
import './index.css'
import { Layout } from './pages/layout'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

const ROOT_PATH = '/'

const router = createBrowserRouter([
  {
    id: 'root',
    path: ROOT_PATH,

    // This route could be a hero dashboard or something
    // but we'll just redirect to the login page
    Component: () => <Layout />
  }
])

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} fallbackElement={<p>Initial Load...</p>} />
    </QueryClientProvider>
  )
}

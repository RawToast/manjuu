import './index.css'
import { Layout } from './pages/layout'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { RootRoute, Router, RouterProvider } from '@tanstack/react-router'

const rootRoute = new RootRoute({
  component: Layout
})

const routeTree = rootRoute.addChildren([])

const router = new Router({
  routeTree
})

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  )
}

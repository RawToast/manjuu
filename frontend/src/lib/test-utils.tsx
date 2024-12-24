import { QueryClient } from '@tanstack/react-query'
import {
  Outlet,
  createRoute,
  createMemoryHistory,
  createRouter,
  createRootRouteWithContext
} from '@tanstack/react-router'

export function createTestRouter(component: () => React.ReactNode, queryClient: QueryClient) {
  const rootRoute = createRootRouteWithContext<{ queryClient: QueryClient }>()({
    component: Outlet
  })

  const componentRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/',
    component
  })

  const router = createRouter({
    routeTree: rootRoute.addChildren([componentRoute]),
    history: createMemoryHistory(),
    context: { queryClient }
  })

  return router
}

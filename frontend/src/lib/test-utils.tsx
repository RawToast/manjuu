import { QueryClient } from '@tanstack/react-query'
import {
  Outlet,
  Route,
  Router,
  createMemoryHistory,
  rootRouteWithContext
} from '@tanstack/react-router'

export function createTestRouter(component: () => JSX.Element, queryClient: QueryClient) {
  const rootRoute = rootRouteWithContext<{ queryClient: QueryClient }>()({
    component: Outlet
  })

  const componentRoute = new Route({
    getParentRoute: () => rootRoute,
    path: '/',
    component
  })

  const router = new Router({
    routeTree: rootRoute.addChildren([componentRoute]),
    history: createMemoryHistory(),
    context: { queryClient }
  })

  return router
}

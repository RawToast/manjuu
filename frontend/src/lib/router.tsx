import { createRootRouteWithContext, createRoute, createRouter } from '@tanstack/react-router'
import { AuthorityInfo, Layout } from '@/pages'
import { QueryClient, queryOptions } from '@tanstack/react-query'
import { fetchAuthorityStats } from '@/lib/client'

export const queryClient = new QueryClient()

const rootRoute = createRootRouteWithContext<{
  queryClient: QueryClient
}>()({
  component: Layout,
  notFoundComponent: () => <div id='empty-route'></div>
})

export const authoritiesRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: 'authority'
})

export const authorityInfoQueryOptions = (authorityId: string) =>
  queryOptions({
    queryKey: ['authorityInfo', { authorityId }],
    queryFn: () => fetchAuthorityStats(authorityId)
  })

export const authorityStats = createRoute({
  getParentRoute: () => authoritiesRoute,
  path: '$authorityId',
  component: () => {
    const params = authorityStats.useParams()
    return <AuthorityInfo authorityId={params.authorityId} />
  },
  errorComponent: () => <div>404</div>,
  beforeLoad(opts) {
    opts.context.queryClient.ensureQueryData(authorityInfoQueryOptions(opts.params.authorityId))
  }
})

const authorityRoutes = authoritiesRoute.addChildren([authorityStats])
const routeTree = rootRoute.addChildren([authorityRoutes])

export const router = createRouter({
  routeTree,
  context: { queryClient: queryClient }
})

declare module '@tanstack/react-router' {
  interface Register {
    router: typeof router
  }
}

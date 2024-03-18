import { Elysia, t } from 'elysia'
import { authorityData, authoritiesMap } from './authorities'
import { establishmentsMap } from './establishments'

const app = new Elysia()
  .get('/', () => 'Hello 🦊 Elysia')
  .get('/authorities/basic', () => authorityData)
  .get('/authorities/:id', ({ set, params: { id } }) => {
    const authority = authoritiesMap.get(id)
    if (authority == undefined) {
      set.status = 404
      return {
        Message: `No authority found with id: ${id}`
      }
    } else {
      return authority
    }
  })
  .get(
    '/establishments',
    ({ set, query }) => {
      const data = establishmentsMap.get(query.localAuthorityId)
      if (data == undefined) {
        set.status = 404
        return {
          Message: `No authority found with id: ${query.localAuthorityId}`
        }
      } else {
        return data
      }
    },
    {
      query: t.Object({
        localAuthorityId: t.String(),
        pageSize: t.Optional(t.String()),
        pageNumber: t.Optional(t.String())
      })
    }
  )
  .listen(3000)

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`)

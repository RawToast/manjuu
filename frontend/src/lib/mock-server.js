import { http, HttpResponse } from 'msw'
import { setupServer } from 'msw/node'

export const fakeAuthorities = [
  {
    id: 1,
    name: 'Fake Newcastle',
    establishments: 123,
    url: 'https://www.norwich.gov.uk/site/custom_scripts/eh/foodratings.html',
    ratings: {
      five: 11,
      four: 24,
      three: 38,
      two: 41,
      one: 7,
      zero: 0,
      exempt: 4
    }
  },
  {
    id: 2,
    name: 'Fake Edinburgh',
    establishments: 572,
    url: 'https://www.norwich.gov.uk/site/custom_scripts/eh/foodratings.html',
    ratings: {
      pass: 398,
      improvement: 164,
      exempt: 10
    }
  }
]

const handlers = [
  http.get('http://localhost:8080/authority/:id', ({ params }) => {
    const authorityId = params.id
    const auth = fakeAuthorities.filter((auth) => auth.id === authorityId)
    if (auth.length > 0) {
      return HttpResponse.json(auth[0])
    } else {
      return HttpResponse.json({ error: 'Authority not found' }, 404)
    }
  }),
  http.get('http://localhost:8080/authority', () => {
    return HttpResponse.json(fakeAuthorities)
  })
]

export const server = setupServer(...handlers)

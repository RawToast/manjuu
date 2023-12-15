// src/mocks/handlers.js
import { http, HttpResponse } from 'msw'

const fakeAuthorities = [
  {
    name: 'Test Authority',
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

export const handlers = [
  http.get('authority/:id', ({ params }) => {
    const authorityId = params.id
    const authIsOdd = authorityId % 2 == 1

    if (authIsOdd) {
      return HttpResponse.json(fakeAuthorities[0])
    } else {
      return HttpResponse.json(fakeAuthorities[1])
    }
  })
]

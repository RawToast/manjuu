export interface Authority {
  name: string
  id: string
  establishments: number
}

export async function authorties(): Promise<Authority[]> {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/authority`, {
    method: 'GET',
    headers: {
      accept: 'application/json'
    }
  })

  const json = await response.json()
  const result = json as Authority[]
  return result
}

export interface StandardRatings {
  five: number
  four: number
  three: number
  two: number
  one: number
  zero: number
  exempt: number
}

export interface ScottishRatings {
  pass: number
  improvementRequired: number
  exempt: number
}

export interface AuthoritySummary {
  name: string
  ratings: StandardRatings | ScottishRatings
}

export async function fetchAuthorityStats(id): Promise<AuthoritySummary> {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/authority/${id}/stats`, {
    method: 'GET',
    headers: {
      accept: 'application/json'
    }
  })

  const json = await response.json()
  const result = json as AuthoritySummary
  return result
}

export interface ApiUser {
  id: string
  org_id: string
  is_superuser: boolean
}

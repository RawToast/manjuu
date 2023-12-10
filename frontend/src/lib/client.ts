import fetch from 'node-fetch'
import { z } from 'zod'

const AuthoritySchema = z.object({
  name: z.string().min(1),
  id: z.number().int().min(0),
  establishments: z.number().int().min(0)
})

export type Authority = z.infer<typeof AuthoritySchema>

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

export async function fetchAuthorities(): Promise<Authority[]> {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/authority`, {
    method: 'GET',
    headers: {
      accept: 'application/json'
    }
  })

  const json = await response.json()
  const result = z.array(AuthoritySchema).parse(json)
  return result
}

export async function fetchAuthorityStats(id): Promise<AuthoritySummary> {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/authority/${id}`, {
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

export async function apiLogin(username: string, password: string): Promise<Response> {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/auth/login`, {
    method: 'POST',
    credentials: 'include',
    headers: {
      accept: 'application/json',
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: `username=${username}&password=${password}`
  })
  return response
}

export async function getMe(): Promise<ApiUser> {
  try {
    const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/users/me`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        accept: 'application/json'
      }
    })

    const json = await response.json()
    console.log('getMe', json)
    const result = json as ApiUser
    return result
  } catch (e) {
    console.error('Error in getMe', e)
    throw new Error(e)
  }
}

export async function apiLogout(): Promise<Response> {
  const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/auth/logout`, {
    method: 'POST',
    credentials: 'include'
  })
  return response
}

export interface ApiUser {
  id: string
  org_id: string
  is_superuser: boolean
}

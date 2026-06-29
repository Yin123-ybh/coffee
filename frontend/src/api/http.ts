import axios from 'axios'

export interface Result<T> {
  code: number
  message: string
  data: T
}

export const http = axios.create({
  baseURL: '/api',
  timeout: 8000,
  headers: {
    'X-User-Id': '1'
  }
})

export async function getData<T>(url: string, params?: Record<string, unknown>) {
  const { data } = await http.get<Result<T>>(url, { params })
  if (data.code !== 0) throw new Error(data.message)
  return data.data
}

export async function postData<T>(url: string, body?: unknown) {
  const { data } = await http.post<Result<T>>(url, body ?? {})
  if (data.code !== 0) throw new Error(data.message)
  return data.data
}

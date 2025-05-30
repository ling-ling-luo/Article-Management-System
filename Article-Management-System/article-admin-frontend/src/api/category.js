import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

export function fetchCategories() {
  return api.get('/categories')
}

export function addCategory(data) {
  return api.post('/categories', data)
}

export function updateCategory(id, data) {
  return api.put(`/categories/${id}`, data)
}

export function deleteCategory(id) {
  return api.delete(`/categories/${id}`)
} 
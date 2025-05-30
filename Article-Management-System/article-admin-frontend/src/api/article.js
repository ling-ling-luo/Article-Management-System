import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

export function fetchArticles(params) {
  return api.get('/articles', { params })
}

export function addArticle(data) {
  return api.post('/articles', data)
}

export function updateArticle(id, data) {
  return api.put(`/articles/${id}`, data)
}

export function deleteArticle(id) {
  return api.delete(`/articles/${id}`)
} 
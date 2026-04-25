import api from './axios'

export const credentialsApi = {
  list: () => api.get('/api/credentials'),
  get: (id) => api.get(`/api/credentials/${id}`),
  add: (data) => api.post('/api/credentials', data),
  update: (id, data) => api.put(`/api/credentials/${id}`, data),
  remove: (id) => api.delete(`/api/credentials/${id}`),
}

import api from './axios'

export const authApi = {
  login: (data) => api.post('/api/auth/login', data),
  signup: (data) => api.post('/api/auth/signup', data),
  me: () => api.get('/api/auth/me'),
  changePassword: (data) => api.put('/api/auth/password', data),
}

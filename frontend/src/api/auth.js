import api from './axios'

export const authApi = {
  getSalt: (email) => api.get(`/api/auth/salt?email=${email}`),
  login: (data) => api.post('/api/auth/login', data),
  signup: (data) => api.post('/api/auth/signup', data),
  me: () => api.get('/api/auth/me'),
  rotateVault: (data) => api.put('/api/auth/rotate-vault', data),
}

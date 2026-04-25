import api from './axios'

export const notesApi = {
    getAll: () => api.get('/api/notes'),
    getById: (id) => api.get(`/api/notes/${id}`),
    create: (data) => api.post('/api/notes', data),
    update: (id, data) => api.put(`/api/notes/${id}`, data),
    delete: (id) => api.delete(`/api/notes/${id}`),
}

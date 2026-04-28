import api from './axios'

export const notesApi = {
    list: () => api.get('/api/notes'),
    getById: (id) => api.get(`/api/notes/${id}`),
    add: (data) => api.post('/api/notes', data),
    update: (id, data) => api.put(`/api/notes/${id}`, data),
    remove: (id) => api.delete(`/api/notes/${id}`),
}

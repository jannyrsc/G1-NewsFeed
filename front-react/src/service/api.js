import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080', // URL do backend
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    console.log('Token:', token);  // Verifica se o token está sendo recuperado corretamente
  }
  return config;
});
export default api;


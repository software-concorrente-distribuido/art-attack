import axios from 'axios';

const api = axios.create({
    baseURL: 'http://172.16.6.206:8080/api',
});

export default api;

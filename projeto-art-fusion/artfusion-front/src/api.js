import axios from 'axios';

const api = axios.create({
    baseURL: 'http://10.2.0.190:8080/api',
});

export default api;
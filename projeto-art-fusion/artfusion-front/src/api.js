import axios from 'axios';
import env from '../src/env/env'

const api = axios.create({
    baseURL: `${env.api_protocol}://${env.api_host}:${env.api_port}/api` ,
    // baseURL: 'http://52.67.57.216:8089/api',
});

export default api;

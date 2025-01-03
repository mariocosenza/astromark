import axios from 'axios';
import {Env} from "../Env.ts";

const axiosConfig = axios.create({
    baseURL: Env.API_BASE_URL, //replace with your BaseURL
    headers: {
        'Content-Type': 'application/json', // change according header type accordingly
    },
});

export default axiosConfig;

axiosConfig.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('user'); // get stored access token
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`; // set in header
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

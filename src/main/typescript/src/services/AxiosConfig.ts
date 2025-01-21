import axios from 'axios';
import {Env} from "../Env.ts";
import {getToken, isExpired} from "./AuthService.ts";

const axiosConfig = axios.create({
    baseURL: Env.API_BASE_URL, //replace with your BaseURL
    headers: {
        'Content-Type': 'application/json',
    },
});

delete axios.defaults.headers.common['Access-Control-Allow-Origin'];
export default axiosConfig;

axiosConfig.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('user'); // get stored access token
        if (accessToken && !isExpired()) {
            config.headers.Authorization = `Bearer ${getToken()}`; // set in header
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);
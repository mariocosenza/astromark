import axios from 'axios';
import {Env} from "../Env.ts";
import {isExpired} from "./AuthService.ts";

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
            config.headers.Authorization = `Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9zdHVkZW50Iiwic3ViIjoiMGFiZTFmM2ItNjZjZi00M2JjLWE5ZTctN2Y5ODBjNmYzNGEyIiwiaWF0IjoxNzM2MTc4MzQ3LCJleHAiOjE3MzYxODU1NDd9.PrxspqD3Ia9bHPOzAD9JJ3VCTzjmfQ-R40CFwC9I6jA`; // set in header
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

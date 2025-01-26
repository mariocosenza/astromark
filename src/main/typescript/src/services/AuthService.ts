import {jwtDecode} from "jwt-decode";
import {JwtToken} from "../entities/JwtToken.ts";
import axiosConfig from "./AxiosConfig.ts";
import {Env} from "../Env.ts";

export function isLogged(): boolean {
    return !isExpired();
}

export function logout(): void {
    localStorage.removeItem("year")
    localStorage.removeItem("studentId")
    try {
        if (localStorage.getItem("user") !== null) {
            axiosConfig.post(Env.API_BASE_URL + "/auth/logout").then(_ => localStorage.removeItem("user"))
        }
        localStorage.removeItem("teaching")
        localStorage.removeItem("schoolClassId")
        localStorage.removeItem("schoolClassTitle")
        localStorage.removeItem("schoolClassDesc")
    } catch (e) {
        localStorage.removeItem("user")
        console.log("User not exist");
    }
}

export async function asyncLogout(item: any) {
    try {
        localStorage.removeItem("year")
        localStorage.removeItem("studentId")
        if (item !== null) {
            await axiosConfig.post(Env.API_BASE_URL + "/auth/logout")
            localStorage.removeItem("user")
        }
        localStorage.removeItem("teaching")
        localStorage.removeItem("schoolClassId")
        localStorage.removeItem("schoolClassTitle")
        localStorage.removeItem("schoolClassDesc")
    } catch (e) {
        localStorage.removeItem("user")
        console.log("User not exist");
    }
}


export function getToken(): string {
    const user = localStorage.getItem("user");
    if (user === null) {
        return "";
    }
    return user.replaceAll("\"", "");
}

export function getRole(): string {
    const user = localStorage.getItem("user");
    if (user === null) {
        return "GUEST";
    }
    return (jwtDecode(user) as JwtToken).role.substring(5);
}

export function saveToken(token: string): void {
    localStorage.setItem("user", token);
}

export function replaceToken(token: string): void {
    saveToken(token);
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
}

export function getCurrentUser(): JwtToken | null {
    const user = localStorage.getItem("user");
    if (user === null) {
        return null;
    }
    return jwtDecode(user) as JwtToken;
}

export function isExpired(): boolean {
    const user = getCurrentUser();
    if (user !== null) {
        return user.exp < Date.now() / 1000;
    }
    return true;
}

export function isRole(role: string): boolean {
    return getRole().toUpperCase() === role.toUpperCase();
}

export function getId(): string {
    const user = localStorage.getItem("user");
    if (user === null) {
        return "";
    }
    return (jwtDecode(user) as JwtToken).sub;
}



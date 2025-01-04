import {jwtDecode} from "jwt-decode";
import {JwtToken} from "../entities/JwtToken.ts";

export function isLogged(): boolean {
    return localStorage.getItem("user") !== null;
}

export function logout(): void {
    try {
        localStorage.removeItem("user");
    } catch (e) {
        console.log("User not exist");
    }
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
    logout();
    saveToken(token);
}

export function getCurrentUser(): JwtToken | null {
    const user = localStorage.getItem("user");
    if(user === null) {
        return null;
    }
    return jwtDecode(user) as JwtToken;
}

export function isExpired(): boolean {
    if(isLogged()) {
        const user = getCurrentUser()
        if(user === null) {
            return true;
        }

        return user.exp < Date.now() / 1000;
    }
    return true;
}

export function isRole(role: string): boolean {
    return getRole().toUpperCase() === role.toUpperCase();
}
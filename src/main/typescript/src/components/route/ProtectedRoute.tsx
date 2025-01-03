import { Navigate, Outlet } from 'react-router';

import React from "react";
import {jwtDecode} from "jwt-decode";
import {JwtToken} from "../../entities/JwtToken.ts";

export const ProtectedRoutePath: React.FC<ProtectedRoutePathProps> = ({role}: ProtectedRoutePathProps) => {
    const user = localStorage.getItem("user")
    if (user === null) {
        return <Navigate to="/" replace />;
    }
    console.log(role)

    const token: JwtToken = jwtDecode(user) as JwtToken;

    const expired: boolean = token.exp < Date.now() / 1000;



    if(expired || token.role.toUpperCase() !== role) {
        if(expired) {
            localStorage.removeItem("user");
        }
        return <Navigate to="/" replace />;
    }


    return <Outlet />;
};

export type ProtectedRoutePathProps = {
    role: Role
}

export enum Role {
    STUDENT = "STUDENT",
    TEACHER = "TEACHER",
    SECRETARY = "SECRETARY",
    PARENT = "PARENT"
}
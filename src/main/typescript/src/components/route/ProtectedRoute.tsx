import { Navigate, Outlet } from 'react-router';

import React from "react";
import {getRole, isExpired, isLogged} from "../../services/AuthService.ts";

export const ProtectedRoutePath: React.FC<ProtectedRoutePathProps> = ({role}: ProtectedRoutePathProps) => {

    if (!isLogged()) {
        return <Navigate to="/" replace />;
    }

    const expired: boolean = isExpired()

    if(expired || getRole().toUpperCase() !== role) {
        if(expired) {
            localStorage.removeItem("user")
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
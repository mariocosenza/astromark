import {Navigate, Outlet} from 'react-router';

import React from "react";
import {getRole, isExpired, isLogged} from "../../services/AuthService.ts";
import {ProtectedRoutePathProps} from "./ProtectedRoute.tsx";
import {SecretaryDashboardNavbar} from "../SecretaryDashboardNavbar.tsx";

export const ProtectedSecretaryRoute: React.FC<ProtectedRoutePathProps> = ({role}: ProtectedRoutePathProps) => {

    if (!isLogged()) {
        return <Navigate to="/" replace/>;
    }

    const expired: boolean = isExpired()

    if (expired || getRole().toUpperCase() !== role) {
        if (expired) {
            localStorage.removeItem("user")
        }

        return <Navigate to="/" replace/>;
    }

    return (
        <div>
            <SecretaryDashboardNavbar/>
            <Outlet/>
        </div>
    )
};



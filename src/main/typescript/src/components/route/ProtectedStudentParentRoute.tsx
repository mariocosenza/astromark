import { Navigate, Outlet } from 'react-router';

import React from "react";
import {getRole, isExpired, isLogged} from "../../services/AuthService.ts";
import {DashboardNavbar} from "../DashboardNavbar.tsx";
import {ProtectedRoutePathProps} from "./ProtectedRoute.tsx";

export const ProtectedStudentParentRoute: React.FC<ProtectedRoutePathProps> = ({role}: ProtectedRoutePathProps) => {

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

    return (
        <div>
            <DashboardNavbar/>
            <Outlet/>
        </div>
    )
};



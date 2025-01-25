import React from "react";
import {ProtectedRoutePathProps} from "./ProtectedRoute.tsx";
import {getRole, isExpired, isLogged} from "../../services/AuthService.ts";
import {Navigate, Outlet} from "react-router";
import {TeacherDashboardNavbar} from "../TeacherDashboardNavbar.tsx";

export const ProtectedTeacherRoute: React.FC<ProtectedRoutePathProps> = ({role}: ProtectedRoutePathProps) => {

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
            <TeacherDashboardNavbar/>
            <Outlet/>
        </div>
    )
};
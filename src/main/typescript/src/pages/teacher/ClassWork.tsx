import React from "react";
import {Typography} from "@mui/material";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";


export const ClassWork: React.FC = () => {
    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Giornale di Classe
            </Typography>

        </div>
    );
};
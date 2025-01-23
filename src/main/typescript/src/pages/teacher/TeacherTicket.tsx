import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import React from "react";
import {TicketComp} from "../../components/TicketComp.tsx";


export const TeacherTicket: React.FC = () => {

    return (
        <div>
            <TeacherDashboardNavbar/>
            <TicketComp/>
        </div>
    );
}
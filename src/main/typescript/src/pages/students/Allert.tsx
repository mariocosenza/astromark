import React from "react";
import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import {AccordionNotViewable} from "../../components/AccordionNotViewable.tsx";

export const Allert : React.FC = () => {
    return (
        <div>
           <DashboardNavbar/>
            <div className={'alert'}>
                {[...Array(10)].map((_, i) =>
                    <AccordionNotViewable key={i} />
                )}
            </div>
        </div>
    );
}
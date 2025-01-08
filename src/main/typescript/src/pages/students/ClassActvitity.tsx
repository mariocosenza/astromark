import React from "react";
import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import {Box, Divider, Tab, Tabs} from "@mui/material";

const Homework : React.FC = () => {
    return (
        <div>
            <h1>Homework</h1>
        </div>
    )
}

const Activity : React.FC = () => {
    return (
        <div>
            <h1>Activity</h1>
        </div>
    )
}

export const ClassActvitity: React.FC = () => {
    const [value, setValue] = React.useState(0);

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    return (
        <div>
            <DashboardNavbar/>
            <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
                <Tabs
                    value={value}
                    onChange={handleChange}
                    variant="fullWidth"
                >
                    <Tab label="Assegno e verifiche" />
                    <Tab label="attività svolte" />
                </Tabs>
                <Divider/>
                {
                    value === 0? <Homework/> : <Activity/>
                }
            </Box>
        </div>

    );
}
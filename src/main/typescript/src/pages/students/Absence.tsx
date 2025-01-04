import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import React from "react";
import {Box, Divider, Stack, Tab, Tabs} from "@mui/material";

const Absence : React.FC = () => {
    return (
        <div>
            <h1>Assenze</h1>
        </div>
    )
}

const Delay : React.FC = () => {
    return (
        <div>
            <h1>Ingressi in Ritardo</h1>
        </div>
    )
}

export const AbsenceDelays: React.FC = () => {
    const [value, setValue] = React.useState(0);

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };
    return (
        <div>
            <DashboardNavbar/>
            <Stack width={'100%'} spacing={0}
                   direction="row"
                   sx={{height: 'calc(100vh - 64px)'}}>
                <Box sx={{ minWidth: '70%'}}>
                    <Box sx={{ minWidth: '100%', bgcolor: 'background.paper' }}>
                        <Tabs
                            value={value}
                            onChange={handleChange}
                            variant="fullWidth"
                        >
                            <Tab label="Assenze" />
                            <Tab label="Ingressi in ritardo" />
                        </Tabs>
                        <Divider/>
                        {
                            value === 0? <Absence/> : <Delay/>
                        }
                    </Box>
                </Box>
                <Stack spacing={'20%'} direction={'column'} className={'sideContainer'}>
                        <Box>
                            <h1>Statistiche</h1>
                        </Box>
                    <Box>
                        <h1>Statistiche</h1>
                    </Box>
                    <Box>
                        <h1>Statistiche</h1>
                    </Box>
                </Stack>
            </Stack>
        </div>
    );
}
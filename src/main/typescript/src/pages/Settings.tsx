import React from "react";
import {Box, Button, Stack, TextField} from "@mui/material";
import {getRole} from "../services/AuthService.ts";
import {Role} from "../components/route/ProtectedRoute.tsx";
import {DashboardNavbar} from "../components/DashboardNavbar.tsx";
import DatePicker from "react-multi-date-picker";

export const Settings: React.FC = () => {
    return (
        <div>
            {
                getRole().toUpperCase() === Role.STUDENT || getRole().toUpperCase() === Role.PARENT ? <DashboardNavbar/> : <h1>Settings</h1>
            }
            <Stack
                spacing={2}
                direction="row"
                flexWrap={'wrap'}
                width={'100%'}
                sx={{
                    height: 'calc(100vh - 64px)',
                    justifyContent: 'center'
                }}
            >
                <Box className={'surface-container'} style={{maxHeight: '80vh', minWidth: '40vw', marginTop: '2rem', display: 'grid', padding: '2vw'}}>
                    <h1>Anagrafica</h1>
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <DatePicker/>
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <Button variant="contained" style={{maxHeight: '4rem'}}>Salva</Button>
                </Box>
                <Box className={'secondary-container'} style={{maxHeight: '80vh', minWidth: '40vw', marginTop: '2rem', display: 'grid', padding: '2vw'}}>
                    <h1>Informazioni contatto</h1>
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <TextField id="outlined-basic" label="Outlined" variant="outlined" />
                    <Button variant="contained" style={{maxHeight: '4rem'}}>Consensi privacy</Button>
                    <Button variant="contained" style={{maxHeight: '4rem'}}>Salva</Button>
                </Box>
            </Stack>
        </div>
    );
}
import React from "react";
import mark from '../assets/mark.png';
import dashboard from '../assets/dashboard.png';
import {
    Box,
    List,
    ListItem,
    ListItemText,
    Typography
} from "@mui/material";
import Grid from '@mui/material/Grid2';
import {HomePageFooter} from "../components/HomePageFooter.tsx";
import {HomePageNavbar} from "../components/HomePageNavbar.tsx";


export const App: React.FC = () => {
    return (
       <main style={{
           height: '100vh',
           display: 'flex',
           flexDirection: 'column'}}>
        <HomePageNavbar/>
            <Box sx={{ height: '100%', width: '80vw', m: 'auto', flex: 1}}>
                <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }} mx='0%' mt='5%'>
                    <Grid size={6}>
                        <List sx = {{ listStyleType : 'disc', paddingLeft: '5%'}}>
                            <ListItem disablePadding sx = {{ display: 'list-item' }}>
                                <ListItemText primary="Scopri la tua media valutazioni in tempo reale." />
                            </ListItem>
                            <ListItem disablePadding sx = {{ display: 'list-item' }}>
                                <ListItemText primary="Giustifica le tue assenze con facilità." />
                            </ListItem>
                            <ListItem disablePadding sx = {{ display: 'list-item' }}>
                                <ListItemText primary="Gestisci compiti e valutazioni in modo intuitivo." />
                            </ListItem>
                            <ListItem disablePadding sx = {{ display: 'list-item' }}>
                                <ListItemText primary="Accedi a strumenti di gestione per dirigenti scolastici" />
                            </ListItem>
                        </List>
                    </Grid>
                    <Grid size={6} sx={{textAlign: 'center'}}>
                        <img style={{maxWidth: '70%', maxHeight: '70%'}} src={mark} alt={'Immagine della dashboard voti'}/>
                    </Grid>
                    <Grid size={6}>
                        <Typography variant={"body1"}>
                            Iscriviti alla nostra piattaforma e offri alla tua scuola un registro elettronico innovativo e intuitivo.
                            Con strumenti per la gestione delle presenze, delle valutazioni e una comunicazione fluida con studenti e genitori, rendiamo la gestione scolastica semplice e accessibile a tutti!
                        </Typography>
                    </Grid>
                    <Grid size={6} sx={{textAlign: 'center'}}>
                        <img style={{maxWidth: '70%', maxHeight: '70%'}} src={dashboard}
                             alt={'Immagine astratta di una dashboard'}/>
                    </Grid>
                </Grid>
            </Box>
        <HomePageFooter/>
        </main>)
};

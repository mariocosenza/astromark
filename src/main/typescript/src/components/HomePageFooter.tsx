import {Container, Stack, Typography} from "@mui/material";
import React from "react";
import {useNavigate} from "react-router";


export const HomePageFooter: React.FC = () => {
    const navigator = useNavigate();
    return (
        <footer style={{backgroundColor: 'var(--md-sys-color-primary-container)'}}>
            <Container maxWidth={false}>
                <Stack useFlexGap direction="row" spacing={'20vw'} sx={{
                    direction: "column",
                    flexWrap: 'wrap',
                    marginBottom: '2rem',
                    justifyContent: "center",
                    width: '100%',
                    paddingTop: '1rem'
                }}>
                    <Typography variant={"h4"}>
                        Contatti
                        <Typography variant={"body1"}>
                            Email: <a href={"mailto:supporto@astromark.com"}
                                      className={'whiteLink'}>supporto@astromark.com</a> <br/>
                            Numero di telefono: +39 123456789 <br/>
                            Orari di assistenza: <br/>
                            Lunedì a Venerdì: 09:00 - 18:00
                        </Typography>
                    </Typography>
                    <Typography variant={"h4"} style={{cursor: 'pointer'}}>
                        Link Utili
                        <Typography variant={"body1"} onClick={() => navigator("/help")}>
                            Termini e Condizioni <br/>
                            Informativa sulla Privacy <br/>
                            FAQ o Guida all'Utilizzo
                        </Typography>
                    </Typography>
                    <Typography variant={"h4"}>
                        Copyright
                        <Typography variant={"body1"}>
                            © 2025 Astromark.
                            Tutti i diritti riservati.
                        </Typography>
                    </Typography>
                </Stack>
            </Container>
        </footer>
    );
};
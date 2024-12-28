import {Container, Stack, Typography} from "@mui/material";
import React from "react";


export const HomePageFooter: React.FC = () => {
    return (
        <footer style={{backgroundColor: 'var(--md-sys-color-primary-container)'}}>
            <Container maxWidth={false}>
                <Stack useFlexGap direction="row" spacing={'20vw'} sx={{
                    direction: "column",
                    flexWrap: 'wrap',
                    alignItems: "center",
                    marginBottom: '2rem',
                    justifyContent: "center",
                    width: '100%',
                }}>
                    <Typography variant={"h3"}>
                        Contatti
                        <Typography variant={"body1"}>
                            Paragrafo test
                        </Typography>
                    </Typography>
                    <Typography variant={"h3"}>
                        Link Utili
                        <Typography variant={"body1"}>
                            Paragrafo test
                        </Typography>
                    </Typography>
                    <Typography variant={"h3"}>
                        Copyright
                        <Typography variant={"body1"}>
                            Paragrafo test
                        </Typography>
                    </Typography>
                </Stack>
            </Container>
        </footer>
    );
};
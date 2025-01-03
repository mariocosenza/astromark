import React from "react";
import {AppBar, Box, Button, IconButton, Toolbar, Typography} from "@mui/material";
import HelpIcon from "@mui/icons-material/Help";
import {Link} from "react-router";

type PropsLogin = {
    showLogin: boolean
}

export const HomePageNavbar: React.FC<PropsLogin> = ({showLogin}) => {
    return (
        <header>
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <AppBar position="static">
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            sx={{mr: 2}}
                        >
                        </IconButton>
                        <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                            AstroMark
                        </Typography>
                        <HelpIcon/>
                        {
                            showLogin && <div> <Link to="/login" className={'whiteLink'}>
                                <Button color="inherit">Accedi</Button>
                            </Link> </div>
                        }
                    </Toolbar>
                </AppBar>
            </Box>
        </header>
    );
};
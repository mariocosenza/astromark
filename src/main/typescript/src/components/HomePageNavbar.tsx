import React from "react";
import {AppBar, Box, Button, IconButton, Toolbar, Typography} from "@mui/material";
import HelpIcon from "@mui/icons-material/Help";
import {Link, useNavigate} from "react-router";
import {getRole, isLogged} from "../services/AuthService.ts";


export const HomePageNavbar: React.FC = () => {
    const path: string = `/${getRole().toLowerCase()}/dashboard`
    const navigator = useNavigate();
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
                            onClick={() => navigator("/")}
                        >
                            <Typography variant="h6">
                                AstroMark
                            </Typography>
                        </IconButton>
                        <Typography variant="h6" component="div" sx={{flexGrow: 1}}>

                        </Typography>
                        {
                            !isLogged() ? <div><Link to="/login" className={'whiteLink'}>
                                <Button color="inherit">Accedi</Button>
                            </Link></div> : <div><Link to={path} className={'whiteLink'}>
                                <Button color="inherit">Vai alla Dashboard</Button>
                            </Link></div>
                        }
                        <HelpIcon onClick={() => navigator("/help")}/>
                    </Toolbar>
                </AppBar>
            </Box>
        </header>
    );
};
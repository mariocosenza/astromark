import React from "react";
import {NavLink, useNavigate} from "react-router";
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import {asyncLogout} from "../services/AuthService.ts";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import {SecretarySideNav} from "./SecretarySideNav.tsx";

export const SecretaryDashboardNavbar: React.FC = () => {
    const navigator = useNavigate()
    return (
        <header>
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <AppBar position="static">
                    <Toolbar>
                        <SecretarySideNav/>
                        <Typography variant="h6" component="div" sx={{ml: 1, flexGrow: 1}}>
                            <NavLink to="/dashboard" className={'whiteLink'}>
                                AstroMark
                            </NavLink>
                        </Typography>

                        <LogoutOutlinedIcon
                            sx={{ml: 1}}
                            onClick={() => {
                                asyncLogout(localStorage.getItem("user")).then(_ => navigator('/logout'))
                            }
                            }
                        />
                    </Toolbar>
                </AppBar>
            </Box>
        </header>
    );
};
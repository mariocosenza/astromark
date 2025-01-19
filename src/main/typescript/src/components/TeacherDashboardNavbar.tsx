import React from "react";
import {NavLink, useNavigate} from "react-router";
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import {logout} from "../services/AuthService.ts";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import {TeacherSideNav} from "./TeacherSideNav.tsx";

export const TeacherDashboardNavbar: React.FC = () => {
    const navigator = useNavigate()
    return (
        <header>
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <AppBar position="static">
                    <Toolbar>
                        <TeacherSideNav/>
                        <Typography variant="h6" component="div" sx={{ml: 1, flexGrow: 1}}>
                            <NavLink to="/dashboard" className={'whiteLink'}>
                                AstroMark
                            </NavLink>
                        </Typography>

                        <LogoutOutlinedIcon
                            sx={{ml: 1}}
                            onClick={() => {
                                logout()
                                navigator('/')
                            }
                            }
                        />
                    </Toolbar>
                </AppBar>
            </Box>
        </header>
    );
};
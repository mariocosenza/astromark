import React from "react";
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';
import {StudentParentSideNav} from "./StudentParentSideNav.tsx";
import {Role} from "./route/ProtectedRoute.tsx";
import {isRole, logout} from "../services/AuthService.ts";
import {ArchiveMenu} from "./ArchiveMenu.tsx";
import {NavLink, useNavigate} from "react-router";



export const DashboardNavbar: React.FC = () => {
    const navigator = useNavigate()
    return (
        <header>
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <AppBar position="static">
                    <Toolbar>
                        <StudentParentSideNav/>
                        <Typography variant="h6" component="div" sx={{ml: 1, flexGrow: 1}}>
                            <NavLink to="/dashboard" className={'whiteLink'}>
                            AstroMark
                            </NavLink>
                        </Typography>
                        <ArchiveMenu/>
                        {
                            isRole(Role.PARENT) && <div> <AccountCircleOutlinedIcon/></div>
                        }
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
import React from "react";
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';
import {StudentParentSideNav} from "./StudentParentSideNav.tsx";
import {Role} from "./route/ProtectedRoute.tsx";
import {asyncLogout, isRole} from "../services/AuthService.ts";
import {ArchiveMenu} from "./ArchiveMenu.tsx";
import {NavLink, useNavigate} from "react-router";
import {AccountMenu} from "./AccountMenu.tsx";



export const DashboardNavbar: React.FC = () => {
    const navigator = useNavigate()
    return (
        <header style={{overflowY: "hidden"}}>
            <Box sx={{flexGrow: 1, width: '100%'}}>
                <AppBar position="static">
                    <Toolbar>
                        <StudentParentSideNav/>
                        <Typography variant="h6" component="div" sx={{ml: 1, flexGrow: 1}}>
                            <NavLink to="/dashboard" className={'whiteLink'}>
                            AstroMark
                            </NavLink>
                        </Typography>
                        {
                            isRole(Role.PARENT) && <div> <AccountMenu/></div>
                        }
                        <ArchiveMenu/>
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
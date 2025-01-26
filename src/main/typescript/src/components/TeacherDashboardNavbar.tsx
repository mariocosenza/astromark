import React, {useEffect} from "react";
import {NavLink, useNavigate} from "react-router";
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import {asyncLogout, isRole} from "../services/AuthService.ts";
import LogoutOutlinedIcon from "@mui/icons-material/LogoutOutlined";
import {TeacherSideNav} from "./TeacherSideNav.tsx";
import {TeachingMenu} from "./TeachingMenu.tsx";
import {Role} from "./route/ProtectedRoute.tsx";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";
import {AxiosResponse} from "axios";
import {SelectedTeaching} from "../services/TeacherService.ts";

export const TeacherDashboardNavbar: React.FC = () => {
    const [teachings, setTeachings] = React.useState<string[]>([])
    const [loading, setLoading] = React.useState<boolean>(true)
    const navigator = useNavigate()

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            if (isRole(Role.TEACHER)){
                const response: AxiosResponse<string[]> = await axiosConfig.get(`${Env.API_BASE_URL}/teachers/teachings`);
                if (response.data.length) {
                    setTeachings(response.data);
                    if(localStorage.getItem("teaching") === null){
                        SelectedTeaching.teaching = response.data[0]
                    }
                }
                setLoading(false);
            }

        } catch (error) {
            console.error(error);
        }
    }

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
                        {
                            !loading && <TeachingMenu data={teachings}/>
                        }
                        <LogoutOutlinedIcon
                            sx={{ml: 1}}
                            onClick={() => {
                                localStorage.removeItem("teaching")
                                localStorage.removeItem("schoolClassId")
                                localStorage.removeItem("schoolClassTitle")
                                localStorage.removeItem("schoolClassDesc")
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
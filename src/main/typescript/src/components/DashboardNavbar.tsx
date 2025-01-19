import React, {useEffect, useState} from "react";
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import LogoutOutlinedIcon from '@mui/icons-material/LogoutOutlined';
import {StudentParentSideNav} from "./StudentParentSideNav.tsx";
import {Role} from "./route/ProtectedRoute.tsx";
import {asyncLogout, isRole} from "../services/AuthService.ts";
import {ArchiveMenu} from "./ArchiveMenu.tsx";
import {NavLink, useNavigate} from "react-router";
import {AccountMenu, SchoolUserDetail} from "./AccountMenu.tsx";
import {getStudentYears} from "../services/StudentService.ts";
import {SelectedStudent, SelectedYear} from "../services/StateService.ts";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";



export const DashboardNavbar: React.FC = () => {

    const [students, setStudents]  = useState<SchoolUserDetail[]>([]);
    const [years, setYears]  = useState<number[]>([]);
    const [loading, setLoading] = React.useState<boolean>(true);



    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            if(!isRole(Role.STUDENT)) {
                await axiosConfig.get<SchoolUserDetail[]>(Env.API_BASE_URL + '/parents/students').then((response) => {
                    SelectedStudent.id = response.data[0].id
                    setStudents(response.data)
                    getStudentYears().then((response) => {
                        console.log(response)
                        if (response !== null) {
                            SelectedYear.year = response[0]
                        }
                    })
                });
            }
        } catch (error) {
            console.error(error);
        }
        try {
            await getStudentYears().then((response) => {
                if (response !== null) {
                    if (SelectedYear.isNull()) {
                        SelectedYear.year = response[0]
                    }
                    setYears(response)
                } else {
                    setYears(new Array(new Date().getFullYear()))
                }
            })
            setLoading(false);
        } catch (error) {
            setYears(new Array(new Date().getFullYear()))
            console.error(error);
        }
    }

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
                           !loading && isRole(Role.PARENT) && <div> <AccountMenu data={students}/></div>
                        }
                        {
                            !loading && <ArchiveMenu data={years}/>
                        }
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
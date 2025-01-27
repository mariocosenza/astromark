import React, { useEffect } from "react";
import { Box, Stack, Typography, useTheme } from "@mui/material";
import { AxiosResponse } from "axios";
import timetable from "../assets/timetable.png";
import classroom from "../assets/classroom.png";
import ticket from "../assets/ticket.png";
import setting from "../assets/setting.png";
import { useNavigate } from "react-router";
import {SchoolUserDetail} from "./AccountMenu.tsx";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";

interface DashboardProps {
    isTeacher: boolean;
}

export const Dashboard: React.FC<DashboardProps> = ({ isTeacher }) => {
    const theme = useTheme();
    const [loading, setLoading] = React.useState<boolean>(true);
    const [schoolUser, setSchoolUser] = React.useState<SchoolUserDetail>();
    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<SchoolUserDetail> = await axiosConfig.get(`${Env.API_BASE_URL}/school-users/detailed`);
            setSchoolUser(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
            setLoading(false);
        }
    }

    const menuItems = isTeacher
        ? [
            { img: timetable, label: "Ricevimenti", color: theme.palette.primary.main, path: "/teacher/ricevimento" },
            { img: classroom, label: "Classi", color: theme.palette.primary.main, path: "/teacher/classi" },
            { img: ticket, label: "Ticket", color: theme.palette.info.main, path: "/teacher/ticket" },
            { img: setting, label: "Impostazioni", color: theme.palette.info.main, path: "/teacher/impostazioni" }
        ]
        : [
            { img: timetable, label: "Orario", color: theme.palette.primary.main, path: "/secretary/timetable" },
            { img: classroom, label: "Gestione classi", color: theme.palette.primary.main, path: "/secretary/manage-class" },
            { img: ticket, label: "Ticket", color: theme.palette.info.main, path: "/secretary/ticket" },
            { img: setting, label: "Impostazioni", color: theme.palette.info.main, path: "/secretary/impostazioni" }
        ];

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            height="70vh"
            textAlign="center"
        >
            <Typography variant="h4" mb={4} color="primary">
                {loading
                    ? "Caricamento..."
                    : `Benvenut${schoolUser?.male ? 'o' : 'a'}, ${schoolUser?.name} ${schoolUser?.surname}!`}
            </Typography>

            <Stack
                direction="column"
                spacing={3}
                alignItems="center"
                style={{ cursor: "pointer" }}
                justifyContent="center"
            >
                <Stack direction="row" spacing={3}>
                    {menuItems.slice(0, 2).map((item, index) => (
                        <Box
                            key={index}
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            className={'hover-animation'}
                            sx = {{padding: '1rem', borderRadius: '1rem'}}
                            onClick={() => navigate(item.path)}
                        >
                            <img
                                src={item.img}
                                alt={`${item.label} icon`}
                                style={{
                                    width: '120px',
                                    height: '120px',
                                    objectFit: 'contain'
                                }}
                            />
                            <Typography
                                variant="h6"
                                color={item.color}
                                mt={1}
                            >
                                {item.label}
                            </Typography>
                        </Box>
                    ))}
                </Stack>

                <Stack direction="row" spacing={3}>
                    {menuItems.slice(2).map((item, index) => (
                        <Box
                            key={index}
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            className={'hover-animation'}
                            sx = {{padding: '1rem', borderRadius: '1rem'}}
                            onClick={() => navigate(item.path)}
                        >
                            <img
                                src={item.img}
                                alt={`${item.label} icon`}
                                style={{
                                    width: '120px',
                                    height: '120px',
                                    objectFit: 'contain'
                                }}
                            />
                            <Typography
                                variant="h6"
                                color={item.color}
                                mt={1}
                            >
                                {item.label}
                            </Typography>
                        </Box>
                    ))}
                </Stack>
            </Stack>
        </Box>
    );
}
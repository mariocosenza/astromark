import React, { useEffect } from "react";
import { Box, Stack, Typography, useTheme } from "@mui/material";
import { AxiosResponse } from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import { Env } from "../../Env.ts";
import { SchoolUserDetail } from "../../components/AccountMenu.tsx";
import timetable from "../../assets/timetable.png";
import classroom from "../../assets/classroom.png";
import ticket from "../../assets/ticket.png";
import setting from "../../assets/setting.png";
import {useNavigate} from "react-router";

export const SecretaryDashboard = () => {
    const theme = useTheme();
    const [loading, setLoading] = React.useState<boolean>(true);
    const [schoolUser, setSchoolUser] = React.useState<SchoolUserDetail>();
    const navigation = useNavigate();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const schoolClass: AxiosResponse<SchoolUserDetail> = await axiosConfig.get(`${Env.API_BASE_URL}/school-users/detailed`);
            setSchoolUser(schoolClass.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }

    const menuItems = [
        { img: timetable, label: "Orario", color: theme.palette.primary.main },
        { img: classroom, label: "Gestione classi", color: theme.palette.primary.main },
        { img: ticket, label: "Ticket", color: theme.palette.info.main },
        { img: setting, label: "Impostazioni", color: theme.palette.info.main }
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
                {loading ? "Caricamento..." : `Benvenuto, ${schoolUser?.name} ${schoolUser?.surname}!`}
            </Typography>

            <Stack
                direction="column"
                spacing={3}
                alignItems="center"
                style={{cursor: "pointer"}}
                justifyContent="center"
            >
                <Stack direction="row" spacing={6}>
                    {menuItems.slice(0, 2).map((item, index) => (
                        <Box
                            key={index}
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            onClick={() => item.label === "Orario" ? navigation("/secretary/timetable") : navigation("/secretary/manage-class")}
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

                <Stack direction="row" spacing={6}>
                    {menuItems.slice(2).map((item, index) => (
                        <Box
                            key={index}
                            display="flex"
                            flexDirection="column"
                            alignItems="center"
                            onClick={() => item.label === "Ticket" ? navigation("/secretary/tickets") : navigation("/secretary/impostazioni")}
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
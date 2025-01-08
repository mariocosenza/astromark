import React, {useEffect, useState} from "react";
import {
    CircularProgress,
    Typography,
} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig.ts";
import {getId} from "../../services/AuthService.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import {SchoolClassResponse} from "../../entities/SchoolClassResponse.ts";
import {Item, RectangleList} from "../../components/RectangleList.tsx";


export const SchoolClass: React.FC = () => {
    const [data, setData] = useState<Item[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<SchoolClassResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/teachers/${getId()}/schoolClasses`);
            const re = response.data;
            const correctedData: Item[] = re.map((schoolClass: SchoolClassResponse) => ({
                id: schoolClass.id,
                title: schoolClass.number.toString() + schoolClass.letter,
                description: schoolClass.description,
            }));

            setData(correctedData);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div>
            <TeacherDashboardNavbar/>

            <Typography
                variant="h4"
                sx={{
                    textAlign: "center",
                    marginTop: 4,
                    fontWeight: "bold",
                    color: "#1976d2",
                }}
            >
                Scegli la classe
            </Typography>

            <div style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                overflowY: 'auto',
                maxHeight: '66vh',
                marginTop: '1vh'
            }}>

                {
                    loading ? <CircularProgress/> : <RectangleList items={data}></RectangleList>
                }

            </div>
        </div>
    );
};
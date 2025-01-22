import React, {useEffect, useState} from "react";
import {CircularProgress, Typography,} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import {TeacherClassResponse} from "../../entities/TeacherClassResponse.ts";
import {GridList, Item} from "../../components/GridList.tsx";
import {useNavigate} from "react-router";
import {SelectedSchoolClass} from "../../services/TeacherService.ts";

export const SchoolClass: React.FC = () => {
    const [data, setData] = useState<Item[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<TeacherClassResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/teachers/schoolClasses`);
            if (response.data.length) {
                const correctedData: Item[] = response.data.map((schoolClass: TeacherClassResponse) => ({
                    id: schoolClass.id,
                    title: schoolClass.number.toString() + schoolClass.letter,
                    desc: schoolClass.description,
                }));

                setData(correctedData);
            }

            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }

    const chooseSchoolClass = (id: number, title: string, desc: string) => {
        SelectedSchoolClass.id = id;
        SelectedSchoolClass.title = title;
        SelectedSchoolClass.desc = desc;
        navigate(`/teacher/agenda`);
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Scegli la classe
            </Typography>

            <div className={'centerBox'} style={{marginTop: '4vh'}}>
                {loading ? <CircularProgress/> :
                    <GridList items={data} onClick={chooseSchoolClass}></GridList>}
            </div>
        </div>
    );
};
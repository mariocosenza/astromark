import React, {useEffect, useState} from "react";

import DatePicker, {DateObject} from "react-multi-date-picker"

import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {InputLabel, Stack, Typography} from "@mui/material";
import {ClassTimeslot, ClassTimeslotProps} from "../../components/ClassTimeslot.tsx";
import {SchoolClass} from "./Communication.tsx";





export const Timetable: React.FC = () => {
    const [data, setData]  = useState<ClassTimeslotProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [toggle, _] = changeStudentOrYear();


    const [values, setValues] = useState<DateObject[]>([
        new DateObject().subtract(6, "days"),
        new DateObject().add(1, "days")
    ])


    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            setLoading(true);
            const schoolClass: AxiosResponse<SchoolClass[]> = await axiosConfig.get(
                `${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`
            );
            const response: AxiosResponse<ClassTimeslotProps[]> = await axiosConfig.get(
                `${Env.API_BASE_URL}/classes/${schoolClass.data[0].id}/week-timeslots/${values[0].format("YYYY-MM-DD")}`
            );

            const array = [...response.data].sort((a, b) => a.hour - b.hour);
            setData(array);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    };

    const change = (value: DateObject[]) => {
        value[0] = value[0].add(1, 'days');
        value[1] = value[1].add(1, 'days');
        setValues(value);
        fetchData();
    }


    return (
        <div>
            <Stack direction={'row'} spacing={2} sx={{justifyContent: 'center', alignItems: 'center'}}>
                <InputLabel sx={{ml: '1rem'}}>
                    Seleziona settimana
                </InputLabel>
                <DatePicker
                    style={{ marginTop: '1rem', marginLeft: '1rem', marginBottom: '1rem' }}
                    range
                    weekPicker
                    value={values}
                    onChange={change}
                />

            </Stack>
            <Stack
                spacing={0}
                sx={{
                    mx: '2vw',
                    mt: '2rem',
                    display: 'flex',
                    flexDirection: 'row',
                    flexWrap: 'wrap',
                    gap: 0
                }}
            >
                {[...Array(6)].map((_, dayIndex) => (
                    <Stack
                        key={`day-${dayIndex}`}
                        spacing={2}
                        direction="column"
                        sx={{
                            flex: '1 1 250px',
                            maxWidth: '400px',
                            mt: '1rem !important'
                        }}
                    >
                        <Typography
                            className={'titleTable'}
                            variant="h5"
                            sx={{
                                display: 'flex',
                                alignItems: 'center'
                            }}
                        >
                            {['Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato'][dayIndex]}
                        </Typography>

                        {!loading ? (
                            [...Array(8)].map((_, hourIndex) => {
                                // Find any existing slot for this dayIndex and hour
                                const slot = data.find(
                                    (item) =>
                                        item.date?.getDay() === dayIndex && item.hour === hourIndex + 1
                                );

                                return (
                                    <ClassTimeslot
                                        key={`day-${dayIndex}-hour-${hourIndex}`}
                                        hour={hourIndex + 1}
                                        title={slot?.title || null}
                                        date={slot?.date || null}
                                    />
                                );
                            })
                        ) : (
                            <div>Loading...</div>
                        )}
                    </Stack>
                ))}
            </Stack>
        </div>
    );
}
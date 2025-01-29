import React, {useEffect, useState} from "react";
import DatePicker, {DateObject} from "react-multi-date-picker"
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {InputLabel, Stack, Typography} from "@mui/material";
import {ClassTimeslot, ClassTimeslotProps} from "../../components/ClassTimeslot.tsx";
import {SchoolClass} from "./Communication.tsx";

/**
 * Type definition for week day indices
 * Maps Monday-Saturday to 0-5
 */
type WeekDayIndex = 0 | 1 | 2 | 3 | 4 | 5;

/**
 * Mapping of day indices to their respective names in Italian
 * Provides type-safe access to day names
 */
const WEEK_DAYS: Record<WeekDayIndex, string> = {
    0: 'Lunedì',
    1: 'Martedì',
    2: 'Mercoledì',
    3: 'Giovedì',
    4: 'Venerdì',
    5: 'Sabato'
};

/**
 * Timetable component that displays weekly class schedule
 * Allows selection of week range and displays classes for each day
 */
export const Timetable: React.FC = () => {
    const [data, setData] = useState<ClassTimeslotProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [toggle, _] = changeStudentOrYear();

    const [values, setValues] = useState<DateObject[]>([
        new DateObject().subtract(6, "days"),
        new DateObject().add(1, "days")
    ])

    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async (selectedValues?: DateObject[]) => {
        try {
            setLoading(true);
            const schoolClass: AxiosResponse<SchoolClass[]> = await axiosConfig.get(
                `${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`
            );

            const dateForFetch = selectedValues ? selectedValues[0] : values[0];

            const response: AxiosResponse<ClassTimeslotProps[]> = await axiosConfig.get(
                `${Env.API_BASE_URL}/classes/${schoolClass.data[0].id}/week-timeslots/${dateForFetch.format("YYYY-MM-DD")}`
            );

            const array = [...response.data].sort((a, b) => a.hour - b.hour);
            setData(array);
            setLoading(false);
        } catch (error) {
            console.error(error);
            setLoading(false);
        }
    };

    const change = (value: DateObject[]) => {
        value[0] = value[0].add(1, 'days');
        value[1] = value[1].add(1, 'days');
        setValues(value);
        setLoading(true);
        fetchData(value);
    };


    /**
     * Converts JavaScript Date day index (0-6, Sunday-Saturday)
     * to custom week day index (0-5, Monday-Saturday)
     * @param dateStr - JavaScript Date string to convert
     * @returns WeekDayIndex - Custom day index (0-5)
     */
    const convertJSDateToDayIndex = (dateStr: string): WeekDayIndex => {
        const date = new Date(dateStr);
        console.log('Original date:', dateStr, 'JS Day:', date.getDay());

        // JavaScript getDay() returns 0-6 (Sunday-Saturday)
        // We need to convert to 0-5 (Monday-Saturday)
        const jsDay = date.getDay(); // 0 is Sunday, 1 is Monday, etc.
        let dayIndex: number;

        if (jsDay === 0) { // Sunday
            dayIndex = 6;
        } else {
            dayIndex = jsDay - 1;
        }

        // Ensure we stay within 0-5 range
        dayIndex = dayIndex > 5 ? 0 : dayIndex;

        console.log('Converted to dayIndex:', dayIndex);
        return dayIndex as WeekDayIndex;
    };

    return (
        <div>
            <Stack direction={'row'} spacing={2} sx={{justifyContent: 'center', alignItems: 'center'}}>
                <InputLabel sx={{ml: '1rem'}}>
                    Seleziona settimana
                </InputLabel>
                <DatePicker
                    style={{marginTop: '1rem', marginLeft: '1rem', marginBottom: '1rem', height: '2.5rem'}}
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
                {(Object.keys(WEEK_DAYS) as unknown as WeekDayIndex[]).map((dayIndex) => (
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
                            {WEEK_DAYS[dayIndex]}
                        </Typography>

                        {!loading ? (
                            [...Array(8)].map((_, hourIndex) => {
                                // Find slot matching the weekday index and hour
                                const slot = data.find(item => {
                                    if (!item.date) return false;
                                    console.log('Checking slot:', {
                                        date: item.date,
                                        hour: item.hour,
                                        title: item.title
                                    });
                                    const itemDayIndex = convertJSDateToDayIndex(item.date);
                                    const matches = itemDayIndex === Number(dayIndex) && item.hour === hourIndex + 1;
                                    console.log('Comparison:', {
                                        itemDayIndex,
                                        dayIndex: Number(dayIndex),
                                        matches
                                    });
                                    return matches;
                                });

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
                            <div>Caricamento in corso...</div>
                        )}
                    </Stack>
                ))}
            </Stack>
        </div>
    );
}
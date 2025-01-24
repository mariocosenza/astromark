import React, {useEffect, useState} from "react";
import {Button, CircularProgress, IconButton, Stack, TableContainer, Typography,} from "@mui/material";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import DatePicker, {DateObject} from "react-multi-date-picker";
import Grid from "@mui/material/Grid2";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedSchoolClass, SelectedTeaching} from "../../services/TeacherService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";
import {RatingsResponse} from "../../entities/RatingsResponse.ts";
import {RatingComponent} from "../../components/RatingComponent.tsx";
import {useNavigate} from "react-router";

export const formatMark = (num: number) :string => {
    let int = Math.floor(num)
    let fract = num - int;
    let sign = (fract === 0.25) ? '+' : (fract === 0.75) ? '-' : (fract === 0.50) ? '.5' : ''
    return (sign === '-' ? int + 1 : int).toString() + sign
}

export const formatType = (type: string) => {
    switch (type) {
        case 'ORAL':
            return 'Orale'
        case 'WRITTEN':
            return 'Scritto'
        case 'LABORATORY':
            return 'Laboratorio'
        default:
            return ''
    }
}

export type RatingsRow = {
    id: number,
    student: string,
    name: string,
    subject: string,
    mark: number | null,
    type: string,
    desc: string,
    date: DateObject,
}

export const Ratings: React.FC = () => {
    const [rows, setRows] = useState<RatingsRow[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [date, setDate] = useState<DateObject>(new DateObject())
    const [changeView, setChangeView] = useState<boolean>(false)
    const [selected, setSelected] = useState<RatingsRow>()
    const navigate = useNavigate();

    useEffect(() => {
        fetchData(date.format("YYYY-MM-DD"));
    }, []);

    const fetchData = async (selectedDate: string) => {
        try {
            let rowResponse : RatingsRow[] = [];
            const response: AxiosResponse<RatingsResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/classes/${SelectedSchoolClass.id}/ratings/${SelectedTeaching.teaching}/date/${selectedDate}`);
            if (response.data.length){
                rowResponse = response.data.map((mark: RatingsResponse) => ({
                    id: mark.id,
                    student: mark.studentId,
                    name: mark.name + ' ' + mark.surname,
                    subject: mark.subject,
                    mark: mark.mark,
                    type: mark.type,
                    desc: mark.description,
                    date: mark.date,
                }));
            }

            setLoading(false)
            setRows(rowResponse)
        } catch (error) {
            console.error(error);
        }
    }

    const handleMark = (row: RatingsRow)=> {
        setSelected(row)
        setChangeView(true)
    }

    const handleDateChange = (newDate: DateObject | null) => {
        if (newDate) {
            setLoading(true);
            setDate(newDate);
            fetchData(newDate.format("YYYY-MM-DD"));
        }
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Valutazioni
            </Typography>

            {changeView ? (
                <RatingComponent row={selected || rows[0]} date={date}
                                 returnBack={() => {setChangeView(false); fetchData(date.format("YYYY-MM-DD"))}}/>
            ) : (
                <div>
                    <Grid container spacing={8} alignItems={'center'} justifyContent={'center'} margin={'1rem'}>
                        <Grid justifyContent={'center'}>
                            <Typography variant="h6" className="title" fontWeight="bold">
                                {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
                            </Typography>
                        </Grid>

                        <Grid justifyContent={'center'}>
                            <Stack direction={'column'} justifyContent={'center'}>
                                <Typography variant="caption" color={'textSecondary'}>
                                    Data
                                </Typography>
                                <DatePicker
                                    value={date}
                                    onChange={handleDateChange}
                                />
                            </Stack>
                        </Grid>
                        <Grid justifyContent={'center'}>
                            <Button variant="contained" size={'large'} onClick={() => navigate('/teacher/valutazioni/tutte')}
                                    sx={{borderRadius: 4, backgroundColor: 'var(--md-sys-color-primary)'}}>
                                Visualizza tutte le Valutazioni
                            </Button>
                        </Grid>

                    </Grid>

                    <TableContainer sx={{width: '80%', margin: '0 10%'}}>
                        <Table>
                            <TableHead>
                                <CustomTableRow>
                                    <CustomTableCell width={'40%'}>Alunno</CustomTableCell>
                                    <CustomTableCell width={'15%'}>Voto</CustomTableCell>
                                    <CustomTableCell width={'45%'}>Note</CustomTableCell>
                                </CustomTableRow>
                            </TableHead>

                            <TableBody>
                                {loading ? null : rows.map((row) => (
                                    <CustomTableRow key={row.student}>

                                        <CustomTableCell>{row.name}</CustomTableCell>
                                        <CustomTableCell sx={{ color: row.mark ? 'white' : 'black',
                                            backgroundColor: (!row.mark ? '' : row.mark < 6 ? 'var(--md-sys-color-error)' : 'green')}}>
                                            <Stack>
                                                <IconButton color={'inherit'} sx={{borderRadius: 0}} onClick={() => {handleMark(row)}}>
                                                    <Stack>
                                                        <Typography fontSize={'xx-large'} fontWeight={'bold'}>
                                                            {row.mark ? formatMark(row.mark) : '+'}
                                                        </Typography>
                                                        <Typography fontSize={'small'}>
                                                            {formatType(row.type)}
                                                        </Typography>
                                                    </Stack>
                                                </IconButton>
                                            </Stack>
                                        </CustomTableCell>
                                        <CustomTableCell>{row.desc}</CustomTableCell>

                                    </CustomTableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>

                </div>
            )}

            <Grid container justifyContent={'center'}>
                {loading ? <CircularProgress size={150}/> : null}
            </Grid>
        </div>
    );
};
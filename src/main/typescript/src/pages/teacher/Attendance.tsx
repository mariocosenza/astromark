import React, {useEffect, useState} from "react";
import {Button, CircularProgress, Stack, TableContainer, Typography,} from "@mui/material";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import DatePicker, {DateObject} from "react-multi-date-picker";
import Grid from "@mui/material/Grid2";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedSchoolClass} from "../../services/TeacherService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";
import {AttendanceResponse} from "../../entities/AttendanceResponse.ts";
import MeetingRoomOutlinedIcon from '@mui/icons-material/MeetingRoomOutlined';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import {DelayComponent} from "../../components/DelayComponent.tsx";

export type AttendanceRow = {
    id: string;
    name: string;
    isAbsent: boolean;
    isDelayed: boolean;
    buttonRowValue: string
    delayTime: DateObject;
    totalAbsence: number;
    totalDelay: number;
}

export const Attendance: React.FC = () => {
    const [rows, setRows] = useState<AttendanceRow[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [date, setDate] = useState<DateObject>(new DateObject())
    const [changeView, setChangeView] = useState<boolean>(false)

    useEffect(() => {
        fetchData(date.format("YYYY-MM-DD"));
    }, []);

    const fetchData = async (selectedDate: string) => {
        try {
            let rowResponse : AttendanceRow[] = [];
            const response: AxiosResponse<AttendanceResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/classes/${SelectedSchoolClass.id}/attendance/${selectedDate}`);
            if (response.data.length){
                rowResponse = response.data.map((attendance: AttendanceResponse) => ({
                    id: attendance.id,
                    name: attendance.name + ' ' + attendance.surname,
                    isAbsent: attendance.isAbsent,
                    isDelayed: attendance.isDelayed,
                    buttonRowValue: attendance.isAbsent ? 'absent' : (attendance.isDelayed ? 'delayed' : ''),
                    delayTime: attendance.delayTime,
                    totalAbsence: attendance.totalAbsence,
                    totalDelay: attendance.totalDelay,
                }));
            }
            setLoading(false)
            setRows(rowResponse)
        } catch (error) {
            console.error(error);
        }
    }

    const handleDateChange = (newDate: DateObject | null) => {
        if (newDate) {
            setLoading(true);
            setDate(newDate);
            fetchData(newDate.format("YYYY-MM-DD"));
        }
    };

    const buttonToggle = (value: 'absent' | 'delayed', selectedRow: AttendanceRow) => {
        selectedRow.buttonRowValue = selectedRow.buttonRowValue === value ? '' : value;
        console.log(selectedRow.buttonRowValue);
        setRows((prevRows) =>
            prevRows.map((row) =>
                row.id === selectedRow.id ? selectedRow : row))

        setChangeView(selectedRow.buttonRowValue === 'delayed');
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Appello
            </Typography>

            {changeView ? (
                <div>
                    <DelayComponent returnBack={() => {setChangeView(false)}}/>
                </div>
            ) : (
                <div>
                    <Grid container alignItems={'center'} justifyContent={'center'} margin={'1rem'}>
                        <Grid size={2}>
                            <Typography variant="h6" className="title" fontWeight="bold">
                                {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
                            </Typography>
                        </Grid>
                        <Grid>
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
                    </Grid>

                    <TableContainer sx={{width: '80%', margin: '0 10%'}}>
                        <Table>
                            <TableHead>
                                <CustomTableRow>
                                    <CustomTableCell width={'25%'}>Alunno</CustomTableCell>
                                    <CustomTableCell width={'25%'}>Presenza</CustomTableCell>
                                    <CustomTableCell width={'30%'}>Eventi</CustomTableCell>
                                    <CustomTableCell width={'20%'}>Assenze | Ritardi</CustomTableCell>
                                </CustomTableRow>
                            </TableHead>

                            <TableBody>
                                {loading ? null : rows.map((row) => (
                                    <CustomTableRow key={row.id}>

                                        <CustomTableCell>{row.name}</CustomTableCell>
                                        <CustomTableCell>
                                            <Stack direction="row" justifyContent="center" spacing={4}>
                                                <Button
                                                    onClick={() => buttonToggle('absent', row)}
                                                    variant="contained"
                                                    sx={{
                                                        backgroundColor: row.buttonRowValue === "absent" ? 'var(--md-sys-color-error)' : "gray",
                                                        borderRadius: 5,
                                                    }}
                                                >
                                                    Assente
                                                </Button>
                                                <Button
                                                    onClick={() => buttonToggle('delayed', row)}
                                                    variant="contained"
                                                    sx={{
                                                        backgroundColor: row.buttonRowValue === "delayed" ? '#EDC001' : "gray",
                                                        borderRadius: 5,
                                                    }}
                                                >
                                                    Ritardo
                                                </Button>
                                            </Stack>
                                        </CustomTableCell>
                                        <CustomTableCell>{row.isDelayed ? ("Ingresso alle " + row.delayTime.toString().substring(11, 16)) : ''}</CustomTableCell>
                                        <CustomTableCell>
                                            <Stack direction="row" justifyContent="center" spacing={2}>
                                                <Stack alignItems="center">
                                                    <PersonOffIcon color="error" fontSize={'large'}/>
                                                    {row.totalAbsence}
                                                </Stack>
                                                <Stack alignItems="center">
                                                    <MeetingRoomOutlinedIcon sx={{color: '#EDC001'}}
                                                                             fontSize={'large'}/>
                                                    {row.totalDelay}
                                                </Stack>
                                            </Stack>
                                        </CustomTableCell>
                                    </CustomTableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>

                    <Stack direction="row" justifyContent="flex-end" margin={'2rem 10%'}>
                        <Button variant="contained" color="primary" sx={{borderRadius: 5, width: '10%'}}>
                            Salva
                        </Button>
                    </Stack>
                </div>
            )}

            <Grid container justifyContent={'center'}>
                {loading ? <CircularProgress size={150}/> : null}
            </Grid>
        </div>
    );
};
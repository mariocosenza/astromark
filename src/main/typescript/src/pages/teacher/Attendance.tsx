import React, {useEffect, useState} from "react";
import {Button, CircularProgress, Stack, TableContainer, Typography,} from "@mui/material";
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
import {AttendanceRequest} from "../../entities/AttendanceRequest.ts";

export type AttendanceRow = {
    id: string;
    name: string;
    isAbsent: boolean;
    isDelayed: boolean;
    buttonRowValue: string
    delayTimeHour: number
    delayTimeMinute: number
    delayNeedJustification: boolean;
    totalAbsence: number;
    totalDelay: number;
}

export const Attendance: React.FC = () => {
    const [rows, setRows] = useState<AttendanceRow[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [date, setDate] = useState<DateObject>(new DateObject())
    const [dateError, setDateError] = useState<boolean>(false)
    const [changeView, setChangeView] = useState<boolean>(false)
    const [selected, setSelected] = useState<AttendanceRow>()

    useEffect(() => {
        fetchData(date.format("YYYY-MM-DD"));
    }, []);

    const fetchData = async (selectedDate: string) => {
        try {
            let rowResponse: AttendanceRow[] = [];
            if (new DateObject(selectedDate) <= new DateObject()) {
                const response: AxiosResponse<AttendanceResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/classes/${SelectedSchoolClass.id}/attendance/${selectedDate}`);
                if (response.data.length) {
                    rowResponse = response.data.map((attendance: AttendanceResponse) => ({
                        id: attendance.id,
                        name: attendance.name + ' ' + attendance.surname,
                        isAbsent: attendance.isAbsent,
                        isDelayed: attendance.isDelayed,
                        buttonRowValue: attendance.isAbsent ? 'absent' : (attendance.isDelayed ? 'delayed' : ''),
                        delayTimeHour: attendance.delayTime ? new Date((attendance.delayTime).toString()).getHours() : 1,
                        delayTimeMinute: attendance.delayTime ? new Date(attendance.delayTime.toString()).getMinutes() : 0,
                        delayNeedJustification: attendance.delayNeedJustification,
                        totalAbsence: attendance.totalAbsence,
                        totalDelay: attendance.totalDelay,
                    }));

                    rowResponse.forEach((attendance) => {
                        attendance.delayTimeHour = attendance.delayTimeHour - 1;
                    });
                }
                setDateError(false);
            } else {
                setDateError(true);
            }

            setLoading(false)
            setRows(rowResponse)
        } catch (error) {
            console.error(error);
        }
    }

    const handleSave = async () => {

        const attendanceRequest: AttendanceRequest[] = rows.map((attendance: AttendanceRow) => ({
            studentId: attendance.id,
            isAbsent: attendance.buttonRowValue === 'absent',
            isDelayed: attendance.buttonRowValue === 'delayed',
            delayTimeHour: attendance.delayTimeHour,
            delayTimeMinute: attendance.delayTimeMinute,
            delayNeedJustification: attendance.delayNeedJustification,
        }));

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/classes/${SelectedSchoolClass.id}/attendance/${date.format("YYYY-MM-DD")}`, attendanceRequest, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            setLoading(true)
            fetchData(date.format("YYYY-MM-DD"));
        } catch (error) {
            console.log(error);
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

        setRows((prevRows) =>
            prevRows.map((row) =>
                row.id === selectedRow.id ? selectedRow : row))

        if (selectedRow.buttonRowValue === 'delayed') {
            setSelected(selectedRow);
            setChangeView(true);
        }
    };

    return (
        <div>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Appello
            </Typography>

            {changeView ? (
                <div>
                    <DelayComponent row={selected || rows[0]} returnBack={() => {
                        setChangeView(false)
                    }}/>
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
                                    Consegna
                                </Typography>
                                <DatePicker
                                    value={date}
                                    onChange={handleDateChange}/>
                                {dateError && (
                                    <Typography variant="caption" color={'error'}>
                                        È possibile visionare solo date successive ad oggi.
                                    </Typography>
                                )}
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
                                        <CustomTableCell>
                                            {row.isDelayed ? ("Ingresso alle " +
                                                row.delayTimeHour.toString().padStart(2, '0') + ':' +
                                                row.delayTimeMinute.toString().padStart(2, '0')) : ''}
                                        </CustomTableCell>
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
                        <Button variant="contained" color="primary" sx={{borderRadius: 5, width: '10%'}}
                                onClick={handleSave}>
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
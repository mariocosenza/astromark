import React, {useEffect, useState} from "react";
import {CircularProgress, IconButton, Stack, TableContainer, Typography,} from "@mui/material";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DatePicker, {DateObject} from "react-multi-date-picker";
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Grid from "@mui/material/Grid2";
import {AxiosResponse} from "axios";
import {TeachingTimeslotDetailedResponse} from "../../entities/TeachingTimeslotDetailedResponse.ts";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedSchoolClass, SelectedTeachingTimeslot} from "../../services/TeacherService.ts";
import {useNavigate} from "react-router";
import {getId} from "../../services/AuthService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";

export interface ClassAgendaRow {
    id: number
    signed: boolean;
    hour: number;
    isTeacherHour: boolean;
    name: string;
    subject: string;
    activityTitle: string
    activityDesc: string;
    homeworkTitle: string;
    homeworkDesc: string;
    homeworkDate: DateObject;
}

export const ClassAgenda: React.FC = () => {
    const [rows, setRows] = useState<ClassAgendaRow[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [date, setDate] = useState<DateObject>(new DateObject())
    const navigate = useNavigate();

    useEffect(() => {
        fetchData(date.format("YYYY-MM-DD"));
    }, []);

    const fetchData = async (selectedDate: string) => {
        try {
            let rowResponse : ClassAgendaRow[] = [];
            const response: AxiosResponse<TeachingTimeslotDetailedResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/classes/${SelectedSchoolClass.id}/signedHours/${selectedDate}`);
            if (response.data.length){
                rowResponse = response.data.map((teachingSlot: TeachingTimeslotDetailedResponse) => ({
                    id: teachingSlot.id,
                    signed: teachingSlot.signed,
                    hour: teachingSlot.hour,
                    isTeacherHour: teachingSlot.teacherId === getId(),
                    name: teachingSlot.name + ' ' + teachingSlot.surname,
                    subject: teachingSlot.subject,
                    activityTitle: teachingSlot.activityTitle,
                    activityDesc: teachingSlot.activityDescription,
                    homeworkTitle: teachingSlot.homeworkTitle,
                    homeworkDesc: teachingSlot.homeworkDescription,
                    homeworkDate: teachingSlot.homeworkDueDate,
                }));
            }

            setLoading(false);
            setRows(rowResponse)
        } catch (error) {
            console.error(error);
        }
    }

    const choseTeachingTimeslot = (slot: ClassAgendaRow) => {
        SelectedTeachingTimeslot.setSlot(slot);
        navigate(`/teacher/ora`);
    };

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
                Agenda di Classe
            </Typography>

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

            <TableContainer sx={{ width: '90%', margin: '0 5%'}}>
                <Table>
                    <TableHead>
                        <CustomTableRow>
                            <CustomTableCell width={'8%'}>Firma</CustomTableCell>
                            <CustomTableCell width={'13%'}>Docente</CustomTableCell>
                            <CustomTableCell width={'13%'}>Materia</CustomTableCell>
                            <CustomTableCell width={'30%'}>Attività Svolta</CustomTableCell>
                            <CustomTableCell width={'30%'}>Compiti Assegnati</CustomTableCell>
                            <CustomTableCell width={'6%'}></CustomTableCell>
                        </CustomTableRow>
                    </TableHead>

                    <TableBody>
                        { loading ? null : rows.map((row) => (
                            <CustomTableRow key={row.hour}>
                                <CustomTableCell padding={'none'}>
                                    <Stack direction={'column'} padding={'0.5rem 1rem'} alignItems={'center'}>
                                        {row.signed ? <CheckCircleOutlineIcon fontSize={'large'} color={'success'}/> :
                                            <IconButton disabled={!row.isTeacherHour} onClick={() => choseTeachingTimeslot(row)}>
                                                <AddCircleOutlineIcon fontSize={'large'}/>
                                            </IconButton>
                                        }

                                        <Typography variant="caption" color={'textSecondary'}>
                                            {(row.hour + 7) + ':00 - ' + (row.hour + 8) + ':00'}
                                        </Typography>
                                    </Stack>
                                </CustomTableCell>

                                <CustomTableCell>{row.name}</CustomTableCell>
                                <CustomTableCell>{row.subject}</CustomTableCell>

                                <CustomTableCell>
                                    <Typography fontWeight={'bold'} fontSize={'large'}>
                                        {row.activityTitle + (row.activityTitle === '' ? '' : ':')}
                                    </Typography>
                                    {row.activityDesc}
                                </CustomTableCell>

                                <CustomTableCell>
                                    <Typography fontWeight={'bold'} fontSize={'large'}>
                                        {row.homeworkTitle + (row.homeworkTitle === '' ? '' : ':')}
                                    </Typography>
                                    {row.homeworkDesc}
                                    <Typography fontSize={'small'} color={'textSecondary'}>
                                        {row.homeworkDate ? 'Per il ' + row.homeworkDate.toString() : ''}
                                    </Typography>
                                </CustomTableCell>

                                <CustomTableCell align={'center'}>
                                    <IconButton disabled={!(row.signed && row.isTeacherHour)} onClick={() => choseTeachingTimeslot(row)}>
                                        <EditOutlinedIcon fontSize={'large'}/>
                                    </IconButton>
                                </CustomTableCell>
                            </CustomTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Grid container justifyContent={'center'} margin={'5%'}>
                { loading ? <CircularProgress size={150}/>  : null}
            </Grid>
        </div>
    );
};
import React, {useEffect, useState} from "react";
import {CircularProgress, IconButton, Stack, TableContainer, Typography,} from "@mui/material";
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
import {SelectedHomework, SelectedSchoolClass, SelectedTeachingTimeslot} from "../../services/TeacherService.ts";
import {useNavigate} from "react-router";
import {getId} from "../../services/AuthService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";
import ForumIcon from '@mui/icons-material/Forum';


export const ClassAgenda: React.FC = () => {
    const [rows, setRows] = useState<TeachingTimeslotDetailedResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [date, setDate] = useState<DateObject>(new DateObject())
    const navigate = useNavigate();

    useEffect(() => {
        fetchData(date.format("YYYY-MM-DD"));
    }, []);

    const fetchData = async (selectedDate: string) => {
        try {
            const response: AxiosResponse<TeachingTimeslotDetailedResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/classes/${SelectedSchoolClass.id}/signedHours/${selectedDate}`);
            setRows(addEmptyHour(response.data))
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }

    const addEmptyHour = (rows: TeachingTimeslotDetailedResponse[]) => {
        let newRows: TeachingTimeslotDetailedResponse[] = [];
        let i = 0;
        for (let hour = 1; hour < 8; hour++) {
            if (i < rows.length && rows[i].hour == hour) {
                newRows.push(rows[i]);
                i++;
            } else {
                newRows.push({
                    id: null,
                    hour: hour,
                    subject: '',
                    signed: false,
                    teacher: {
                        id: '',
                        name: '',
                        surname: '',
                    },
                    activity: null,
                    homework: null,
                })
            }
        }

        return newRows;
    }

    const choseTeachingTimeslot = (slot: TeachingTimeslotDetailedResponse) => {
        SelectedTeachingTimeslot.setSlot(slot);
        SelectedTeachingTimeslot.date = date;
        navigate(`/teacher/ora`);
    };

    const choseHomeworkChat = (slot: TeachingTimeslotDetailedResponse) => {
        if (slot.homework && slot.homework.hasChat) {
            SelectedHomework.id = slot.homework.id;
            SelectedHomework.title = slot.homework.title;
            SelectedHomework.desc = slot.homework.description;
            navigate(`/teacher/agenda/chat`);
        }
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

            <TableContainer sx={{width: '90%', margin: '0 5%'}}>
                <Table>
                    <TableHead>
                        <CustomTableRow>
                            <CustomTableCell width={'8%'}>Firma</CustomTableCell>
                            <CustomTableCell width={'13%'}>Docente</CustomTableCell>
                            <CustomTableCell width={'10%'}>Materia</CustomTableCell>
                            <CustomTableCell width={'31%'}>Attività Svolta</CustomTableCell>
                            <CustomTableCell width={'32%'}>Compiti Assegnati</CustomTableCell>
                            <CustomTableCell width={'6%'}></CustomTableCell>
                        </CustomTableRow>
                    </TableHead>

                    <TableBody>
                        {loading ? null : rows.map((row) => (
                            <CustomTableRow key={row.hour}>
                                <CustomTableCell padding={'none'}>
                                    <Stack direction={'column'} padding={'0.5rem 1rem'} alignItems={'center'}>
                                        {row.signed ? <CheckCircleOutlineIcon fontSize={'large'} color={'success'}
                                                                              sx={{padding: '8px'}}/> :
                                            <IconButton disabled={!(!row.teacher.id || getId() === row.teacher.id)}
                                                        onClick={() => choseTeachingTimeslot(row)}>
                                                <AddCircleOutlineIcon fontSize={'large'}/>
                                            </IconButton>
                                        }

                                        <Typography variant="caption" color={'textSecondary'}>
                                            {row.hour + ' ora'}
                                        </Typography>
                                    </Stack>
                                </CustomTableCell>

                                <CustomTableCell>{row.teacher.name + ' ' + row.teacher.surname}</CustomTableCell>
                                <CustomTableCell>{row.subject}</CustomTableCell>

                                <CustomTableCell>
                                    <Typography fontWeight={'bold'} fontSize={'large'}>
                                        {row.activity ? row.activity.title + ':' : ''}
                                    </Typography>
                                    {row.activity?.description}
                                </CustomTableCell>

                                <CustomTableCell>

                                    <Stack direction={'row'} justifyContent={'space-between'} alignItems={'center'}>
                                        <Grid>
                                            <Typography fontWeight={'bold'} fontSize={'large'}>
                                                {row.homework ? row.homework.title + ':' : ''}
                                            </Typography>
                                            {row.homework?.description}
                                            <Typography fontSize={'small'} color={'textSecondary'}>
                                                {row.homework ? 'Per il ' + row.homework.dueDate.toString() : ''}
                                            </Typography>
                                        </Grid>
                                        {row.homework?.hasChat && (
                                            <IconButton disabled={getId() !== row.teacher.id}
                                                        onClick={() => choseHomeworkChat(row)}>
                                                <ForumIcon/>
                                            </IconButton>
                                        )}
                                    </Stack>
                                </CustomTableCell>

                                <CustomTableCell align={'center'}>
                                    <IconButton disabled={!(row.signed && getId() === row.teacher.id)}
                                                onClick={() => choseTeachingTimeslot(row)}>
                                        <EditOutlinedIcon fontSize={'large'}/>
                                    </IconButton>
                                </CustomTableCell>
                            </CustomTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Grid container justifyContent={'center'} margin={'5%'}>
                {loading ? <CircularProgress size={150}/> : null}
            </Grid>
        </div>
    );
};
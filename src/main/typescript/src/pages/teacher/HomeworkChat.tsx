import React, {useEffect, useState} from "react";
import {Card, CardContent, CircularProgress, IconButton, Stack, TableContainer, Typography,} from "@mui/material";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import Grid from "@mui/material/Grid2";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedSchoolClass, SelectedTeachingTimeslot} from "../../services/TeacherService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";
import {SchoolClassStudentResponse} from "../../entities/SchoolClassStudentResponse.ts";
import SmsIcon from '@mui/icons-material/Sms';
import {ChatHomeworkComponent} from "../../components/ChatHomework.tsx";


export const HomeworkChat: React.FC = () => {
    const [rows, setRows] = useState<SchoolClassStudentResponse[]>([]);
    const [homeworkId, setHomeworkId] = useState<number>()
    const [loading, setLoading] = useState<boolean>(true);
    const [selected, setSelected] = useState<SchoolClassStudentResponse | null>(null)

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const rowResponse: AxiosResponse<SchoolClassStudentResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/class-management/${SelectedSchoolClass.id}/students`);
            if (rowResponse.data.length) {
                setRows(rowResponse.data)
                setHomeworkId(SelectedTeachingTimeslot.homework?.id)
            }

            setLoading(false)
        } catch (error) {
            console.error(error);
        }
    }

    const handleToggle = (row: SchoolClassStudentResponse) => {
        setSelected(selected ? null : row)
    }

    return (
        <div>
            <Typography variant="h3" className="title" fontWeight="bold" marginTop={'revert'}>
                Chat
            </Typography>
            <Typography variant="h6" className="title" fontWeight="bold">
                {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
            </Typography>

            <Card elevation={10} sx={{margin: '2rem 30%', borderRadius: 2}}>
                <CardContent>
                    <Stack direction="column" justifyContent={'space-between'} alignItems={'center'}>
                        <Typography variant="h5" fontWeight="bold">
                            {SelectedTeachingTimeslot.homework?.title}
                        </Typography>
                        {SelectedTeachingTimeslot.homework?.description}
                    </Stack>
                </CardContent>
            </Card>

            <Stack direction={'row'} justifyContent={'space-evenly'} sx={{padding: '2rem'}}>
                <TableContainer sx={{width: (selected ? '40' : '60') + '%'}}>
                    <Table>
                        <TableHead>
                            <CustomTableRow>
                                <CustomTableCell width={'40%'}>Cognome</CustomTableCell>
                                <CustomTableCell width={'40%'}>Nome</CustomTableCell>
                                <CustomTableCell width={'20%'}></CustomTableCell>
                            </CustomTableRow>
                        </TableHead>

                        <TableBody>
                            {loading ? null : rows.map((row) => (
                                <CustomTableRow key={row.id}>
                                    <CustomTableCell>
                                        <Typography fontWeight="bold" fontSize={'x-large'}>
                                            {row.surname}
                                        </Typography>
                                    </CustomTableCell>
                                    <CustomTableCell>
                                        <Typography fontWeight="bold" fontSize={'x-large'}>
                                            {row.name}
                                        </Typography>
                                    </CustomTableCell>
                                    <CustomTableCell>
                                        <IconButton size={'large'} onClick={() => handleToggle(row)}>
                                            <SmsIcon/>
                                        </IconButton>
                                    </CustomTableCell>
                                </CustomTableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>

                {!loading && selected && homeworkId && (
                    <ChatHomeworkComponent homeworkId={homeworkId} studentId={selected.id}/>
                )}

            </Stack>

            <Grid container justifyContent={'center'}>
                {loading ? <CircularProgress size={150}/> : null}
            </Grid>
        </div>
    );
};
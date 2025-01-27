import React, {useEffect, useState} from 'react';
import {Autocomplete, Box, Button, Card, CardContent, Stack, TextField, Typography} from '@mui/material';
import DatePicker, {DateObject} from "react-multi-date-picker";
import Grid from "@mui/material/Grid2";
import {SelectedSchoolClass} from "../../services/TeacherService.ts";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SchoolClassStudentResponse} from "../../entities/SchoolClassStudentResponse.ts";
import {NoteRequest} from "../../entities/NoteRequest.ts";
import {useNavigate} from "react-router";

export type StudentData = {
    id: string;
    name: string;
}

export const TeacherNote: React.FC = () => {
    const [students, setStudents] = useState<StudentData[]>([])
    const [selectedStudent, setSelectedStudent] = useState<StudentData | null>(null);
    const [note, setNote] = useState<string>("");
    const [date, setDate] = useState<DateObject>(new DateObject())
    const [loading, setLoading] = useState<boolean>(false);
    const navigate = useNavigate();

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            let studentResponse: StudentData[] = [];
            const response: AxiosResponse<SchoolClassStudentResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/class-management/${SelectedSchoolClass.id}/students`);
            if (response.data.length) {
                studentResponse = response.data.map((student: SchoolClassStudentResponse) => ({
                    id: student.id,
                    name: student.name + ' ' + student.surname,
                }));

                setStudents(studentResponse);
            }

            setLoading(false)
        } catch (error) {
            console.error(error);
        }
    }

    const handleSave = async () => {
        if (selectedStudent && date) {
            const noteRequest: NoteRequest = {
                studentId: selectedStudent.id,
                description: note,
                date: date.toDate(),
            }

            try {
                await axiosConfig.post(`${Env.API_BASE_URL}/students/note/create`, noteRequest, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                navigate('/teacher/dashboard')
            } catch (e) {
                console.log(e)
            }
        }
    }

    const handleDateChange = (newDate: DateObject | null) => {
        if (newDate) {
            setDate(newDate);
        }
    };


    return (
        <div>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Note
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

            <Card elevation={10} sx={{margin: '2rem 20%', borderRadius: 6}}>
                <CardContent>
                    <Stack direction={'row'} justifyContent={'space-around'} alignItems={'center'}>
                        <Stack margin={2} spacing={1} sx={{width: '30%'}}>
                            <Typography variant="subtitle1">
                                Inserisci Nome:
                            </Typography>

                            <Autocomplete
                                options={students}
                                getOptionLabel={(option) => option.name}
                                value={selectedStudent}
                                onChange={(_event, newValue) => setSelectedStudent(newValue)}
                                renderInput={(params) => (
                                    <TextField {...params} placeholder="Inserisci Nome" variant="outlined"/>
                                )}
                                fullWidth
                            />
                        </Stack>

                        <Stack direction="column" justifyContent={'space-around'} alignItems={'center'} spacing={2}
                               sx={{width: '40%'}}>
                            <Box width={'100%'}>
                                <Typography variant="subtitle1">Nota:</Typography>
                                <TextField
                                    value={note}
                                    onChange={(e) => setNote(e.target.value)}
                                    placeholder="Scrivi una nota..."
                                    multiline
                                    rows={4}
                                    fullWidth
                                />
                            </Box>
                        </Stack>
                    </Stack>
                    {!loading && (
                        <Stack justifyContent={'flex-end'} margin={2} paddingRight={'5rem'}>
                            <Button variant="contained" color="primary"
                                    sx={{alignSelf: 'flex-end', borderRadius: 5, justifySelf: ''}} onClick={handleSave}>
                                Salva
                            </Button>
                        </Stack>
                    )}
                </CardContent>
            </Card>
        </div>
    );
};

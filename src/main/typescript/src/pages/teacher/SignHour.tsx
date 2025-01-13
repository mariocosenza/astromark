import React, {useState} from "react";
import {Button, Card, CardContent, Divider, Stack, TextField, Typography,} from "@mui/material";
import DatePicker, {DateObject} from "react-multi-date-picker";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import {SelectedSchoolClass, SelectedTeachingTimeslot} from "../../services/TeacherService.ts";

export const SignHour: React.FC = () => {
    const [date, setDate] = useState<DateObject>(new DateObject().setDate(new Date(2025, 0, 15)))
    const [activityTitle, setActivityTitle] = React.useState<string>(SelectedTeachingTimeslot.activityTitle);
    const [activityDesc, setActivityDesc] = React.useState<string>(SelectedTeachingTimeslot.activityDesc);
    const [homeworkTitle, setHomeworkTitle] = React.useState<string>(SelectedTeachingTimeslot.homeworkTitle);
    const [homeworkDesc, setHomeworkDesc] = React.useState<string>(SelectedTeachingTimeslot.homeworkDesc);

    const handleSave = () => {
        console.log(activityTitle);
        console.log(activityDesc);
        console.log(homeworkTitle);
        console.log(homeworkDesc);
        console.log(date.format("YYYY-MM-DD"));
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
            </Typography>

            <Card elevation={10} sx={{padding: 2, margin: 5, borderRadius: 7}}>
                <CardContent>
                    <Stack spacing={4} direction={{xs: "column", md: "row"}}
                           divider={<Divider orientation="vertical" flexItem />}>

                        <Stack spacing={3} flex={1}>
                            <Stack direction={'column'} justifyContent={'center'}>
                                <Typography variant="subtitle1">
                                    Titolo
                                </Typography>
                                <TextField fullWidth variant="outlined" value={activityTitle}
                                           onChange={(e) => setActivityTitle(e.target.value)}/>
                            </Stack>

                            <Stack direction={'column'} justifyContent={'center'}>
                                <Typography variant="subtitle1">
                                    Attività Svolta
                                </Typography>
                                <TextField fullWidth variant="outlined" multiline rows={4} value={activityDesc}
                                           onChange={(e) => setActivityDesc(e.target.value)}/>
                            </Stack>
                        </Stack>

                        <Stack spacing={3} flex={1}>
                            <Stack direction={'column'} justifyContent={'center'}>
                                <Typography variant="subtitle1">
                                    Titolo
                                </Typography>
                                <TextField fullWidth variant="outlined" value={homeworkTitle}
                                           onChange={(e) => setHomeworkTitle(e.target.value)}/>
                            </Stack>

                            <Stack direction={'column'} justifyContent={'center'}>
                                <Typography variant="subtitle1">
                                    Compiti Assegnati
                                </Typography>
                                <TextField fullWidth variant="outlined" multiline rows={4} value={homeworkDesc}
                                           onChange={(e) => setHomeworkDesc(e.target.value)}/>
                            </Stack>

                            <Stack direction={'column'} justifyContent={'center'}>
                                <Typography variant="caption" color={'textSecondary'}>
                                    Consegna
                                </Typography>
                                <DatePicker
                                    value={date}
                                    onChange={(newDate: DateObject) => {
                                        newDate ? setDate(newDate) : null
                                    }}
                                />
                            </Stack>
                        </Stack>
                    </Stack>

                    <Stack direction="row" justifyContent="flex-end" spacing={2} mt={4}>
                        <Button variant="contained" color="error" sx={{borderRadius: 5}}>
                            Elimina
                        </Button>
                        <Button variant="contained" color="primary" sx={{borderRadius: 5}} onClick={handleSave}>
                            Salva
                        </Button>
                    </Stack>
                </CardContent>
            </Card>
        </div>
    );
};

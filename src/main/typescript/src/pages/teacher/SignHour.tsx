import React, {useState} from "react";
import {Button, Card, CardContent, Divider, FormControlLabel, Radio, RadioGroup, Stack, TextField, Typography,} from "@mui/material";
import DatePicker, {DateObject} from "react-multi-date-picker";
import {SelectedSchoolClass, SelectedTeaching, SelectedTeachingTimeslot} from "../../services/TeacherService.ts";
import {useFormik} from "formik";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import YupPassword from "yup-password";
import * as yup from "yup";
import {useNavigate} from "react-router";
import {ClassActivityRequest, HomeworkRequest, SignHourRequest} from "../../entities/SignHourRequest.ts";

YupPassword(yup)

const validationSchema = yup.object({
    activityTitle: yup.string().test(
        'activityTitle-required-if-desc',
        'Titolo attività obbligatoria se la descrizione è presente',
        function (value) {
            return !!value || !this.parent.activityDesc;
        }
    ),

    homeworkTitle: yup.string().test(
        'homeworkTitle-required-if-desc',
        'Titolo compiti obbligatoria se la descrizione è presente',
        function (value) {
            return !!value || !this.parent.homeworkDesc;
        }
    ),
});

export const SignHour: React.FC = () => {
    const [dueDate, setDueDate] = useState<DateObject>(SelectedTeachingTimeslot.homework?.dueDate || new DateObject().add(1, 'days'))
    const [needChat, setNeedChat] = useState<boolean>(!!SelectedTeachingTimeslot.homework?.hasChat)
    const [dateError, setDateError] = useState<boolean>(false)
    const navigate = useNavigate();

    const initialValues = {
        activityTitle: SelectedTeachingTimeslot.activity?.title || '',
        activityDesc: SelectedTeachingTimeslot.activity?.description || '',
        homeworkTitle: SelectedTeachingTimeslot.homework?.title || '',
        homeworkDesc: SelectedTeachingTimeslot.homework?.description || '',
    };

    const formik = useFormik({
        initialValues,
        validationSchema,
        onSubmit: async (values) => {
            if (!dateError && SelectedTeachingTimeslot.date) {

                let activity: ClassActivityRequest | null = null
                if (values.activityTitle) {
                    activity = {
                        id: SelectedTeachingTimeslot.activity?.id || null,
                        title: values.activityTitle,
                        description: values.activityDesc,
                    }
                }

                let homework: HomeworkRequest | null = null
                if (values.homeworkTitle) {
                    homework = {
                        id: SelectedTeachingTimeslot.homework?.id || null,
                        title: values.homeworkTitle,
                        description: values.homeworkDesc,
                        dueDate: dueDate.toDate(),
                        hasChat: needChat,
                    }
                }

                const signHourRequest: SignHourRequest = {
                    id: SelectedTeachingTimeslot.id,
                    hour: SelectedTeachingTimeslot.hour,
                    subject: SelectedTeaching.teaching,
                    date: SelectedTeachingTimeslot.date.toDate(),
                    activity: activity,
                    homework: homework,
                };

                try {
                    await axiosConfig.post(`${Env.API_BASE_URL}/classes/${SelectedSchoolClass.id}/signHour`, signHourRequest, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });

                    navigate(`/teacher/agenda`);
                } catch (error) {
                    console.error(error);
                }
            }
        },
    });

    const handleDateChange = (newDate: DateObject | null) => {
        if (newDate) {
            setDueDate(newDate)
            setDateError(newDate <= new DateObject())
        }
    };

    return (
        <div>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
            </Typography>

            <Card elevation={10} sx={{padding: 2, margin: 5, borderRadius: 7}}>
                <CardContent>
                    <form onSubmit={formik.handleSubmit}>
                        <Stack spacing={4} direction={{xs: 'column', md: 'row'}}
                               divider={<Divider orientation="vertical" flexItem/>}>

                            <Stack spacing={3} flex={1}>
                                <TextField fullWidth variant="outlined" label="Titolo" name="activityTitle"
                                           value={formik.values.activityTitle}
                                           onChange={formik.handleChange}
                                           error={!!formik.errors.activityTitle}
                                           helperText={formik.errors.activityTitle}/>

                                <TextField fullWidth variant="outlined" multiline rows={4} label="Attività Svolta"
                                           name="activityDesc"
                                           value={formik.values.activityDesc}
                                           onChange={formik.handleChange}/>
                            </Stack>
                            <Stack spacing={3} flex={1}>
                                <TextField fullWidth variant="outlined" label="Titolo Compiti" name="homeworkTitle"
                                           value={formik.values.homeworkTitle}
                                           onChange={formik.handleChange}
                                           error={!!formik.errors.homeworkTitle}
                                           helperText={formik.errors.homeworkTitle}/>

                                <TextField fullWidth variant="outlined" multiline rows={4} label="Compiti Assegnati"
                                           name="homeworkDesc"
                                           value={formik.values.homeworkDesc}
                                           onChange={formik.handleChange}/>

                                <Stack direction={'row'} spacing={4}>
                                    <Stack direction={'column'} justifyContent={'center'}>
                                        <Typography variant="caption" color={'textSecondary'}>
                                            Consegna
                                        </Typography>
                                        <DatePicker
                                            value={dueDate}
                                            onChange={handleDateChange}/>
                                        {dateError && (
                                            <Typography variant="caption" color={'error'}>
                                                È possibile salvare solo date successive ad oggi.
                                            </Typography>
                                        )}
                                    </Stack>
                                    <Stack alignItems={'flex-start'}>
                                        <Typography variant="caption" color={'textSecondary'}>
                                            Abilita chat
                                        </Typography>
                                        <RadioGroup row value={needChat ? 'yes' : 'no'}
                                                    onChange={(e) => setNeedChat(e.target.value === 'yes')}>
                                            <FormControlLabel label='Sì' value='yes' control={<Radio/>}
                                                              disabled={SelectedTeachingTimeslot.homework?.hasChat}/>
                                            <FormControlLabel label='No' value='no' control={<Radio/>}
                                                              disabled={SelectedTeachingTimeslot.homework?.hasChat}/>
                                        </RadioGroup>
                                    </Stack>
                                </Stack>
                            </Stack>
                        </Stack>

                        <Stack direction="row" justifyContent="flex-end" spacing={2} mt={4}>
                            <Button variant="contained" color="error" sx={{borderRadius: 5}} onClick={() => {
                                navigate(`/teacher/agenda`)
                            }}>
                                Elimina
                            </Button>
                            <Button variant="contained" color="primary" sx={{borderRadius: 5}} type={'submit'}>
                                Salva
                            </Button>
                        </Stack>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};

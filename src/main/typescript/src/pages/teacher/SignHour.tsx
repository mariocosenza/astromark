import React, {useState} from "react";
import {Button, Card, CardContent, Divider, Stack, TextField, Typography,} from "@mui/material";
import DatePicker, {DateObject} from "react-multi-date-picker";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import {SelectedSchoolClass, SelectedTeachingTimeslot} from "../../services/TeacherService.ts";
import {useFormik} from "formik";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import YupPassword from "yup-password";
import * as yup from "yup";
import {useNavigate} from "react-router";

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
    const [dueDate, setDueDate] = useState<DateObject>(SelectedTeachingTimeslot.homeworkDate || new DateObject())
    const [dateError, setDateError] = useState<boolean>(false)
    const navigate = useNavigate();

    const initialValues = {
        activityTitle: SelectedTeachingTimeslot.activityTitle,
        activityDesc: SelectedTeachingTimeslot.activityDesc,
        homeworkTitle: SelectedTeachingTimeslot.homeworkTitle,
        homeworkDesc: SelectedTeachingTimeslot.homeworkDesc,
    };

    const formik = useFormik({
        initialValues,
        validationSchema,
        onSubmit: async (values) => {
            if (!dateError) {
                const signHourRequest = {
                    slotId: SelectedTeachingTimeslot.id,
                    activityTitle: values.activityTitle,
                    activityDescription: values.activityDesc,
                    homeworkTitle: values.homeworkTitle,
                    homeworkDescription: values.homeworkDesc,
                    homeworkDueDate: dueDate,
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
            setDateError(newDate < new DateObject())
        }
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
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
                                           onBlur={formik.handleBlur}
                                           error={formik.touched.activityTitle && Boolean(formik.errors.activityTitle)}
                                           helperText={formik.touched.activityTitle && formik.errors.activityTitle}/>

                                <TextField fullWidth variant="outlined" multiline rows={4} label="Attività Svolta" name="activityDesc"
                                           value={formik.values.activityDesc}
                                           onChange={formik.handleChange}
                                           onBlur={formik.handleBlur}
                                           error={formik.touched.activityDesc && Boolean(formik.errors.activityDesc)}
                                           helperText={formik.touched.activityDesc && formik.errors.activityDesc}/>
                            </Stack>
                            <Stack spacing={3} flex={1}>
                                <TextField fullWidth variant="outlined" label="Titolo Compiti" name="homeworkTitle"
                                           value={formik.values.homeworkTitle}
                                           onChange={formik.handleChange}
                                           onBlur={formik.handleBlur}
                                           error={formik.touched.homeworkTitle && Boolean(formik.errors.homeworkTitle)}
                                           helperText={formik.touched.homeworkTitle && formik.errors.homeworkTitle}/>

                                <TextField fullWidth variant="outlined" multiline rows={4} label="Compiti Assegnati" name="homeworkDesc"
                                           value={formik.values.homeworkDesc}
                                           onChange={formik.handleChange}
                                           onBlur={formik.handleBlur}
                                           error={formik.touched.homeworkDesc && Boolean(formik.errors.homeworkDesc)}
                                           helperText={formik.touched.homeworkDesc && formik.errors.homeworkDesc}/>

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
                            </Stack>
                        </Stack>

                        <Stack direction="row" justifyContent="flex-end" spacing={2} mt={4}>
                            <Button variant="contained" color="error" sx={{borderRadius: 5}} onClick={() => {navigate(`/teacher/agenda`)}}>
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

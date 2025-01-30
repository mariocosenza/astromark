import {useParams} from "react-router";
import React, {useEffect, useState} from "react";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Modal,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import {SelectChangeEvent} from "@mui/material/Select";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env.ts";

interface TeachingTimeslotResponse {
    hour: number;
    date: string;
    title: string;
    timeTableId: number;
}

interface TeachingResponse {
    username: string;
    subject: string;
}

interface TimeTableResponse {
    timeTableId: number;
    startDate: string;
    endDate: string;
    number: number;
    letter: string;
    year: number;
}

export const ClassSchedule = () => {
    const {classId} = useParams<{ classId: string }>();
    const [schedule, setSchedule] = useState<TeachingTimeslotResponse[]>([]);
    const [teachings, setTeachings] = useState<TeachingResponse[]>([]);
    const [timetables, setTimetables] = useState<TimeTableResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [modalOpen, setModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        teacherUsername: "",
        timetableId: "",
        dayWeek: "",
        hour: "",
    });

    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [submitError, setSubmitError] = useState<string | null>(null);
    const [validationError, setValidationError] = useState<string | null>(null);

    // Stato per gestire i messaggi di validazione per ogni campo
    const [fieldErrors, setFieldErrors] = useState<{
        teacherUsername?: string;
        timetableId?: string;
        dayWeek?: string;
        hour?: string;
    }>({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (!classId) throw new Error("Class ID is missing.");

                const [scheduleRes, teachingsRes, timetablesRes] = await Promise.all([
                    axiosConfig.get<TeachingTimeslotResponse[]>(
                        `${Env.API_BASE_URL}/classes/${classId}/class-schedule`
                    ),
                    axiosConfig.get<TeachingResponse[]>(
                        `${Env.API_BASE_URL}/class-management/teaching`
                    ),
                    axiosConfig.get<TimeTableResponse[]>(
                        `${Env.API_BASE_URL}/classes/${classId}/timetable`
                    ),
                ]);

                setSchedule(scheduleRes.data);
                setTeachings(teachingsRes.data);
                setTimetables(timetablesRes.data);
                setLoading(false);
            } catch (err) {
                console.error("Failed to fetch data:", err);
                setError("Impossibile recuperare l'orario delle lezioni o i dati correlati.");
                setLoading(false);
            }
        };

        fetchData();
    }, [classId]);

    const groupedSchedule = schedule.reduce<Record<string, TeachingTimeslotResponse[]>>((acc, timeslot) => {
        if (!acc[timeslot.date]) acc[timeslot.date] = [];
        acc[timeslot.date].push(timeslot);
        return acc;
    }, {});

    const handleOpenModal = () => setModalOpen(true);
    const handleCloseModal = () => {
        setFormData({
            teacherUsername: "",
            timetableId: "",
            dayWeek: "",
            hour: "",
        });
        setModalOpen(false);
        setValidationError(null);
        setSubmitError(null);
        setSuccessMessage(null);
        setFieldErrors({});
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData({...formData, [name]: value});

        // Reset field error on change
        setFieldErrors((prev) => ({...prev, [name]: undefined}));
    };

    const handleSelectChange = (e: SelectChangeEvent<string>) => {
        const {name, value} = e.target;
        setFormData({...formData, [name]: value});

        // Reset field error on change
        setFieldErrors((prev) => ({...prev, [name]: undefined}));
    };

    const handleSubmit = async () => {
        // Reset previous messages
        setValidationError(null);
        setSubmitError(null);
        setSuccessMessage(null);
        setFieldErrors({});

        let hasError = false;
        const errors: typeof fieldErrors = {};

        // Validazione dei campi obbligatori
        if (!formData.teacherUsername) {
            errors.teacherUsername = "Seleziona un insegnante.";
            hasError = true;
        }
        if (!formData.timetableId) {
            errors.timetableId = "Seleziona un orario della classe.";
            hasError = true;
        }
        if (!formData.dayWeek) {
            errors.dayWeek = "Inserisci il giorno della settimana.";
            hasError = true;
        } else if (parseInt(formData.dayWeek, 10) < 1 || parseInt(formData.dayWeek, 10) > 6) {
            errors.dayWeek = "Il giorno della settimana deve essere compreso tra 1 e 6.";
            hasError = true;
        }
        if (!formData.hour) {
            errors.hour = "Inserisci l'ora.";
            hasError = true;
        } else if (parseInt(formData.hour, 10) < 1 || parseInt(formData.hour, 10) > 6) {
            errors.hour = "L'ora deve essere compresa tra 1 e 6.";
            hasError = true;
        }

        if (hasError) {
            setFieldErrors(errors);
            setValidationError("Si prega di correggere gli errori nel modulo.");
            return;
        }

        try {
            const [username, subject] = formData.teacherUsername.split("-");

            const requestData = {
                dayWeek: parseInt(formData.dayWeek, 10),
                hour: parseInt(formData.hour, 10),
                username: username.trim(),
                subject: subject.trim(),
                timetableId: parseInt(formData.timetableId, 10),
            };

            await axiosConfig.post(`${Env.API_BASE_URL}/classes/${classId}/createTimeSlot`, requestData);
            setSuccessMessage("La fascia oraria è stata aggiunta correttamente!");
            handleCloseModal();

            // Refresh schedule
            const [scheduleRes] = await Promise.all([
                axiosConfig.get<TeachingTimeslotResponse[]>(
                    `${Env.API_BASE_URL}/classes/${classId}/class-schedule`
                ),
            ]);
            setSchedule(scheduleRes.data);
        } catch (err: any) {
            console.error("Failed to add timeslot:", err);
            setSubmitError(err.response?.data?.message || "Impossibile aggiungere la fascia oraria.");
        }
    };
    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress/>
            </Box>
        );
    }

    if (error) {
        return (
            <Box textAlign="center" marginTop={4}>
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    return (
        <Box sx={{padding: "16px"}}>
            <Typography variant="h4" gutterBottom>
                Orario della classe
            </Typography>

            {/* Alert Globale di Successo */}
            {successMessage && (
                <Alert
                    severity="success"
                    onClose={() => setSuccessMessage(null)}
                    sx={{mb: 2}}
                >
                    {successMessage}
                </Alert>
            )}

            {/* Alert Globale di Errore */}
            {submitError && (
                <Alert
                    severity="error"
                    onClose={() => setSubmitError(null)}
                    sx={{mb: 2}}
                >
                    {submitError}
                </Alert>
            )}

            {/* Alert di Validazione */}
            {validationError && (
                <Alert
                    severity="warning"
                    onClose={() => setValidationError(null)}
                    sx={{mb: 2}}
                >
                    {validationError}
                </Alert>
            )}

            <Button variant="contained" color="primary" onClick={handleOpenModal} sx={{mb: 2}}>
                Aggiungi ora
            </Button>

            {/* Modale per Aggiungere Insegnamento */}
            <Modal open={modalOpen} onClose={handleCloseModal}>
                <Box
                    sx={{
                        position: "absolute",
                        top: "50%",
                        left: "50%",
                        transform: "translate(-50%, -50%)",
                        width: 400,
                        bgcolor: "background.paper",
                        boxShadow: 24,
                        p: 4,
                        borderRadius: 2,
                    }}
                >
                    <Typography variant="h6" gutterBottom>
                        Aggiungi insegnamento
                    </Typography>
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="teaching-select-label">Insegnamento</InputLabel>
                        <Select
                            labelId="teaching-select-label"
                            name="teacherUsername"
                            value={formData.teacherUsername}
                            onChange={handleSelectChange}
                            label="Insegnamento"
                            error={Boolean(fieldErrors.teacherUsername)}
                        >
                            {teachings.map((teaching) => {
                                const formattedName = teaching.username
                                    .split('.')
                                    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
                                    .join(' ');

                                return (
                                    <MenuItem
                                        key={`${teaching.username}-${teaching.subject}`}
                                        value={`${teaching.username}-${teaching.subject}`}
                                    >
                                        {teaching.subject} - {formattedName}
                                    </MenuItem>
                                );
                            })}
                        </Select>
                        {fieldErrors.teacherUsername && (
                            <Typography variant="caption" color="error">
                                {fieldErrors.teacherUsername}
                            </Typography>
                        )}
                    </FormControl>

                    <FormControl fullWidth margin="normal">
                        <InputLabel id="timetable-select-label">Orario della classe</InputLabel>
                        <Select
                            labelId="timetable-select-label"
                            name="timetableId"
                            value={formData.timetableId}
                            onChange={handleSelectChange}
                            label="Orario della classe"
                            error={Boolean(fieldErrors.timetableId)}
                        >
                            {timetables.map((timetable) => (
                                <MenuItem key={timetable.timeTableId} value={timetable.timeTableId.toString()}>
                                    {`${timetable.number}${timetable.letter} - ${timetable.startDate} to ${timetable.endDate || "N/A"}`}
                                </MenuItem>
                            ))}
                        </Select>
                        {fieldErrors.timetableId && (
                            <Typography variant="caption" color="error">
                                {fieldErrors.timetableId}
                            </Typography>
                        )}
                    </FormControl>

                    <TextField
                        fullWidth
                        label="Giorno della settimana (1-6)"
                        name="dayWeek"
                        value={formData.dayWeek}
                        onChange={handleInputChange}
                        margin="normal"
                        type="number"
                        inputProps={{min: 1, max: 6}}
                        error={Boolean(fieldErrors.dayWeek)}
                        helperText={fieldErrors.dayWeek}
                        required
                    />
                    <TextField
                        fullWidth
                        label="Ora (1-6)"
                        name="hour"
                        value={formData.hour}
                        onChange={handleInputChange}
                        margin="normal"
                        type="number"
                        inputProps={{min: 1, max: 6}}
                        error={Boolean(fieldErrors.hour)}
                        helperText={fieldErrors.hour}
                        required
                    />
                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseModal} sx={{mr: 2}}>
                            Esci
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleSubmit}>
                            Inserisci
                        </Button>
                    </Box>
                </Box>
            </Modal>

            <Box display="flex" flexWrap="wrap" gap={3}>
                {Object.keys(groupedSchedule).map((date) => (
                    <Card
                        key={date}
                        sx={{
                            flex: "1 1 calc(33.33% - 24px)",
                            minWidth: "300px",
                            maxWidth: "400px",
                            boxShadow: 2,
                            borderRadius: "12px",
                        }}
                    >
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                {date}
                            </Typography>
                            <Stack spacing={1}>
                                {groupedSchedule[date].map((timeslot, index) => (
                                    <Typography key={index}>
                                        {timeslot.hour}: {timeslot.title}
                                    </Typography>
                                ))}
                            </Stack>
                        </CardContent>
                    </Card>
                ))}
            </Box>
        </Box>)
};

export default ClassSchedule;

import { useParams } from "react-router";
import { useEffect, useState } from "react";
import {
    Typography,
    CircularProgress,
    Alert,
    Box,
    Card,
    CardContent,
    Stack,
    Button,
    Modal,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
    TextField,
} from "@mui/material";
import { SelectChangeEvent } from "@mui/material/Select";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env.ts";

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
    const { classId } = useParams<{ classId: string }>();
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

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (!classId) throw new Error("Class ID is missing.");

                const [scheduleRes, teachingsRes, timetablesRes] = await Promise.all([
                    axiosConfig.get<TeachingTimeslotResponse[]>(`${Env.API_BASE_URL}/classes/${classId}/class-schedule`),
                    axiosConfig.get<TeachingResponse[]>(`${Env.API_BASE_URL}/class-management/teaching`),
                    axiosConfig.get<TimeTableResponse[]>(`${Env.API_BASE_URL}/classes/${classId}/timetable`),
                ]);

                setSchedule(scheduleRes.data);
                setTeachings(teachingsRes.data);
                setTimetables(timetablesRes.data);
                setLoading(false);

            } catch (err) {
                console.error("Failed to fetch data:", err);
                setError("Failed to fetch class schedule or related data.");
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
    const handleCloseModal = () => setModalOpen(false);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSelectChange = (e: SelectChangeEvent<string>) => {
        console.log(`Field: ${e.target.name}, Value: ${e.target.value}` , formData.timetableId); // Debug
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };



    const handleSubmit = async () => {
        console.log("Form Data:", formData); // Debug: controlla i valori

        if (!formData.teacherUsername || !formData.timetableId || !formData.dayWeek || !formData.hour) {
            alert("Please fill in all required fields.");
            return;
        }

        try {
            const requestData = {
                dayWeek: parseInt(formData.dayWeek, 10),
                hour: parseInt(formData.hour, 10),
                username: formData.teacherUsername.split("-")[0], // Controlla se `teacherUsername` contiene il valore corretto
                subject: formData.teacherUsername.split("-")[1],  // Verifica il formato di `teacherUsername`
                timetableId: parseInt(formData.timetableId, 10),  // Assicurati che `timetableId` sia valorizzato
            };

            console.log("Request Data:", requestData); // Debug: controlla i dati inviati al backend

            await axiosConfig.post(`${Env.API_BASE_URL}/classes/${classId}/createTimeSlot`, requestData);
            handleCloseModal();
            alert("Timeslot added successfully!");

        } catch (err) {
            console.error("Failed to add timeslot:", err);
            alert("Failed to add timeslot.");
        }
    };




    if (loading) return <CircularProgress />;
    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <Box sx={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom>
                Class Schedule
            </Typography>
            <Button variant="contained" color="primary" onClick={handleOpenModal} sx={{ mb: 2 }}>
                Add Timeslot
            </Button>
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
                        Add Teaching Timeslot
                    </Typography>
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="teaching-select-label">Teaching</InputLabel>
                        <Select
                            labelId="teaching-select-label"
                            name="teacherUsername"
                            value={formData.teacherUsername}
                            onChange={handleSelectChange}
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
                    </FormControl>


                    <FormControl fullWidth margin="normal">
                        <InputLabel id="timetable-select-label">Timetable</InputLabel>
                        <Select
                            labelId="timetable-select-label"
                            name="timetableId"
                            value={formData.timetableId}
                            onChange={handleSelectChange}
                        >
                            {timetables.map((timetable) => (
                                <MenuItem key={timetable.timeTableId} value={timetable.timeTableId.toString()}>
                                    {timetable.number}{timetable.letter} [{timetable.startDate} - {timetable.endDate || "N/A"}]
                                </MenuItem>

                            ))}
                        </Select>
                    </FormControl>


                    <TextField
                        fullWidth
                        label="Day of Week (1-7)"
                        name="dayWeek"
                        value={formData.dayWeek}
                        onChange={handleInputChange}
                        margin="normal"
                        type="number"
                        inputProps={{ min: 1, max: 7 }}
                    />
                    <TextField
                        fullWidth
                        label="Hour (1-6)"
                        name="hour"
                        value={formData.hour}
                        onChange={handleInputChange}
                        margin="normal"
                        type="number"
                        inputProps={{ min: 1, max: 6 }}
                    />
                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseModal} sx={{ mr: 2 }}>
                            Cancel
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleSubmit}>
                            Submit
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
        </Box>
    );
};

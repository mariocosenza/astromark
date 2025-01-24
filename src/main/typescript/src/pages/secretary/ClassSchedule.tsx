import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { Typography, CircularProgress, Alert, Box } from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env.ts";
interface ClassScheduleResponse {
    day: string;
    timeslots: { subject: string; hour: string }[];
}

const ClassSchedule = () => {
    const { classId } = useParams<{ classId: string }>();
    const [schedule, setSchedule] = useState<ClassScheduleResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchSchedule = async () => {
            try {
                const response = await axiosConfig.get<ClassScheduleResponse[]>(
                    `${Env.API_BASE_URL}/${classId}/class-schedule`
                );
                setSchedule(response.data);
                setLoading(false);
            } catch (err) {
                setError("Failed to fetch class schedule.");
                setLoading(false);
            }
        };

        fetchSchedule();
    }, [classId]);

    if (loading) return <CircularProgress />;
    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <div style={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom>
                Class Schedule for Class ID: {classId}
            </Typography>
            {schedule.map((daySchedule) => (
                <Box key={daySchedule.day} mb={2}>
                    <Typography variant="h6">{daySchedule.day}</Typography>
                    <ul>
                        {daySchedule.timeslots.map((slot, index) => (
                            <li key={index}>
                                {slot.hour} - {slot.subject}
                            </li>
                        ))}
                    </ul>
                </Box>
            ))}
        </div>
    );
};

export default ClassSchedule;

import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { Typography, CircularProgress, Alert, Box } from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env.ts";

interface TeachingTimeslotResponse {
    hour: number; // Adattato da Short
    date: string; // LocalDate verrà gestito come stringa ISO
    title: string;
}

export const ClassSchedule = () => {
    const { classId } = useParams<{ classId: string }>();
    const [schedule, setSchedule] = useState<TeachingTimeslotResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchSchedule = async () => {
            if (!classId) {
                setError("Class ID is missing.");
                setLoading(false);
                return;
            }

            try {
                const response = await axiosConfig.get<TeachingTimeslotResponse[]>(
                    `${Env.API_BASE_URL}/classes/${classId}/class-schedule`
                );
                console.log(response);
                setSchedule(response.data);
                setLoading(false);
            } catch (err) {
                console.error("Failed to fetch class schedule:", err);
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
            {schedule.length === 0 ? (
                <Typography>No schedule available.</Typography>
            ) : (
                schedule.map((timeslot, index) => (
                    <Box key={`${timeslot.date}-${index}`} mb={2}>
                        <Typography variant="h6">Date: {timeslot.date}</Typography>
                        <ul>
                            <li>
                                {timeslot.hour}: {timeslot.title}
                            </li>
                        </ul>
                    </Box>
                ))
            )}
        </div>
    );
};



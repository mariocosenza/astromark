import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { Typography, CircularProgress, Alert, Box, Card, CardContent, Stack } from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env.ts";

interface TeachingTimeslotResponse {
    hour: number;
    date: string;
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

    // Group schedule by date
    const groupedSchedule = schedule.reduce((acc, timeslot) => {
        if (!acc[timeslot.date]) {
            acc[timeslot.date] = [];
        }
        acc[timeslot.date].push(timeslot);
        return acc;
    }, {} as Record<string, TeachingTimeslotResponse[]>);

    const dates = Object.keys(groupedSchedule).slice(0, 6); // Limit to 6 columns

    return (
        <Box sx={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom>
                Class Schedule
            </Typography>
            {dates.length === 0 ? (
                <Typography>No schedule available.</Typography>
            ) : (
                <Box display="flex" flexWrap="wrap" gap={3}>
                    {dates.map((date) => (
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
            )}
        </Box>
    );
};

export default ClassSchedule;

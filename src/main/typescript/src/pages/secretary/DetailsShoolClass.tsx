import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { Box, Typography, List, ListItem, CircularProgress, Grid, Paper } from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env.ts";

interface SchoolClassStudentResponse {
    id: string;
    name: string;
    surname: string;
}

interface TeachingResponse {
    username: string;
    subject: string; // L'array di stringhe racchiuso in un'unica stringa
}

export const DetailsSchoolClass = () => {
    const { classId } = useParams<{ classId: string }>();
    const [students, setStudents] = useState<SchoolClassStudentResponse[]>([]);
    const [teachings, setTeachings] = useState<TeachingResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const [studentsResponse, teachingsResponse] = await Promise.all([
                    axiosConfig.get<SchoolClassStudentResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/students`),
                    axiosConfig.get<TeachingResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/teaching`),
                ]);
                setStudents(studentsResponse.data);
                setTeachings(teachingsResponse.data);
            } catch (err: any) {
                setError(err.response?.data?.message || "Failed to fetch class details");
            } finally {
                setLoading(false);
            }
        };

        if (classId) {
            fetchDetails();
        }
    }, [classId]);

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box textAlign="center" marginTop={4}>
                <Typography variant="h6" color="error">
                    {error}
                </Typography>
            </Box>
        );
    }

    return (
        <Box padding={4}>
            <Typography variant="h4" gutterBottom>
                Class {classId} Details
            </Typography>

            <Grid container spacing={4}>
                {/* Lista degli studenti */}
                <Grid item xs={12} md={6}>
                    <Paper elevation={3} sx={{ padding: 2 }}>
                        <Typography variant="h5" gutterBottom>
                            Students
                        </Typography>
                        {students.length > 0 ? (
                            <List>
                                {students.map((student) => (
                                    <ListItem key={student.id}>
                                        <Typography variant="body1">
                                            {student.name} {student.surname}
                                        </Typography>
                                    </ListItem>
                                ))}
                            </List>
                        ) : (
                            <Typography variant="body2" color="textSecondary">
                                No students found in this class.
                            </Typography>
                        )}
                    </Paper>
                </Grid>

                {/* Lista dei professori */}
                <Grid item xs={12} md={6}>
                    <Paper elevation={3} sx={{ padding: 2 }}>
                        <Typography variant="h5" gutterBottom>
                            Professors
                        </Typography>
                        {teachings.length > 0 ? (
                            <List>
                                {teachings.map((teaching, index) => (
                                    <ListItem key={index}>
                                        <Typography variant="body1">
                                            {formatUsername(teaching.username)} - {formatSubjects(teaching.subject)}
                                        </Typography>
                                    </ListItem>
                                ))}
                            </List>
                        ) : (
                            <Typography variant="body2" color="textSecondary">
                                No professors assigned to this class.
                            </Typography>
                        )}
                    </Paper>
                </Grid>
            </Grid>
        </Box>
    );
};

/**
 * Formatta l'username del professore.
 * Ad esempio, "prof.john" diventa "Prof JOHN".
 *
 * @param username L'username da formattare.
 * @returns L'username formattato.
 */
const formatUsername = (username: string): string => {
    const parts = username.split(".");
    if (parts.length === 2) {
        const [prefix, name] = parts;
        return `${capitalize(prefix)} ${name.toUpperCase()}`;
    }
    return capitalize(username);
};

/**
 * Formatta la stringa dei soggetti separando le virgole con spazi.
 *
 * @param subjects La stringa dei soggetti.
 * @returns La stringa dei soggetti formattata.
 */
const formatSubjects = (subjects: string): string => {
    return subjects.split(",").map(subject => subject.trim()).join(", ");
};

/**
 * Capitalizza la prima lettera di una stringa.
 *
 * @param text La stringa da capitalizzare.
 * @returns La stringa con la prima lettera maiuscola.
 */
const capitalize = (text: string): string => {
    if (text.length === 0) return text;
    return text.charAt(0).toUpperCase() + text.slice(1);
};

import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import {
    Box,
    Button,
    Checkbox,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControlLabel,
    Grid,
    List,
    ListItem,
    Paper,
    TextField,
    Typography
} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env.ts";

interface SchoolClassStudentResponse {
    id: string;
    name: string;
    surname: string;
}

interface TeachingResponse {
    username: string;
    subject: string;
}

interface StudentRequest {
    email: string;
    name: string;
    surname: string;
    taxId?: string;
    birthDate: string; // ISO string format
    male: boolean;
    residentialAddress: string;
    classId: number;
}

export const DetailsSchoolClass = () => {
    const {classId} = useParams<{ classId: string }>();
    const [students, setStudents] = useState<SchoolClassStudentResponse[]>([]);
    const [teachings, setTeachings] = useState<TeachingResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [open, setOpen] = useState<boolean>(false);
    const [newStudent, setNewStudent] = useState<StudentRequest>({
        email: "",
        name: "",
        surname: "",
        taxId: "",
        birthDate: "",
        male: false,
        residentialAddress: "",
        classId: parseInt(classId || "0", 10),
    });
    const [formError, setFormError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState<boolean>(false);

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

    const handleOpen = () => {
        setNewStudent({
            email: "",
            name: "",
            surname: "",
            taxId: "",
            birthDate: "",
            male: false,
            residentialAddress: "",
            classId: parseInt(classId || "0", 10),
        });
        setFormError(null);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value, type, checked} = e.target;
        setNewStudent((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async () => {
        // Validazione di base
        if (
            !newStudent.email ||
            !newStudent.name ||
            !newStudent.surname ||
            !newStudent.birthDate ||
            !newStudent.residentialAddress ||
            !newStudent.classId
        ) {
            setFormError("Please fill in all required fields.");
            return;
        }

        setSubmitting(true);
        setFormError(null);

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/students/create-student`, newStudent);
            setOpen(false);
            // Ricarica la lista degli studenti
            const response = await axiosConfig.get<SchoolClassStudentResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/students`);
            setStudents(response.data);
        } catch (err: any) {
            setFormError(err.response?.data?.message || "Failed to add student.");
        } finally {
            setSubmitting(false);
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
                    <Paper elevation={3} sx={{padding: 2}}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="h5">
                                Students
                            </Typography>
                            <Button variant="contained" color="primary" onClick={handleOpen}>
                                Add Student
                            </Button>
                        </Box>
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
                    <Paper elevation={3} sx={{padding: 2}}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="h5">
                                Professori
                            </Typography>
                            <Button variant="contained" color="primary">
                                Aggiungi professore
                            </Button>
                        </Box>
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

            {/* Modale per aggiungere uno studente */}
            <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
                <DialogTitle>Add New Student</DialogTitle>
                <DialogContent>
                    <Box component="form" noValidate>
                        <TextField
                            margin="dense"
                            label="Email"
                            type="email"
                            fullWidth
                            required
                            name="email"
                            value={newStudent.email}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="dense"
                            label="Name"
                            type="text"
                            fullWidth
                            required
                            name="name"
                            value={newStudent.name}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="dense"
                            label="Surname"
                            type="text"
                            fullWidth
                            required
                            name="surname"
                            value={newStudent.surname}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="dense"
                            label="Tax ID"
                            type="text"
                            fullWidth
                            name="taxId"
                            value={newStudent.taxId}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="dense"
                            label="Birth Date"
                            type="date"
                            fullWidth
                            required
                            name="birthDate"
                            InputLabelProps={{
                                shrink: true,
                            }}
                            value={newStudent.birthDate}
                            onChange={handleChange}
                        />
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={newStudent.male}
                                    onChange={handleChange}
                                    name="male"
                                />
                            }
                            label="Male"
                        />
                        <TextField
                            margin="dense"
                            label="Residential Address"
                            type="text"
                            fullWidth
                            required
                            name="residentialAddress"
                            value={newStudent.residentialAddress}
                            onChange={handleChange}
                        />
                    </Box>
                    {formError && (
                        <Typography variant="body2" color="error" mt={2}>
                            {formError}
                        </Typography>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} disabled={submitting}>
                        Cancel
                    </Button>
                    <Button
                        onClick={handleSubmit}
                        color="primary"
                        variant="contained"
                        disabled={submitting}
                    >
                        {submitting ? "Adding..." : "Add Student"}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>)
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

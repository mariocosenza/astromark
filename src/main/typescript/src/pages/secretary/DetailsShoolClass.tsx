import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import {
    Box,
    Button,
    Checkbox,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle, FormControl,
    FormControlLabel,
    Grid, InputLabel,
    List,
    ListItem,
    Paper, Select, SelectChangeEvent,
    TextField,
    Typography
} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env.ts";
import MenuItem from "@mui/material/MenuItem";


interface SchoolClassStudentResponse {
    id: string;
    name: string;
    surname: string;
}

interface SchoolClassParentResponse {
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


interface ParentRequest {
    email: string;
    name: string;
    surname: string;
    taxId?: string;
    birthDate: string; // ISO string format
    male: boolean;
    residentialAddress: string;
    legalGuardian: boolean;
    studentId: string
}



export const DetailsSchoolClass = () => {
    const navigate = useNavigate()
    const {classId} = useParams<{ classId: string }>();
    const [students, setStudents] = useState<SchoolClassStudentResponse[]>([]);
    const [parents, setParents] = useState<SchoolClassParentResponse[]>([]);
    const [teachings, setTeachings] = useState<TeachingResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [isStudentModalOpen, setIsStudentModalOpen] = useState(false);
    const [isParentModalOpen, setIsParentModalOpen] = useState(false);
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

    const [newParent, setNewParent] = useState<ParentRequest>({
        email: "",
        name: "",
        surname: "",
        taxId: "",
        birthDate: "",
        male: false,
        residentialAddress: "",
        legalGuardian: false,
        studentId: "",
    });


    const [formError, setFormError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState<boolean>(false);


    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const [studentsResponse, parentResponse, teachingsResponse] = await Promise.all([
                    axiosConfig.get<SchoolClassStudentResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/students`),
                    axiosConfig.get<SchoolClassParentResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/parents`),
                    axiosConfig.get<TeachingResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/teaching`),
                ]);
                setParents(parentResponse.data);
                setStudents(studentsResponse.data);
                setTeachings(teachingsResponse.data);

            } catch (err: any) {
                setError(err.response?.data?.message || "Impossibile recuperare i dettagli della classe");
            } finally {
                setLoading(false);
            }
        };

        if (classId) {
            fetchDetails();
        }
    }, [classId]);

    const handleStudentOpen = () => {
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
        setIsStudentModalOpen(true);
    };

    const handleStudentClose = () => {
        setIsStudentModalOpen(false);
    };

    const handleStudentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value, type, checked} = e.target;
        setNewStudent((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleStudentSubmit = async () => {

        if (
            !newStudent.email ||
            !newStudent.name ||
            !newStudent.surname ||
            !newStudent.birthDate ||
            !newStudent.residentialAddress ||
            !newStudent.classId
        ) {
            setFormError("Si prega di compilare tutti i campi obbligatori.");
            return;
        }

        setSubmitting(true);
        setFormError(null);

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/students/create-student`, newStudent);
            setIsStudentModalOpen(false);

            const response = await axiosConfig.get<SchoolClassStudentResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/students`);
            setStudents(response.data);
        } catch (err: any) {
            setFormError("Impossibile aggiungere lo studente.");
        } finally {
            setSubmitting(false);
        }
    };

    const handleParentOpen = () => {
        setNewParent({
            email: "",
            name: "",
            surname: "",
            taxId: "",
            birthDate: "",
            male: false,
            residentialAddress: "",
            legalGuardian: false,
            studentId:""
        });
        setFormError(null);
        setIsParentModalOpen(true);
    };

    const handleParentClose = () => {
        setIsParentModalOpen(false);
    };


    const handleParentChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string>
    ) => {
        const { name, value, type, checked } = e.target as HTMLInputElement;

        if (!name) return;

        setNewParent((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };





    const handleParentSubmit = async () => {

        console.log(newParent)
        if (
            !newParent.email ||
            !newParent.name ||
            !newParent.surname ||
            !newParent.birthDate ||
            !newParent.residentialAddress ||
            !newParent.studentId
        ) {
            setFormError("Si prega di compilare tutti i campi obbligatori.");
            return;
        }

        setSubmitting(true);
        setFormError(null);

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/parents/create-parent`, newParent);
            setIsParentModalOpen(false);

            const response = await axiosConfig.get<SchoolClassParentResponse[]>(`${Env.API_BASE_URL}/class-management/${classId}/parents`);
            setStudents(response.data);
        } catch (err: any) {
            setFormError("Impossibile aggiungere il genitore.");
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
                Dettagli classe
            </Typography>
            <Grid container spacing={4}>
                <Grid item xs={12} md={6}>
                    <Paper elevation={3} sx={{padding: 2}}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="h5">
                                Studenti
                            </Typography>
                            <Button variant="contained" color="primary" onClick={handleStudentOpen}>
                                Aggiungi Studente
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
                                Non ci sono studenti in questa classe
                            </Typography>
                        )}
                    </Paper>
                </Grid>
                <Grid item xs={12} md={6}>
                    <Paper elevation={3} sx={{padding: 2}}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="h5">
                                Professori
                            </Typography>
                            <Button variant="contained" color="primary" onClick={() => navigate("/secretary/teacher")}>
                                Sezione professori per aggiungerli alla classe
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
                                Non ci sono professori assegnati a questa classe
                            </Typography>
                        )}
                    </Paper>
                </Grid>
                <Grid item xs={12} md={6}>
                    <Paper elevation={3} sx={{padding: 2}}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="h5">
                                Genitori
                            </Typography>
                            <Button variant="contained" color="primary" onClick={handleParentOpen}>
                                Aggiungi genitori
                            </Button>
                        </Box>
                        {parents.length > 0 ? (
                            <List>
                                {parents.map((parents, index) => (
                                    <ListItem key={index}>
                                        <Typography variant="body1">
                                            {parents.name} {parents.surname}
                                        </Typography>
                                    </ListItem>
                                ))}
                            </List>
                        ) : (
                            <Typography variant="body2" color="textSecondary">
                                Non ci sono genitori assegnati a questa classe
                            </Typography>
                        )}
                    </Paper>
                </Grid>
            </Grid>
            <Dialog open={isStudentModalOpen} onClose={handleStudentClose} fullWidth maxWidth="sm">
                <DialogTitle>Aggiungi studente</DialogTitle>
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
                            onChange={handleStudentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Nome"
                            type="text"
                            fullWidth
                            required
                            name="name"
                            value={newStudent.name}
                            onChange={handleStudentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Cognome"
                            type="text"
                            fullWidth
                            required
                            name="surname"
                            value={newStudent.surname}
                            onChange={handleStudentChange}
                        />
                        <TextField
                            margin="dense"
                            label="CF"
                            type="text"
                            fullWidth
                            name="taxId"
                            value={newStudent.taxId}
                            onChange={handleStudentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Data di nascita"
                            type="date"
                            fullWidth
                            required
                            name="birthDate"
                            InputLabelProps={{
                                shrink: true,
                            }}
                            value={newStudent.birthDate}
                            onChange={handleStudentChange}
                        />
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={newStudent.male}
                                    onChange={handleStudentChange}
                                    name="male"
                                />
                            }
                            label="Uomo"
                        />
                        <TextField
                            margin="dense"
                            label="Indirizzo di residenza"
                            type="text"
                            fullWidth
                            required
                            name="residentialAddress"
                            value={newStudent.residentialAddress}
                            onChange={handleStudentChange}
                        />
                    </Box>
                    {formError && (
                        <Typography variant="body2" color="error" mt={2}>
                            {formError}
                        </Typography>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleStudentClose} disabled={submitting}>
                        Cancel
                    </Button>
                    <Button
                        onClick={handleStudentSubmit}
                        color="primary"
                        variant="contained"
                        disabled={submitting}
                    >
                        {submitting ? "Adding..." : "Add Student"}
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog open={isParentModalOpen} onClose={handleParentClose} fullWidth maxWidth="sm">
                <DialogTitle>Aggiungi Genitore</DialogTitle>
                <DialogContent>
                    <Box component="form" noValidate>
                        <TextField
                            margin="dense"
                            label="Email"
                            type="email"
                            fullWidth
                            required
                            name="email"
                            value={newParent.email}
                            onChange={handleParentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Nome"
                            type="text"
                            fullWidth
                            required
                            name="name"
                            value={newParent.name}
                            onChange={handleParentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Cognome"
                            type="text"
                            fullWidth
                            required
                            name="surname"
                            value={newParent.surname}
                            onChange={handleParentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Codice Fiscale"
                            type="text"
                            fullWidth
                            name="taxId"
                            value={newParent.taxId}
                            onChange={handleParentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Data di Nascita"
                            type="date"
                            fullWidth
                            required
                            name="birthDate"
                            InputLabelProps={{
                                shrink: true,
                            }}
                            value={newParent.birthDate}
                            onChange={handleParentChange}
                        />
                        <TextField
                            margin="dense"
                            label="Indirizzo Residenziale"
                            type="text"
                            fullWidth
                            required
                            name="residentialAddress"
                            value={newParent.residentialAddress}
                            onChange={handleParentChange}
                        />
                        <FormControl margin="dense" fullWidth required>
                            <InputLabel id="student-select-label">Studente</InputLabel>
                            <Select
                                labelId="student-select-label"
                                value={newParent.studentId}
                                name="studentId"
                                onChange={handleParentChange}
                            >
                                {students.map((student) => (
                                    <MenuItem key={student.id} value={student.id}>
                                        {student.name} {student.surname}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={newParent.male}
                                    onChange={handleParentChange}
                                    name="male"
                                />
                            }
                            label="Maschio"
                        />
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={newParent.legalGuardian}
                                    onChange={handleParentChange}
                                    name="legalGuardian"
                                />
                            }
                            label="Tutore Legale"
                        />
                    </Box>
                    {formError && (
                        <Typography variant="body2" color="error" mt={2}>
                            {formError}
                        </Typography>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleParentClose} disabled={submitting}>
                        Annulla
                    </Button>
                    <Button
                        onClick={handleParentSubmit}
                        color="primary"
                        variant="contained"
                        disabled={submitting}
                    >
                        {submitting ? "Aggiunta in corso..." : "Aggiungi Genitore"}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>)
};


const formatUsername = (username: string): string => {

    const cleanedUsername = username.replace(/\d+/g, "");
    const parts = cleanedUsername.split(".");

    if (parts.length === 2) {
        const [firstName, lastName] = parts;
        return `${capitalize(firstName)} ${capitalize(lastName)}`;
    }
    return capitalize(cleanedUsername);
};



const formatSubjects = (subjects: string): string => {
    return subjects.split(",").map(subject => subject.trim()).join(", ");
};


const capitalize = (text: string): string => {
    if (text.length === 0) return text;
    return text.charAt(0).toUpperCase() + text.slice(1);
};

import React, { useEffect, useState } from "react";
import {
    Alert,
    Box,
    Button,
    Card,
    Checkbox,
    CircularProgress,
    FormControl,
    FormControlLabel,
    InputLabel,
    MenuItem,
    Modal,
    Select,
    SelectChangeEvent,
    TextField,
    Typography,
} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env";
import { useParams } from "react-router";

interface SchoolClassResponse {
    id: number;
    year: number;
    letter: string;
    number: number;
    description: string;
}

interface TeachingResponse {
    username: string;
    teaching: string[];
}

interface SubjectResponse {
    subject: string;
}

export const TeacherDetails = () => {
    const { teacheruuid } = useParams<{ teacheruuid: string }>();

    const [classes, setClasses] = useState<SchoolClassResponse[]>([]);
    const [teachings, setTeachings] = useState<TeachingResponse[]>([]);
    const [allClasses, setAllClasses] = useState<SchoolClassResponse[]>([]);
    const [allSubjects, setAllSubjects] = useState<string[]>([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [classModalOpen, setClassModalOpen] = useState(false);
    const [teachingModalOpen, setTeachingModalOpen] = useState(false);

    const [selectedClassId, setSelectedClassId] = useState<string>("");
    const [isCoordinator, setIsCoordinator] = useState<boolean>(false);

    const [teachingFormData, setTeachingFormData] = useState({
        subjectTitle: "",
        activityType: "",
    });

    // Stati per i messaggi globali
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [
                    classesResponse,
                    allClassesResponse,
                    teachingResponse,
                    subjectsResponse,
                ] = await Promise.all([
                    axiosConfig.get<SchoolClassResponse[]>(
                        `${Env.API_BASE_URL}/class-management/${teacheruuid}/class`
                    ),
                    axiosConfig.get<SchoolClassResponse[]>(
                        `${Env.API_BASE_URL}/class-management/all`
                    ),
                    axiosConfig.get<TeachingResponse[]>(
                        `${Env.API_BASE_URL}/teachers/${teacheruuid}/teachings`
                    ),
                    axiosConfig.get<SubjectResponse[]>(
                        `${Env.API_BASE_URL}/class-management/teaching`
                    ),
                ]);

                setClasses(classesResponse.data || []);
                setAllClasses(allClassesResponse.data || []);
                setTeachings(teachingResponse.data || []);
                const onlySubjects = (subjectsResponse.data || []).map((item) => item.subject);
                setAllSubjects(onlySubjects);

                setLoading(false);
            } catch (err: any) {
                console.error("Errore nel recuperare i dati:", err);
                setError("Impossibile recuperare i dati.");
                setLoading(false);
            }
        };

        if (teacheruuid) {
            fetchData();
        }
    }, [teacheruuid]);

    const handleOpenClassModal = () => {
        setSelectedClassId("");
        setIsCoordinator(false);
        setErrorMessage(null);
        setSuccessMessage(null);
        setClassModalOpen(true);
    };
    const handleCloseClassModal = () => {
        setSelectedClassId("");
        setIsCoordinator(false);
        setClassModalOpen(false);
    };

    const handleOpenTeachingModal = () => {
        setTeachingFormData({ subjectTitle: "", activityType: "" });
        setErrorMessage(null);
        setSuccessMessage(null);
        setTeachingModalOpen(true);
    };
    const handleCloseTeachingModal = () => {
        setTeachingFormData({ subjectTitle: "", activityType: "" });
        setTeachingModalOpen(false);
    };

    const handleClassChange = (event: SelectChangeEvent) => {
        setSelectedClassId(event.target.value);
    };

    const handleCoordinatorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setIsCoordinator(event.target.checked);
    };

    const handleSubjectChange = (event: SelectChangeEvent) => {
        setTeachingFormData((prev) => ({
            ...prev,
            subjectTitle: event.target.value,
        }));
    };

    const handleActivityTypeChange = (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => {
        setTeachingFormData((prev) => ({
            ...prev,
            activityType: event.target.value,
        }));
    };

    // Funzione per aggiungere una classe al professore
    const handleAddClass = async () => {
        if (!selectedClassId) {
            setErrorMessage("Seleziona una classe.");
            setSuccessMessage(null);
            return;
        }

        try {
            await axiosConfig.post(
                `${Env.API_BASE_URL}/class-management/${teacheruuid}/add-teacher-to-class`,
                {
                    classId: selectedClassId,
                    isCoordinator,
                }
            );
            setSuccessMessage("Professore aggiunto alla classe correttamente.");
            setErrorMessage(null);
            handleCloseClassModal();

            // Refresh the classes list
            const response = await axiosConfig.get<SchoolClassResponse[]>(
                `${Env.API_BASE_URL}/class-management/${teacheruuid}/class`
            );
            setClasses(response.data);
        } catch (err: any) {
            console.error("Errore nell'aggiungere il professore alla classe:", err);
            setErrorMessage(err.response?.data?.message || "Errore nell'aggiunta del professore alla classe.");
            setSuccessMessage(null);
        }
    };

    // Funzione per aggiungere un insegnamento
    const handleAddTeaching = async () => {
        const { subjectTitle, activityType } = teachingFormData;
        if (!subjectTitle || !activityType) {
            setErrorMessage("Riempi tutti i campi.");
            setSuccessMessage(null);
            return;
        }

        try {
            await axiosConfig.post(
                `${Env.API_BASE_URL}/class-management/${teacheruuid}/add-teaching`,
                {
                    subjectTitle,
                    activityType,
                }
            );
            setSuccessMessage("Insegnamento aggiunto con successo!");
            setErrorMessage(null);
            handleCloseTeachingModal();

            // Refresh the teachings list
            const [teachingResponse] = await Promise.all([
                axiosConfig.get<TeachingResponse[]>(
                    `${Env.API_BASE_URL}/teachers/${teacheruuid}/teachings`
                ),
            ]);
            setTeachings(teachingResponse.data);
        } catch (err: any) {
            console.error("Errore nell'aggiungere l'insegnamento:", err);
            setErrorMessage(err.response?.data?.message || "Errore nell'aggiunta dell'insegnamento.");
            setSuccessMessage(null);
        }
    };

    // Funzione per rimuovere una classe dal professore
    const handleDeleteFromClass = async (schoolClassId: number) => {
        try {
            await axiosConfig.delete(`${Env.API_BASE_URL}/class-management/${teacheruuid}/${schoolClassId}/delete-from-class`);
            setSuccessMessage("Classe rimossa con successo.");
            setErrorMessage(null);

            // Refresh the classes list
            const response = await axiosConfig.get<SchoolClassResponse[]>(
                `${Env.API_BASE_URL}/class-management/${teacheruuid}/class`
            );
            setClasses(response.data);
        } catch (err: any) {
            console.error("Errore nel rimuovere la classe:", err);
            setErrorMessage(err.response?.data?.message || "Errore nel rimuovere la classe.");
            setSuccessMessage(null);
        }
    };

    if (loading) {
        return (
            <CircularProgress sx={{ display: "block", margin: "auto", mt: 4 }} />
        );
    }
    if (error) {
        return (
            <Alert severity="error" sx={{ maxWidth: "600px", margin: "auto", mt: 4 }}>
                {error}
            </Alert>
        );
    }

    return (
        <Box sx={{ padding: "16px" }}>
            <Typography
                variant="h4"
                gutterBottom
                sx={{ textAlign: "center", fontWeight: "bold" }}
            >
                Dettagli professore
            </Typography>

            {/* Alert Globale di Successo */}
            {successMessage && (
                <Alert
                    severity="success"
                    onClose={() => setSuccessMessage(null)}
                    sx={{ mb: 2 }}
                >
                    {successMessage}
                </Alert>
            )}

            {/* Alert Globale di Errore */}
            {errorMessage && (
                <Alert
                    severity="error"
                    onClose={() => setErrorMessage(null)}
                    sx={{ mb: 2 }}
                >
                    {errorMessage}
                </Alert>
            )}

            <Typography variant="h5" gutterBottom sx={{ textAlign: "center", mt: 4 }}>
                Classi
            </Typography>

            <Box>
                {classes.map((schoolClass) => (
                    <Card
                        key={schoolClass.id}
                        sx={{
                            backgroundColor: "#f3f4f6",
                            padding: "16px",
                            marginY: "8px",
                            borderRadius: "12px",
                            boxShadow: 2,
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "space-between",
                        }}
                    >
                        <Box>
                            <Typography
                                variant="h6"
                                sx={{
                                    color: "#1e3a8a",
                                    fontWeight: "bold",
                                }}
                            >
                                {`${schoolClass.number}${schoolClass.letter}`}
                            </Typography>
                            <Typography
                                variant="body2"
                                sx={{
                                    color: "#64748b",
                                    marginTop: "4px",
                                }}
                            >
                                Anno: {schoolClass.year}
                            </Typography>
                            <Typography
                                variant="body2"
                                sx={{
                                    color: "#64748b",
                                    marginTop: "4px",
                                }}
                            >
                                {schoolClass.description}
                            </Typography>
                        </Box>
                        <Button
                            variant="outlined"
                            color="error"
                            sx={{
                                marginTop: 2,
                                textTransform: "none",
                                paddingX: 3,
                                paddingY: 1,
                                borderRadius: 2,
                                boxShadow: 2,
                                ":hover": {
                                    backgroundColor: "error.main",
                                    color: "white",
                                },
                            }}
                            onClick={() => handleDeleteFromClass(schoolClass.id)}
                        >
                            Rimuovi Classe
                        </Button>
                    </Card>
                ))}
            </Box>

            <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{ marginTop: "16px", textTransform: "none" }}
                onClick={handleOpenClassModal}
            >
                Aggiungi a classe
            </Button>

            <Typography variant="h5" gutterBottom sx={{ textAlign: "center", mt: 4 }}>
                Insegnamenti
            </Typography>
            <Box>
                {teachings.length > 0 ? (
                    teachings.map((teaching, index) => (
                        <Card
                            key={index}
                            sx={{
                                backgroundColor: "#f9f9f9",
                                padding: "16px",
                                marginY: "8px",
                                borderRadius: "12px",
                                boxShadow: 2,
                            }}
                        >
                            <Typography variant="h6" sx={{ color: "#374151", fontWeight: "bold" }}>
                                {teaching.username}
                            </Typography>
                            <Typography variant="body2" sx={{ color: "#64748b", marginTop: "4px" }}>
                                {teaching.teaching.join(", ")}
                            </Typography>
                        </Card>
                    ))
                ) : (
                    <Typography variant="body2" sx={{ textAlign: "center", mt: 2 }}>
                        Nessun insegnamento
                    </Typography>
                )}
            </Box>

            <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{ marginTop: "16px", textTransform: "none" }}
                onClick={handleOpenTeachingModal}
            >
                Aggiungi insegnamento
            </Button>

            {/* Modale Aggiungi a Classe */}
            <Modal open={classModalOpen} onClose={handleCloseClassModal}>
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
                        Aggiungi a classe
                    </Typography>
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="class-select-label">Seleziona classe</InputLabel>
                        <Select
                            labelId="class-select-label"
                            value={selectedClassId}
                            onChange={handleClassChange}
                            label="Seleziona classe"
                        >
                            <MenuItem value="" disabled>
                                Seleziona una classe
                            </MenuItem>
                            {allClasses.map((schoolClass) => (
                                <MenuItem
                                    key={schoolClass.id}
                                    value={schoolClass.id.toString()}
                                >
                                    {`${schoolClass.number}${schoolClass.letter} - ${schoolClass.description}`}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={isCoordinator}
                                onChange={handleCoordinatorChange}
                                name="isCoordinator"
                            />
                        }
                        label="Coordinatore"
                    />

                    {/* Alert di Errore nel Modale Classe */}
                    {errorMessage && (
                        <Alert
                            severity="error"
                            onClose={() => setErrorMessage(null)}
                            sx={{ mt: 2 }}
                        >
                            {errorMessage}
                        </Alert>
                    )}

                    {/* Alert di Successo nel Modale Classe */}
                    {successMessage && (
                        <Alert
                            severity="success"
                            onClose={() => setSuccessMessage(null)}
                            sx={{ mt: 2 }}
                        >
                            {successMessage}
                        </Alert>
                    )}

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseClassModal} sx={{ mr: 2 }}>
                            Chiudi
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleAddClass}>
                            Invia
                        </Button>
                    </Box>
                </Box>
            </Modal>

            {/* Modale Aggiungi Insegnamento */}
            <Modal open={teachingModalOpen} onClose={handleCloseTeachingModal}>
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
                        <InputLabel id="subject-select-label">Materie</InputLabel>
                        <Select
                            labelId="subject-select-label"
                            value={teachingFormData.subjectTitle}
                            onChange={handleSubjectChange}
                            label="Materie"
                        >
                            <MenuItem value="" disabled>
                                Materie disponibili
                            </MenuItem>
                            {allSubjects.map((subjectStr, index) => (
                                <MenuItem key={index} value={subjectStr}>
                                    {subjectStr}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <TextField
                        fullWidth
                        label="Attività"
                        name="activityType"
                        value={teachingFormData.activityType}
                        onChange={handleActivityTypeChange}
                        margin="normal"
                        required
                    />

                    {/* Alert di Errore nel Modale Insegnamento */}
                    {errorMessage && (
                        <Alert
                            severity="error"
                            onClose={() => setErrorMessage(null)}
                            sx={{ mt: 2 }}
                        >
                            {errorMessage}
                        </Alert>
                    )}

                    {/* Alert di Successo nel Modale Insegnamento */}
                    {successMessage && (
                        <Alert
                            severity="success"
                            onClose={() => setSuccessMessage(null)}
                            sx={{ mt: 2 }}
                        >
                            {successMessage}
                        </Alert>
                    )}

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseTeachingModal} sx={{ mr: 2 }}>
                            Chiudi
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleAddTeaching}>
                            Invia
                        </Button>
                    </Box>
                </Box>
            </Modal>
        </Box>
    );
};

export default TeacherDetails;

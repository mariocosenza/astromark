import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    CircularProgress,
    Alert,
    Card,
    Button,
    Modal,
    Select,
    MenuItem,
    InputLabel,
    FormControl,
    FormControlLabel,
    Checkbox,
    TextField,
} from "@mui/material";
import { SelectChangeEvent } from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env";
import { useParams } from "react-router";

/** Rappresenta una classe scolastica. */
interface SchoolClassResponse {
    id: number;
    year: number;
    letter: string;
    number: number;
    description: string;
}

/** Rappresenta l’oggetto restituito dalle API “teaching”. */
interface TeachingResponse {
    username: string;
    teaching: string[];
}

/** Rappresenta un soggetto. */
interface SubjectResponse {
    subject: string;
}

export const TeacherDetails = () => {
    const { teacheruuid } = useParams<{ teacheruuid: string }>();

    // Stato per le classi associate al docente
    const [classes, setClasses] = useState<SchoolClassResponse[]>([]);
    // Stato per gli insegnamenti associati al docente
    const [teachings, setTeachings] = useState<TeachingResponse[]>([]);
    // Stato per tutte le classi disponibili (per la select)
    const [allClasses, setAllClasses] = useState<SchoolClassResponse[]>([]);
    // Stato per tutti i soggetti disponibili (per la select)
    const [allSubjects, setAllSubjects] = useState<string[]>([]);

    // Stato per loading e error
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Stato per le modali
    const [classModalOpen, setClassModalOpen] = useState(false);
    const [teachingModalOpen, setTeachingModalOpen] = useState(false);

    // Stato per aggiungere classi al docente
    const [selectedClassId, setSelectedClassId] = useState<string>("");
    const [isCoordinator, setIsCoordinator] = useState<boolean>(false);

    // Stato per il form di aggiunta insegnamenti
    const [teachingFormData, setTeachingFormData] = useState({
        subjectTitle: "", // la materia selezionata
        activityType: "", // il tipo di attività
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Facciamo quattro chiamate in parallelo
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
                    // NOTA: qui cambiamo il tipo T di get<T> da array a singolo oggetto
                    axiosConfig.get<TeachingResponse>(
                        `${Env.API_BASE_URL}/teachers/${teacheruuid}/teachings`
                    ),
                    axiosConfig.get<SubjectResponse[]>(
                        `${Env.API_BASE_URL}/class-management/teaching`
                    ),
                ]);

                // 1) Classi che il docente già ha
                setClasses(classesResponse.data || []);
                // 2) Tutte le classi per la select
                setAllClasses(allClassesResponse.data || []);

                // 3) Teachings del docente (API restituisce UN singolo oggetto)
                //    -> Lo salviamo come array di lunghezza 1, così il resto del codice
                //       può fare teachings.map(...) senza errori.
                const singleTeachingObj = teachingResponse.data;
                setTeachings([singleTeachingObj]);

                console.log("Teaching Response Data:", singleTeachingObj);

                // 4) L’endpoint /class-management/teaching restituisce un array di SubjectResponse,
                //    cioè { subject: string }. Facciamo un "map" per ottenere un array di stringhe:
                const onlySubjects = (subjectsResponse.data || []).map((item) => item.subject);
                setAllSubjects(onlySubjects);

                setLoading(false);
            } catch (err) {
                console.error("Failed to fetch data:", err);
                setError("Failed to fetch data.");
                setLoading(false);
            }
        };

        fetchData();
    }, [teacheruuid]);


    // Apertura/chiusura modale "Add Class"
    const handleOpenClassModal = () => setClassModalOpen(true);
    const handleCloseClassModal = () => {
        setSelectedClassId("");
        setIsCoordinator(false);
        setClassModalOpen(false);
    };

    // Apertura/chiusura modale "Add Teaching"
    const handleOpenTeachingModal = () => setTeachingModalOpen(true);
    const handleCloseTeachingModal = () => {
        setTeachingFormData({ subjectTitle: "", activityType: "" });
        setTeachingModalOpen(false);
    };

    // Handler per il Select delle classi
    const handleClassChange = (event: SelectChangeEvent<string>) => {
        setSelectedClassId(event.target.value);
    };

    // Handler per il checkbox “Is Coordinator”
    const handleCoordinatorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setIsCoordinator(event.target.checked);
    };

    // Handler per il Select delle materie -> "subjectTitle"
    // Poiché allSubjects è string[], usiamo "event.target.value" come string
    const handleSubjectChange = (event: SelectChangeEvent<string>) => {
        setTeachingFormData((prev) => ({
            ...prev,
            subjectTitle: event.target.value,
        }));
    };

    // Handler per il TextField -> "activityType"
    const handleActivityTypeChange = (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => {
        setTeachingFormData((prev) => ({
            ...prev,
            activityType: event.target.value,
        }));
    };

    // Aggiunta di una nuova classe per il docente
    const handleAddClass = async () => {
        if (!selectedClassId) {
            alert("Please select a class.");
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
            alert("Teacher added to class successfully!");
            handleCloseClassModal();
            // (se vuoi, richiama fetchData o aggiorna manualmente 'classes')
        } catch (err) {
            console.error("Failed to add teacher to class:", err);
            alert("Failed to add teacher to class.");
        }
    };

    // Aggiunta di un nuovo teaching per il docente
    const handleAddTeaching = async () => {
        const { subjectTitle, activityType } = teachingFormData;
        if (!subjectTitle || !activityType) {
            alert("Please fill in all fields.");
            return;
        }

        try {
            await axiosConfig.post(
                `${Env.API_BASE_URL}/class-management/${teacheruuid}/add-teaching`,
                // l’oggetto che inviamo, es. { subjectTitle, activityType }
                {
                    subjectTitle,
                    activityType,
                }
            );
            alert("Teaching added successfully!");
            handleCloseTeachingModal();
            // (se vuoi, ricarica i teachings o aggiorna manualmente 'teachings')
            // Esempio: fetchData();
        } catch (err) {
            console.error("Failed to add teaching:", err);
            alert("Failed to add teaching.");
        }
    };

    // Loading & error
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

    // Render principale
    return (
        <Box sx={{ padding: "16px" }}>
            <Typography
                variant="h4"
                gutterBottom
                sx={{ textAlign: "center", fontWeight: "bold" }}
            >
                Teacher Details
            </Typography>

            {/* Sezione: classi del docente */}
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
                            </Typography>
                        </Box>
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
                Add to Class
            </Button>

            {/* Sezione: Teachings */}
            <Typography variant="h5" gutterBottom sx={{ textAlign: "center", mt: 4 }}>
                Teachings
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
                        No teachings available.
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
                Add Teaching
            </Button>

            {/* Modal per aggiungere una classe */}
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
                        Add Teacher to Class
                    </Typography>
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="class-select-label">Select Class</InputLabel>
                        <Select
                            labelId="class-select-label"
                            value={selectedClassId}
                            onChange={handleClassChange}
                        >
                            <MenuItem value="" disabled>
                                Select a class
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
                            />
                        }
                        label="Is Coordinator"
                    />

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseClassModal} sx={{ mr: 2 }}>
                            Cancel
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleAddClass}>
                            Submit
                        </Button>
                    </Box>
                </Box>
            </Modal>

            {/* Modal per aggiungere un Teaching */}
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
                        Add Teaching
                    </Typography>

                    <FormControl fullWidth margin="normal">
                        <InputLabel id="subject-select-label">Subject Title</InputLabel>
                        <Select
                            labelId="subject-select-label"
                            value={teachingFormData.subjectTitle}
                            onChange={handleSubjectChange}
                        >
                            <MenuItem value="" disabled>
                                Select a subject
                            </MenuItem>

                            {/* Qui usiamo `allSubjects` come array di sole stringhe. */}
                            {allSubjects.map((subjectStr, index) => (
                                <MenuItem key={index} value={subjectStr}>
                                    {subjectStr}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <TextField
                        fullWidth
                        label="Activity Type"
                        name="activityType"
                        value={teachingFormData.activityType}
                        onChange={handleActivityTypeChange}
                        margin="normal"
                    />

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseTeachingModal} sx={{ mr: 2 }}>
                            Cancel
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleAddTeaching}>
                            Submit
                        </Button>
                    </Box>
                </Box>
            </Modal>
        </Box>
    );
};

export default TeacherDetails;

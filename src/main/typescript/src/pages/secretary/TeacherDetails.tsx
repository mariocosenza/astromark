import {useEffect, useState} from "react";
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
import {SelectChangeEvent} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env";
import {useParams} from "react-router";

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
    const {teacheruuid} = useParams<{ teacheruuid: string }>();


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

                    axiosConfig.get<TeachingResponse>(
                        `${Env.API_BASE_URL}/teachers/${teacheruuid}/teachings`
                    ),
                    axiosConfig.get<SubjectResponse[]>(
                        `${Env.API_BASE_URL}/class-management/teaching`
                    ),
                ]);


                setClasses(classesResponse.data || []);

                setAllClasses(allClassesResponse.data || []);


                const singleTeachingObj = teachingResponse.data;
                setTeachings([singleTeachingObj]);


                const onlySubjects = (subjectsResponse.data || []).map((item) => item.subject);
                setAllSubjects(onlySubjects);

                setLoading(false);
            } catch (err) {
                setError("Failed to fetch data.");
                setLoading(false);
            }
        };

        fetchData();
    }, [teacheruuid]);


    const handleOpenClassModal = () => setClassModalOpen(true);
    const handleCloseClassModal = () => {
        setSelectedClassId("");
        setIsCoordinator(false);
        setClassModalOpen(false);
    };
    function handleDeleteFromClass( schoolClassId: number) {
        console.log("School Class ID:", schoolClassId);
        console.log("Teacher UUID:", teacheruuid);

        axiosConfig
            .delete(`${Env.API_BASE_URL}/class-management/${teacheruuid}/${schoolClassId}/delete-from-class`)
            .then((response) => {
                console.log("Class successfully removed:", response.data);
            })
            .catch((error) => {
                if (error.response) {
                    console.error(
                        "Failed to remove class:",
                        error.response.status,
                        error.response.data
                    );
                } else {
                    console.error("Error during delete request:", error.message);
                }
            });
    }


    const handleOpenTeachingModal = () => setTeachingModalOpen(true);
    const handleCloseTeachingModal = () => {
        setTeachingFormData({subjectTitle: "", activityType: ""});
        setTeachingModalOpen(false);
    };


    const handleClassChange = (event: SelectChangeEvent<string>) => {
        setSelectedClassId(event.target.value);
    };


    const handleCoordinatorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setIsCoordinator(event.target.checked);
    };


    const handleSubjectChange = (event: SelectChangeEvent<string>) => {
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


    const handleAddClass = async () => {
        if (!selectedClassId) {
            alert("Seleziona una classe");
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
            alert("Professore aggiunto alla classe correttamente");
            handleCloseClassModal();
            // (se vuoi, richiama fetchData o aggiorna manualmente 'classes')
        } catch (err) {
            alert("Errore nell'aggiunta.");
        }
    };


    const handleAddTeaching = async () => {
        const {subjectTitle, activityType} = teachingFormData;
        if (!subjectTitle || !activityType) {
            alert("Riempi tutti i campi.");
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
            alert("Insegnamento aggiunto con successo!");
            handleCloseTeachingModal();

        } catch (err) {
            alert("Errore nell'aggiunta.");
        }
    };


    if (loading) {
        return (
            <CircularProgress sx={{display: "block", margin: "auto", mt: 4}}/>
        );
    }
    if (error) {
        return (
            <Alert severity="error" sx={{maxWidth: "600px", margin: "auto", mt: 4}}>
                {error}
            </Alert>
        );
    }


    return (
        <Box sx={{padding: "16px"}}>
            <Typography
                variant="h4"
                gutterBottom
                sx={{textAlign: "center", fontWeight: "bold"}}
            >
                Dettagli professore
            </Typography>

            <Typography variant="h5" gutterBottom sx={{textAlign: "center", mt: 4}}>
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
                sx={{marginTop: "16px", textTransform: "none"}}
                onClick={handleOpenClassModal}
            >
                Aggiungi a classe
            </Button>


            <Typography variant="h5" gutterBottom sx={{textAlign: "center", mt: 4}}>
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
                            <Typography variant="h6" sx={{color: "#374151", fontWeight: "bold"}}>
                                {teaching.username}
                            </Typography>
                            <Typography variant="body2" sx={{color: "#64748b", marginTop: "4px"}}>
                                {teaching.teaching.join(", ")}
                            </Typography>
                        </Card>
                    ))
                ) : (
                    <Typography variant="body2" sx={{textAlign: "center", mt: 2}}>
                        Nessun insegnamento
                    </Typography>
                )}
            </Box>

            <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{marginTop: "16px", textTransform: "none"}}
                onClick={handleOpenTeachingModal}
            >
                Aggiungi insegamento
            </Button>


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
                            />
                        }
                        label="Coordinatore"
                    />

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseClassModal} sx={{mr: 2}}>
                            Chiudi
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleAddClass}>
                            Invia
                        </Button>
                    </Box>
                </Box>
            </Modal>


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
                        <InputLabel id="subject-select-label"> Materie</InputLabel>
                        <Select
                            labelId="subject-select-label"
                            value={teachingFormData.subjectTitle}
                            onChange={handleSubjectChange}
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
                    />

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseTeachingModal} sx={{mr: 2}}>
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

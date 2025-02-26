import React, {useEffect, useState} from "react";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Modal,
    Select,
    SelectChangeEvent,
    TextField,
    Typography
} from "@mui/material";
import {useNavigate} from "react-router";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env.ts";

interface SchoolClassResponse {
    id: number;
    year: number;
    letter: string;
    number: number;
}

export const ManageTimetable = () => {
    const [schoolClasses, setSchoolClasses] = useState<SchoolClassResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [modalOpen, setModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        schoolClassId: "",
        startDate: "",
        endDate: "",
        expectedHours: "27" // Valore predefinito
    });
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [submitError, setSubmitError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSchoolClasses = async () => {
            try {
                const response = await axiosConfig.get<SchoolClassResponse[]>(`${Env.API_BASE_URL}/class-management/all`);
                setSchoolClasses(response.data);
                setLoading(false);
            } catch (err) {
                setError("Impossibile recuperare le lezioni scolastiche.");
                setLoading(false);
            }
        };

        fetchSchoolClasses();
    }, []);

    const handleCardClick = (classId: number) => {
        navigate(`/secretary/class-schedule/${classId}`);
    };

    const handleOpenModal = () => {
        setModalOpen(true);
    };

    const handleCloseModal = () => {
        setModalOpen(false);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({...formData, [e.target.name]: e.target.value});
    };

    const handleClassChange = (e: SelectChangeEvent) => {
        setFormData({...formData, schoolClassId: e.target.value});
    };

    const handleSubmit = async () => {

        if (new Date(formData.endDate).getTime() < new Date(formData.startDate).getTime()) {
            setSubmitError("Inserisci data validata");
            return
        }
        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/classes/createTimeTable`, formData);
            setSuccessMessage("Orario delle lezioni creato con successo!");
            setSubmitError(null);
            handleCloseModal();
        } catch (err) {
            console.error("Failed to create class timetable:", err);
            setSubmitError("Impossibile creare l'orario delle lezioni.");
            setSuccessMessage(null);
        }
    };

    if (loading) return <CircularProgress/>;
    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <div style={{padding: "16px"}}>
            <Typography variant="h4" gutterBottom>
                Classi
            </Typography>

            {/* Alert di Successo */}
            {successMessage && (
                <Alert
                    severity="success"
                    onClose={() => setSuccessMessage(null)}
                    sx={{mb: 2}}
                >
                    {successMessage}
                </Alert>
            )}

            {/* Alert di Errore durante la Submit */}
            {submitError && (
                <Alert
                    severity="error"
                    onClose={() => setSubmitError(null)}
                    sx={{mb: 2}}
                >
                    {submitError}
                </Alert>
            )}

            <Button
                variant="contained"
                color="primary"
                onClick={handleOpenModal}
                style={{marginBottom: "16px"}}
            >
                Crea orario
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
                        borderRadius: 2
                    }}
                >
                    <Typography variant="h6" gutterBottom>
                        Crea orario della classe
                    </Typography>
                    <FormControl fullWidth margin="normal">
                        <InputLabel id="school-class-select-label">Seleziona classe</InputLabel>
                        <Select
                            labelId="school-class-select-label"
                            value={formData.schoolClassId}
                            onChange={handleClassChange}
                        >
                            {schoolClasses.map((schoolClass) => (
                                <MenuItem key={schoolClass.id} value={schoolClass.id.toString()}>
                                    {schoolClass.number} {schoolClass.letter} - Anno {schoolClass.year}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <TextField
                        fullWidth
                        type="date"
                        label="Inizio validità"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleInputChange}
                        margin="normal"
                        InputLabelProps={{shrink: true}}
                    />
                    <TextField
                        fullWidth
                        type="date"
                        label="Fine Validità"
                        name="endDate"
                        value={formData.endDate}
                        onChange={handleInputChange}
                        margin="normal"
                        InputLabelProps={{shrink: true}}
                    />
                    <TextField
                        fullWidth
                        type="number"
                        label="Ore settimanali"
                        name="expectedHours"
                        value={formData.expectedHours}
                        onChange={handleInputChange}
                        margin="normal"
                        InputProps={{inputProps: {min: 0, max: 40}}}
                    />
                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseModal} style={{marginRight: "8px"}}>
                            Esci
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleSubmit}>
                            Invia
                        </Button>
                    </Box>
                </Box>
            </Modal>

            <Box display="flex" flexWrap="wrap" gap={3}>
                {schoolClasses.map((schoolClass) => (
                    <Box
                        key={schoolClass.id}
                        flexBasis={{xs: "100%", sm: "calc(50% - 24px)", md: "calc(33.33% - 24px)"}}
                        onClick={() => handleCardClick(schoolClass.id)}
                        style={{cursor: "pointer"}}
                    >
                        <Card>
                            <CardContent>
                                <Typography variant="h6">
                                    {schoolClass.number} {schoolClass.letter}
                                </Typography>
                                <Typography>Anno: {schoolClass.year}</Typography>
                            </CardContent>
                        </Card>
                    </Box>
                ))}
            </Box>
        </div>
    );
};

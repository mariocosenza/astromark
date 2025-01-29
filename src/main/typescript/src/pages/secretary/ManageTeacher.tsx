import React, { useEffect, useState } from "react";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Checkbox,
    CircularProgress,
    FormControlLabel,
    Modal,
    TextField,
    Typography,
} from "@mui/material";
import { useNavigate } from "react-router";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env";

interface TeacherResponse {
    name: string;
    surname: string;
    uuid: string;
}

export const ManageTeacher = () => {
    const [teachers, setTeachers] = useState<TeacherResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error] = useState<string | null>(null);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [modalOpen, setModalOpen] = useState(false);
    const [formData, setFormData] = useState({
        email: "",
        name: "",
        surname: "",
        taxId: "",
        birthDate: "",
        male: true,
        residentialAddress: "",
    });

    const navigate = useNavigate(); // Hook per navigare tra le pagine

    useEffect(() => {
        const fetchTeachers = async () => {
            try {
                const response = await axiosConfig.get<TeacherResponse[]>(`${Env.API_BASE_URL}/teachers/all`);
                setTeachers(response.data);
                setSuccessMessage("Professori recuperati con successo!");
                setErrorMessage(null);
                setLoading(false);
            } catch (err: any) {
                console.error("Failed to fetch teachers:", err);
                setErrorMessage("Impossibile trovare gli insegnanti.");
                setSuccessMessage(null);
                setLoading(false);
            }
        };

        fetchTeachers();
    }, []);

    const handleOpenModal = () => {
        setFormData({
            email: "",
            name: "",
            surname: "",
            taxId: "",
            birthDate: "",
            male: true,
            residentialAddress: "",
        });
        setErrorMessage(null);
        setSuccessMessage(null);
        setModalOpen(true);
    };
    const handleCloseModal = () => setModalOpen(false);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, male: e.target.checked });
    };

    const handleSubmit = async () => {
        // Validazione dei campi obbligatori
        if (
            !formData.email ||
            !formData.name ||
            !formData.surname ||
            !formData.birthDate ||
            !formData.residentialAddress
        ) {
            setErrorMessage("Si prega di compilare tutti i campi obbligatori.");
            setSuccessMessage(null);
            return;
        }

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/teachers/create`, formData);
            setSuccessMessage("Insegnante creato con successo!");
            setErrorMessage(null);
            handleCloseModal();

            // Refresh teacher list
            const response = await axiosConfig.get<TeacherResponse[]>(`${Env.API_BASE_URL}/teachers/all`);
            setTeachers(response.data);
        } catch (err: any) {
            console.error("Failed to create teacher:", err);
            setErrorMessage("Impossibile creare l'insegnante.");
            setSuccessMessage(null);
        }
    };

    const handleCardClick = (uuid: string) => {
        navigate(`/secretary/teachers/${uuid}`);
    };

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
                <Alert severity="error">{error}</Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom>
                Professori
            </Typography>

            {/* Alert di Successo Globale */}
            {successMessage && (
                <Alert
                    severity="success"
                    onClose={() => setSuccessMessage(null)}
                    sx={{ mb: 2 }}
                >
                    {successMessage}
                </Alert>
            )}

            {/* Alert di Errore Globale */}
            {errorMessage && (
                <Alert
                    severity="error"
                    onClose={() => setErrorMessage(null)}
                    sx={{ mb: 2 }}
                >
                    {errorMessage}
                </Alert>
            )}

            <Button variant="contained" color="primary" onClick={handleOpenModal} sx={{ mb: 2 }}>
                Aggiungi professore
            </Button>

            {/* Modale per Aggiungere Insegnante */}
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
                        borderRadius: 2,
                    }}
                >
                    <Typography variant="h6" gutterBottom>
                        Crea professore
                    </Typography>
                    <Box component="form" noValidate>
                        <TextField
                            fullWidth
                            label="Email"
                            name="email"
                            value={formData.email}
                            onChange={handleInputChange}
                            margin="normal"
                            required
                            type="email"
                        />
                        <TextField
                            fullWidth
                            label="Nome"
                            name="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Cognome"
                            name="surname"
                            value={formData.surname}
                            onChange={handleInputChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="CF"
                            name="taxId"
                            value={formData.taxId}
                            onChange={handleInputChange}
                            margin="normal"
                        />
                        <TextField
                            fullWidth
                            type="date"
                            label="Data di nascita"
                            name="birthDate"
                            value={formData.birthDate}
                            onChange={handleInputChange}
                            margin="normal"
                            InputLabelProps={{ shrink: true }}
                            required
                        />
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={formData.male}
                                    onChange={handleCheckboxChange}
                                    name="male"
                                />
                            }
                            label="Uomo"
                        />
                        <TextField
                            fullWidth
                            label="Indirizzo"
                            name="residentialAddress"
                            value={formData.residentialAddress}
                            onChange={handleInputChange}
                            margin="normal"
                            required
                        />
                    </Box>

                    {/* Alert di Errore nel Modale */}
                    {errorMessage && (
                        <Alert
                            severity="error"
                            onClose={() => setErrorMessage(null)}
                            sx={{ mt: 2 }}
                        >
                            {errorMessage}
                        </Alert>
                    )}

                    {/* Alert di Successo nel Modale */}
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
                        <Button onClick={handleCloseModal} sx={{ mr: 2 }} disabled={loading}>
                            Chiudi
                        </Button>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleSubmit}
                            disabled={loading}
                        >
                            Invia
                        </Button>
                    </Box>
                </Box>
            </Modal>

            <Box display="flex" flexWrap="wrap" gap={3}>
                {teachers.map((teacher) => (
                    <Card
                        key={teacher.uuid}
                        sx={{
                            flex: "1 1 calc(33.33% - 24px)",
                            minWidth: "300px",
                            maxWidth: "400px",
                            boxShadow: 2,
                            borderRadius: "12px",
                        }}
                        onClick={() => handleCardClick(teacher.uuid)}
                        style={{ cursor: "pointer" }}
                    >
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                {teacher.name} {teacher.surname}
                            </Typography>
                        </CardContent>
                    </Card>
                ))}
            </Box>
        </Box>
    );
};

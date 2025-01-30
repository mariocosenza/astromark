import React, {useEffect, useState} from "react";
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
import {useNavigate} from "react-router";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env";

// Interface for Teacher response
interface TeacherResponse {
    name: string;
    surname: string;
    uuid: string;
}

// Regular Expressions for validation
const NAME_SURNAME_REGEX = /^[a-zA-Z]([a-zA-Z]*)(?: [a-zA-Z]([a-zA-Z]*)){0,3}$/;
const ADDRESS_REGEX = /^[a-zA-Z0-9\s.,]+$/;

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

    const [fieldErrors, setFieldErrors] = useState<{
        email?: string;
        name?: string;
        surname?: string;
        residentialAddress?: string;
        birthDate?: string;
    }>({});

    const navigate = useNavigate(); // Hook for navigation

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
        setFieldErrors({});
        setModalOpen(true);
    };
    const handleCloseModal = () => setModalOpen(false);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData({...formData, [name]: value});

        // Reset field error on change
        setFieldErrors((prev) => ({...prev, [name]: undefined}));
    };

    const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({...formData, male: e.target.checked});
    };

    const handleSubmit = async () => {
        // Reset previous errors
        setFieldErrors({});
        setErrorMessage(null);
        setSuccessMessage(null);

        let hasError = false;
        const errors: typeof fieldErrors = {};

        // Validate required fields
        if (!formData.email) {
            errors.email = "Email necessaria.";
            hasError = true;
        }
        if (!formData.name) {
            errors.name = "Nome richiesto.";
            hasError = true;
        } else if (!NAME_SURNAME_REGEX.test(formData.name.trim())) {
            errors.name = "Nome non valido.";
            hasError = true;
        }
        if (!formData.surname) {
            errors.surname = "Cognome richiesto.";
            hasError = true;
        } else if (!NAME_SURNAME_REGEX.test(formData.surname.trim())) {
            errors.surname = "Cognome non valida.";
            hasError = true;
        }
        if (!formData.birthDate) {
            errors.birthDate = "Data necessaria.";
            hasError = true;
        }
        if (!formData.residentialAddress) {
            errors.residentialAddress = "Indirizzo richiesto.";
            hasError = true;
        } else if (!ADDRESS_REGEX.test(formData.residentialAddress.trim())) {
            errors.residentialAddress = "Formato indirizzo non valido.";
            hasError = true;
        }

        if (hasError) {
            setFieldErrors(errors);
            setErrorMessage("Correggi gli errori.");
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
                <CircularProgress/>
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
        <Box sx={{padding: "16px"}}>
            <Typography variant="h4" gutterBottom>
                Professori
            </Typography>

            {/* Global Success Alert */}
            {successMessage && (
                <Alert
                    severity="success"
                    onClose={() => setSuccessMessage(null)}
                    sx={{mb: 2}}
                >
                    {successMessage}
                </Alert>
            )}

            {/* Global Error Alert */}
            {errorMessage && (
                <Alert
                    severity="error"
                    onClose={() => setErrorMessage(null)}
                    sx={{mb: 2}}
                >
                    {errorMessage}
                </Alert>
            )}

            <Button variant="contained" color="primary" onClick={handleOpenModal} sx={{mb: 2}}>
                Aggiungi professore
            </Button>

            {/* Modal to Add Teacher */}
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
                            error={Boolean(fieldErrors.email)}
                            helperText={fieldErrors.email}
                        />
                        <TextField
                            fullWidth
                            label="Nome"
                            name="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            margin="normal"
                            required
                            error={Boolean(fieldErrors.name)}
                            helperText={fieldErrors.name}
                        />
                        <TextField
                            fullWidth
                            label="Cognome"
                            name="surname"
                            value={formData.surname}
                            onChange={handleInputChange}
                            margin="normal"
                            required
                            error={Boolean(fieldErrors.surname)}
                            helperText={fieldErrors.surname}
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
                            InputLabelProps={{shrink: true}}
                            required
                            error={Boolean(fieldErrors.birthDate)}
                            helperText={fieldErrors.birthDate}
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
                            error={Boolean(fieldErrors.residentialAddress)}
                            helperText={fieldErrors.residentialAddress}
                        />
                    </Box>

                    {/* Modal Error Alert */}
                    {errorMessage && (
                        <Alert
                            severity="error"
                            onClose={() => setErrorMessage(null)}
                            sx={{mt: 2}}
                        >
                            {errorMessage}
                        </Alert>
                    )}

                    {/* Modal Success Alert */}
                    {successMessage && (
                        <Alert
                            severity="success"
                            onClose={() => setSuccessMessage(null)}
                            sx={{mt: 2}}
                        >
                            {successMessage}
                        </Alert>
                    )}

                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseModal} sx={{mr: 2}} disabled={loading}>
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
                        style={{cursor: "pointer"}}
                    >
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                {teacher.name} {teacher.surname}
                            </Typography>
                        </CardContent>
                    </Card>
                ))}
            </Box>
        </Box>)
};


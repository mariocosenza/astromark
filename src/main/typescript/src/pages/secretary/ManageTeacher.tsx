import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    CircularProgress,
    Alert,
    Card,
    CardContent,
    Button,
    Modal,
    TextField,
    FormControlLabel,
    Checkbox,
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
    const [error, setError] = useState<string | null>(null);
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
                setLoading(false);
            } catch (err) {
                console.error("Failed to fetch teachers:", err);
                setError("Failed to fetch teachers.");
                setLoading(false);
            }
        };

        fetchTeachers();
    }, []);

    const handleOpenModal = () => setModalOpen(true);
    const handleCloseModal = () => setModalOpen(false);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, male: e.target.checked });
    };

    const handleSubmit = async () => {
        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/teachers/create`, formData);
            handleCloseModal();
            alert("Teacher created successfully!");
            // Refresh teacher list
            const response = await axiosConfig.get<TeacherResponse[]>(`${Env.API_BASE_URL}/teachers/all`);
            setTeachers(response.data);
        } catch (err) {
            console.error("Failed to create teacher:", err);
            alert("Failed to create teacher.");
        }
    };

    const handleCardClick = (uuid: string) => {
        navigate(`/secretary/teachers/${uuid}`);
    };

    if (loading) return <CircularProgress />;
    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <Box sx={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom>
                Teacher List
            </Typography>
            <Button variant="contained" color="primary" onClick={handleOpenModal} sx={{ mb: 2 }}>
                Add Teacher
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
                        borderRadius: 2,
                    }}
                >
                    <Typography variant="h6" gutterBottom>
                        Create Teacher
                    </Typography>
                    <TextField
                        fullWidth
                        label="Email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        label="Name"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        label="Surname"
                        name="surname"
                        value={formData.surname}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        label="Tax ID"
                        name="taxId"
                        value={formData.taxId}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        type="date"
                        label="Birth Date"
                        name="birthDate"
                        value={formData.birthDate}
                        onChange={handleInputChange}
                        margin="normal"
                        InputLabelProps={{ shrink: true }}
                    />
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={formData.male}
                                onChange={handleCheckboxChange}
                                name="male"
                            />
                        }
                        label="Male"
                    />
                    <TextField
                        fullWidth
                        label="Residential Address"
                        name="residentialAddress"
                        value={formData.residentialAddress}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <Box mt={2} display="flex" justifyContent="flex-end">
                        <Button onClick={handleCloseModal} sx={{ mr: 2 }}>
                            Cancel
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleSubmit}>
                            Submit
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
                        onClick={() => handleCardClick(teacher.uuid)} // Gestisce il click sulla card
                        style={{ cursor: "pointer" }}
                    >
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                {teacher.name} {teacher.surname}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                UUID: {teacher.uuid}
                            </Typography>
                        </CardContent>
                    </Card>
                ))}
            </Box>
        </Box>
    );
};

export default ManageTeacher;

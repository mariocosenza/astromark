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
} from "@mui/material";
import { SelectChangeEvent } from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env";
import {useParams} from "react-router";

interface SchoolClassResponse {
    id: number;
    year: number;
    letter: string;
    number: number;
    description: string;
}

export const TeacherDetails = () => {
    const { teacheruuid } = useParams<{ teacheruuid: string }>();
    const [classes, setClasses] = useState<SchoolClassResponse[]>([]);
    const [allClasses, setAllClasses] = useState<SchoolClassResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedClassId, setSelectedClassId] = useState<string>("");
    const [isCoordinator, setIsCoordinator] = useState<boolean>(false);

    useEffect(() => {
        const fetchClasses = async () => {
            try {
                const [classesResponse, allClassesResponse] = await Promise.all([
                    axiosConfig.get<SchoolClassResponse[]>(`${Env.API_BASE_URL}/class-management/${teacheruuid}/class`),
                    axiosConfig.get<SchoolClassResponse[]>(`${Env.API_BASE_URL}/class-management/all`),
                ]);

                setClasses(classesResponse.data);
                setAllClasses(allClassesResponse.data);
                setLoading(false);
            } catch (err) {
                console.error("Failed to fetch classes:", err);
                setError("Failed to fetch class data.");
                setLoading(false);
            }
        };

        fetchClasses();
    }, []);

    const handleOpenModal = () => setModalOpen(true);
    const handleCloseModal = () => {
        setSelectedClassId("");
        setIsCoordinator(false);
        setModalOpen(false);
    };

    const handleClassChange = (event: SelectChangeEvent<string>) => {
        setSelectedClassId(event.target.value);
    };

    const handleCoordinatorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setIsCoordinator(event.target.checked);
    };

    const handleSubmit = async () => {
        if (!selectedClassId) {
            alert("Please select a class.");
            return;
        }
        console.log(teacheruuid)

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/class-management/${teacheruuid}/add-teacher-to-class`, {
                classId: selectedClassId,
                isCoordinator,
            });
            alert("Teacher added to class successfully!");
            handleCloseModal();
        } catch (err) {
            console.error("Failed to add teacher to class:", err);
            alert("Failed to add teacher to class.");
        }
    };

    if (loading) return <CircularProgress sx={{ display: "block", margin: "auto", mt: 4 }} />;
    if (error) return <Alert severity="error" sx={{ maxWidth: "600px", margin: "auto", mt: 4 }}>{error}</Alert>;

    return (
        <Box sx={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom sx={{ textAlign: "center", fontWeight: "bold" }}>
                Teacher Classes
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
                                {schoolClass.description}
                            </Typography>
                        </Box>
                        <Button
                            variant="outlined"
                            color="secondary"
                            size="small"
                            onClick={() => console.log(`Remove class ID: ${schoolClass.id}`)}
                        >
                            Remove
                        </Button>
                    </Card>
                ))}
            </Box>

            <Button
                variant="contained"
                color="primary"
                fullWidth
                sx={{ marginTop: "16px", textTransform: "none" }}
                onClick={handleOpenModal}
            >
                Add to Class
            </Button>

            {/* Modal for Adding Teacher to Class */}
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
                                <MenuItem key={schoolClass.id} value={schoolClass.id.toString()}>
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
                        <Button onClick={handleCloseModal} sx={{ mr: 2 }}>
                            Cancel
                        </Button>
                        <Button variant="contained" color="primary" onClick={handleSubmit}>
                            Submit
                        </Button>
                    </Box>
                </Box>
            </Modal>
        </Box>
    );
};

export default TeacherDetails;

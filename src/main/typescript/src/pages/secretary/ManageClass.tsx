import {useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {Alert, Box, Card, CardContent, CircularProgress, Typography,} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig";
import {Env} from "../../Env.ts";

interface SchoolClassResponse {
    id: number;
    year: number;
    letter: string;
    number: number;
}

export const ManageClass = () => {
    const [schoolClasses, setSchoolClasses] = useState<SchoolClassResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [successMessage, setSuccessMessage] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSchoolClasses = async () => {
            try {
                const response = await axiosConfig.get<SchoolClassResponse[]>(`${Env.API_BASE_URL}/class-management/all`);
                setSchoolClasses(response.data);
                setSuccessMessage("Classi recuperate con successo!");
                setErrorMessage(null);
                setLoading(false);
            } catch (err) {
                console.error("Errore nel recupero delle classi:", err);
                setErrorMessage("Problema nel recuperare le lezioni scolastiche.");
                setSuccessMessage(null);
                setLoading(false);
            }
        };

        fetchSchoolClasses();
    }, []);

    const handleCardClick = (classId: number) => {
        navigate(`/secretary/class-info/${classId}`);
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress/>
            </Box>
        );
    }

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

            {/* Alert di Errore */}
            {errorMessage && (
                <Alert
                    severity="error"
                    onClose={() => setErrorMessage(null)}
                    sx={{mb: 2}}
                >
                    {errorMessage}
                </Alert>
            )}

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

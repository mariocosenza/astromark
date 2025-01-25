import { useEffect, useState } from "react";
import { Card, CardContent, Typography, Box, CircularProgress, Alert } from "@mui/material";
import { useNavigate } from "react-router";
import axiosConfig from "../../services/AxiosConfig";
import { Env } from "../../Env.ts";


interface SchoolClassResponse {
    id: number;
    year: number;
    letter: string;
    number: number;
}

const SchoolClassList = () => {
    const [schoolClasses, setSchoolClasses] = useState<SchoolClassResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchSchoolClasses = async () => {
            try {
                const response = await axiosConfig.get<SchoolClassResponse[]>(`${Env.API_BASE_URL}/class-management/all`);
                setSchoolClasses(response.data);
                setLoading(false);
            } catch (err) {
                setError("Failed to fetch school classes.");
                setLoading(false);
            }
        };

        fetchSchoolClasses();
    }, []);

    const handleCardClick = (classId: number) => {
        navigate(`/secretary/class-schedule/${classId}`);
    };





    if (loading) return <CircularProgress />;
    if (error) return <Alert severity="error">{error}</Alert>;

    return (
        <div style={{ padding: "16px" }}>
            <Typography variant="h4" gutterBottom>
                School Classes
            </Typography>
            <Box display="flex" flexWrap="wrap" gap={3}>
                {schoolClasses.map((schoolClass) => (
                    <Box
                        key={schoolClass.id}
                        flexBasis={{ xs: "100%", sm: "calc(50% - 24px)", md: "calc(33.33% - 24px)" }}
                        onClick={() => handleCardClick(schoolClass.id)}
                        style={{ cursor: "pointer" }}
                    >
                        <Card>
                            <CardContent>
                                <Typography variant="h6">
                                    Number: {schoolClass.number} Letter: {schoolClass.letter}
                                </Typography>
                                <Typography>Year: {schoolClass.year}</Typography>
                            </CardContent>
                        </Card>
                    </Box>
                ))}
            </Box>
        </div>
    );
};

export const ManageTimetable = SchoolClassList;

export default SchoolClassList;

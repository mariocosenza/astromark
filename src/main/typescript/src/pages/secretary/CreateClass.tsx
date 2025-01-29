import {Alert, AlertTitle, Box, Button, Container, Paper, TextField, Typography} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {useState} from "react";

export type SchoolClassRequest = {
    letter: string;
    number: number;
}

export const CreateClass = () => {
    const [letter, setLetter] = useState<string>("");
    const [number, setNumber] = useState<number | string>("");
    const [alert, setAlert] = useState<{
        type: 'success' | 'error';
        message: string;
    } | null>(null);

    const isValidLetter = (value: string) => /^[A-Z]{1,2}$/.test(value);

    const handleSubmit = async () => {
        // Validate inputs
        if (!isValidLetter(letter) || !number) {
            setAlert({
                type: 'error',
                message: 'Inserire una lettera valida (1-2 maiuscole) e un numero'
            });
            return;
        }

        const schoolClass: SchoolClassRequest = {
            letter: letter,
            number: typeof number === 'string' ? parseInt(number) : number
        }

        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/class-management/class`, schoolClass);

            // Success alert
            setAlert({
                type: 'success',
                message: 'Classe creata con successo!'
            });

            // Reset form
            setLetter("");
            setNumber("");
        } catch (error) {
            // Error alert
            setAlert({
                type: 'error',
                message: 'Errore nella creazione della classe'
            });
        }
    }

    return (
        <Container maxWidth="xs">
            <Paper
                elevation={3}
                sx={{
                    padding: 3,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    marginTop: 4
                }}
            >
                <Typography variant="h5" gutterBottom color={'primary'}>
                    Crea Nuova Classe
                </Typography>

                {alert && (
                    <Alert
                        severity={alert.type}
                        onClose={() => setAlert(null)}
                        sx={{width: '100%', marginBottom: 2}}
                    >
                        <AlertTitle>{alert.type === 'success' ? 'Successo' : 'Errore'}</AlertTitle>
                        {alert.message}
                    </Alert>
                )}

                <Box
                    component="form"
                    sx={{
                        width: '100%',
                        display: 'flex',
                        flexDirection: 'column',
                        gap: 2
                    }}
                >
                    <TextField
                        fullWidth
                        label="Lettera"
                        variant="outlined"
                        value={letter}
                        onChange={(e) => {
                            const value = e.target.value.toUpperCase();
                            if (value.length <= 2) {
                                setLetter(value);
                            }
                        }}
                        error={letter !== "" && !isValidLetter(letter)}
                        helperText={letter !== "" && !isValidLetter(letter) ? "Inserire 1-2 lettere maiuscole" : ""}
                    />

                    <TextField
                        fullWidth
                        type="number"
                        label="Numero"
                        variant="outlined"
                        value={number}
                        onChange={(e) => setNumber(e.target.value)}
                        error={!number && number !== ''}
                        helperText={!number && number !== '' ? "Il numero è obbligatorio" : ""}
                    />

                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSubmit}
                        fullWidth
                    >
                        Crea Classe
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};
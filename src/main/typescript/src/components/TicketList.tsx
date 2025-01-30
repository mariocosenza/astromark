import React, {useMemo, useState} from "react";
import {
    Alert,
    Avatar,
    Box,
    Button,
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
    Snackbar,
    Typography,
} from "@mui/material";
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import {SelectedTicket} from "../services/TicketService.ts";
import {getRole} from "../services/AuthService.ts";
import {TicketResponse} from "../entities/TicketResponse.ts";
import {Role} from "./route/ProtectedRoute.tsx";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";
import {AxiosError} from "axios";

export type TicketCard = {
    id: string;
    avatar: string;
    title: string;
    description: string;
    hexColor: string;
    ticket: TicketResponse;
}

export type Props = {
    list: TicketCard[];
    onTicketClick: () => void;
}

interface AlertState {
    message: string;
    severity: 'error' | 'success';
    open: boolean;
}

interface ApiErrorResponse {
    error?: string;
    detail?: string;
}

export const TicketList: React.FC<Props> = ({list: initialList, onTicketClick}) => {
    const [list, setList] = useState<TicketCard[]>(initialList);
    const [selectedCategory, setSelectedCategory] = useState<string>("Tutte le categorie");
    const [alertState, setAlertState] = useState<AlertState>({
        message: '',
        severity: 'success',
        open: false
    });
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    // Update local list when prop changes
    React.useEffect(() => {
        setList(initialList);
    }, [initialList]);

    const categories = useMemo(() => {
        const cats = list.map(item => item.ticket.category);
        return ["Tutte le categorie", ...Array.from(new Set(cats))];
    }, [list]);

    const handleCloseAlert = () => {
        setAlertState(prev => ({...prev, open: false}));
    };

    const showAlert = (message: string, severity: 'error' | 'success') => {
        setAlertState({
            message,
            severity,
            open: true
        });
    };

    const handleApiError = (error: AxiosError<ApiErrorResponse>) => {
        const errorResponse = error.response?.data;
        let errorMessage = "Errore durante la chiusura del ticket. Riprova più tardi.";

        if (errorResponse) {
            errorMessage = errorResponse.error || errorResponse.detail || errorMessage;
        }

        return errorMessage;
    };

    const updateTicketStatus = (ticketId: string, solved: boolean) => {
        SelectedTicket.closed = true;
        setList(currentList =>
            currentList.map(item =>
                item.id === ticketId
                    ? {
                        ...item,
                        ticket: {
                            ...item.ticket,
                            closed: true,
                            solved: solved
                        }
                    }
                    : item
            )
        );
    };

    const sendCloseUnsolved = async (id: string, e: React.MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation();
        setIsSubmitting(true);

        try {
            await axiosConfig.patch(`${Env.API_BASE_URL}/tickets/${id}/closeUnsolved`);
            updateTicketStatus(id, false);
            showAlert("Ticket chiuso come non risolto con successo", 'success');
        } catch (err) {
            const errorMessage = err instanceof AxiosError ?
                handleApiError(err) :
                "Errore durante la chiusura del ticket. Riprova più tardi.";
            showAlert(errorMessage, 'error');
        } finally {
            setIsSubmitting(false);
        }
    };

    const sendCloseSolve = async (id: string, e: React.MouseEvent<HTMLButtonElement>) => {
        e.stopPropagation();
        setIsSubmitting(true);

        try {
            await axiosConfig.patch(`${Env.API_BASE_URL}/tickets/${id}/closeAndSolve`);
            updateTicketStatus(id, true);
            showAlert("Ticket chiuso come risolto con successo", 'success');
        } catch (err) {
            const errorMessage = err instanceof AxiosError ?
                handleApiError(err) :
                "Errore durante la chiusura del ticket. Riprova più tardi.";
            showAlert(errorMessage, 'error');
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleChange = (event: SelectChangeEvent<string>) => {
        setSelectedCategory(event.target.value);
    };

    const filteredList = useMemo(() => {
        if (selectedCategory === "Tutte le categorie") {
            return list;
        }
        return list.filter(item => item.ticket.category === selectedCategory);
    }, [list, selectedCategory]);

    const handleTicketClick = (id: string, closed: boolean) => {
        SelectedTicket.ticketId = id;
        SelectedTicket.closed = closed;
        onTicketClick();
    };

    return (
        <Box sx={{width: '100%'}}>
            <Snackbar
                open={alertState.open}
                autoHideDuration={6000}
                onClose={handleCloseAlert}
                anchorOrigin={{vertical: 'top', horizontal: 'center'}}
            >
                <Alert
                    onClose={handleCloseAlert}
                    severity={alertState.severity}
                    variant="filled"
                    sx={{width: '100%'}}
                >
                    {alertState.message}
                </Alert>
            </Snackbar>

            {getRole().toUpperCase() === Role.SECRETARY &&
                <FormControl variant="standard" fullWidth sx={{marginBottom: '1rem'}}>
                    <InputLabel id="category-select-label">Categoria</InputLabel>
                    <Select
                        labelId="category-select-label"
                        value={selectedCategory}
                        onChange={handleChange}
                        label="Categoria"
                    >
                        {categories.map((category) => (
                            <MenuItem key={category} value={category}>
                                {category}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            }

            {filteredList.map((item) => (
                <Box
                    className={'listItem hover-animation'}
                    display="flex"
                    justifyContent={'space-between'}
                    margin={'0.5rem 0'}
                    padding={'1rem'}
                    key={item.id}
                    onClick={() => handleTicketClick(item.id, item.ticket.closed)}
                    sx={{
                        borderRadius: '8px',
                        cursor: 'pointer',
                        '&:hover': {
                            backgroundColor: '#f5f5f5',
                        },
                    }}
                >
                    <Box display={'flex'} alignItems={'center'}>
                        <Avatar sx={{bgcolor: item.hexColor, marginRight: '1rem'}}>{item.avatar}</Avatar>
                        <Box>
                            <Typography variant='subtitle1' fontWeight={'bold'}>
                                {item.title} {getRole().toUpperCase() === Role.SECRETARY ? ' - ' + (item.ticket.isTeacher ? 'Docente' : 'Genitore') : ''}
                            </Typography>
                            <Typography variant='subtitle2' fontWeight={'bold'} color='text.secondary'>
                                {"Stato: " + (item.ticket.solved ? 'Risolto' : 'Aperto') + (item.ticket.closed ? ' - Chiuso' : '')}
                            </Typography>
                            <Typography variant='body2' color='text.secondary'>
                                {item.description}
                            </Typography>
                            {!item.ticket.closed && getRole().toUpperCase() == Role.SECRETARY && (
                                <Box sx={{mt: 2, display: 'flex', gap: 2}}>
                                    <Button
                                        variant="contained"
                                        color="error"
                                        disabled={isSubmitting}
                                        onClick={(e) => sendCloseUnsolved(item.id, e)}
                                    >
                                        Chiudi Non Risolto
                                    </Button>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        disabled={isSubmitting}
                                        onClick={(e) => sendCloseSolve(item.id, e)}
                                    >
                                        Chiudi Risolto
                                    </Button>
                                </Box>
                            )}
                        </Box>
                    </Box>

                    <IconButton>
                        <ArrowForwardIosIcon/>
                    </IconButton>
                </Box>
            ))}

            {filteredList.length === 0 && (
                <Typography variant="body1" color="text.secondary" align="center">
                    Nessun ticket trovato per la categoria selezionata.
                </Typography>
            )}
        </Box>
    );
}

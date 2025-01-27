import React, {useEffect, useState} from "react";
import {Box, Button, Card, CardContent, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, Stack, TextField, Typography} from "@mui/material";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {Booking, ReceptionTimeslotResponse} from "../parents/Reception.tsx";
import DatePicker, {DateObject} from "react-multi-date-picker";
import {getId} from "../../services/AuthService.ts";
import {ListBooked} from "../../components/ListBooked.tsx";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";


export const TeacherReception: React.FC = () => {
    const [timeslots, setTimeslots] = useState<ReceptionTimeslotResponse[]>([]);
    const [open, setOpen] = useState<boolean>(false);
    const [newDate, setNewDate] = useState<DateObject | null>(null);
    const [newHour, setNewHour] = useState<number>(1);
    const [newCapacity, setNewCapacity] = useState<number>(1);
    const [newMode, setNewMode] = useState<string>("In presenza");
    const [changeView, setChangeView] = useState<boolean>(false);
    const [selected, setSelected] = useState<ReceptionTimeslotResponse | null>(null);
    const [booked, setBooked] = useState<Booking[]>([])
    const [loading, setLoading] = useState<boolean>(true);
    const [loadingBooked, setLoadingBooked] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<ReceptionTimeslotResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/agenda/reception/teacher/${getId()}/timeslots`);
            if (response.data.length){
                setTimeslots(response.data)
            }

            setLoading(false)
        } catch (error) {
            console.error(error);
        }
    }

    const fetchBooked = async ()  => {
        try {
            const response: AxiosResponse<Booking[]> = await axiosConfig.get(`${Env.API_BASE_URL}/agenda/reception/timeslot/booked`);
            if (response.data.length){
                const selectedBooked = response.data.filter(
                    (slot) => (slot.date === selected?.date) && (slot.hour === selected.hour)
                );

                setBooked(selectedBooked)
            }

            setLoadingBooked(false)
        } catch (error) {
            console.error(error);
        }
    }

    const handleAddTimeslot = () => {
        alert("add slot")
        setOpen(false)
    };

    const handleSelect = (timeslot: ReceptionTimeslotResponse | null) => {
        setSelected(timeslot)
        if (selected) {
            setLoadingBooked(true)
            fetchBooked()
            setChangeView(true)
        }
    }

    const handleReturnBack = () => {
        setSelected(null)
        setLoading(true)
        fetchData()
        setChangeView(false)
    }

    return (
        <div>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Ricevimento
            </Typography>

            <Dialog open={open} onClose={() => setOpen(false)}>
                <DialogTitle>Aggiungi Nuovo Orario</DialogTitle>
                <DialogContent>
                    <Stack direction={'column'} justifyContent={'center'}>
                        <Typography variant="caption" color={'textSecondary'}>
                            Data
                        </Typography>
                        <DatePicker
                            style={{width: '97%'}}
                            value={newDate}
                            onChange={(selectedDate) => {selectedDate && setNewDate(selectedDate)}}
                        />
                    </Stack>
                    <TextField
                        label="Ora (HH)"
                        type="number"
                        fullWidth
                        value={newHour}
                        onChange={(e) => setNewHour(Number(e.target.value))}
                        margin="normal"
                    />
                    <TextField
                        label="Capacità"
                        type="number"
                        fullWidth
                        value={newCapacity}
                        onChange={(e) => setNewCapacity(Number(e.target.value))}
                        margin="normal"
                    />
                    <TextField
                        label="Modalità"
                        fullWidth
                        value={newMode}
                        onChange={(e) => setNewMode(e.target.value)}
                        margin="normal"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpen(false)}>Annulla</Button>
                    <Button onClick={handleAddTimeslot} variant="contained" color="primary">
                        Aggiungi
                    </Button>
                </DialogActions>
            </Dialog>

            {changeView ? (
                    <Stack justifyContent="center" alignItems={'center'} margin={'1rem'} spacing={'3rem'}>
                        {loadingBooked ? <CircularProgress size={100}/> : <ListBooked list={booked}/>}
                        <Button variant="contained" size={'large'} onClick={() => handleReturnBack()}
                                sx={{borderRadius: 4, backgroundColor: 'var(--md-sys-color-primary)'}}>
                            Torna indietro
                        </Button>
                    </Stack>
            ) : (
                <Stack justifyContent={'center'} alignItems={'center'}>
                    <Box className={'grid-container'} justifyContent={'center'} width={'80%'} margin={'1rem 10%'}>
                        {loading || timeslots.map((timeslot) => (
                            <Card className={'hover-animation'} key={timeslot.id} sx={{borderRadius: 4}}
                                  onClick={() => handleSelect(timeslot)}>

                                <CardContent sx={{backgroundColor: 'var(--md-sys-color-tertiary-container)'}}>
                                    <Typography variant="h6">
                                        Slot del {timeslot.date}
                                    </Typography>
                                    <Typography variant="body2" color="textSecondary">
                                        Modalità: {timeslot.mode}
                                    </Typography>
                                    <Typography variant="body2" color="textSecondary">
                                        Capacità: {timeslot.booked}/{timeslot.capacity}
                                    </Typography>
                                </CardContent>
                            </Card>
                        ))}
                    </Box>
                    {loading ? <CircularProgress size={150}/> : timeslots.length ? (
                        <Button variant={'outlined'} size={'large'} onClick={() => setOpen(true)}
                                sx={{borderColor: 'white', borderRadius: '2rem', padding: '2rem'}}>
                            <Stack alignItems={'center'}>
                                <AddCircleOutlineIcon fontSize={'large'} color={'success'} sx={{fontSize: '7rem'}}/>
                                <Typography color="textSecondary">
                                    Aggiungi orario
                                </Typography>
                            </Stack>
                        </Button>
                    ) : (
                        <Typography variant="body1" color="textSecondary" align="center">
                            Nessun orario disponibile al momento.
                        </Typography>
                    )}
                </Stack>
            )}
        </div>
    );
};


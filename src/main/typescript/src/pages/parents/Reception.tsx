import React, {useEffect, useState} from "react";
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import {Alert, Autocomplete, Box, Button, Divider, Snackbar, Tab, Tabs, TextField} from "@mui/material";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {ListBooked} from "../../components/ListBooked.tsx";
import Paper from "@mui/material/Paper";
export type BookingId = {
    parentId: string
    receptionTimeslotId: number
}

export type Booking = {
    id: BookingId
    bookingOrder: number
    confirmed: boolean
    refused: boolean
    date: string
    hour: number
    mode: string
    name: string
    surname: string
}

export type ReceptionTimeslotResponse = {
    id: number
    capacity: number
    booked: number
    mode: string
    hour: number
    date: string
    name: string
    surname: string
}

type Teacher = {
    id: string
    name: string
    surname: string
}


const BookedSlots: React.FC = () => {
    const [booking, setBooking] = useState<Booking[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<Booking[]> = await axiosConfig.get(`${Env.API_BASE_URL}/agenda/reception/timeslot/booked`);
            setBooking(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    };
    return (
        <div style={{ display: 'flex', justifyContent: 'center',  marginTop: '1rem'}}>
            {
                loading ?
                    <div>Loading...</div> :
                    <ListBooked
                        list={booking}
                    />
            }
        </div>
    );
}

const BookSlot: React.FC = () => {
    const [teachers, setTeachers] = useState<Teacher[]>([]);
    const [selectedTeacher, setSelectedTeacher] = useState<Teacher>();
    const [loading, setLoading] = useState<boolean>(true);
    const [selectedSlot, setSelectedSlot] = useState<number>(-1);
    const [rows, setRows] = useState<ReceptionTimeslotResponse[]>([]);
    const [error, setError] = useState<string>("");
    const [showError, setShowError] = useState(false);
    const [selectedRow, setSelectedRow] = useState<ReceptionTimeslotResponse | null>(null);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<Teacher[]> = await axiosConfig.get(
                `${Env.API_BASE_URL}/parents/teachers`
            );
            let responsePage;
            if (selectedTeacher === undefined) {
                responsePage = await axiosConfig.get(
                    `${Env.API_BASE_URL}/agenda/reception/teacher/${response.data[0].id}/timeslots`
                );
            } else {
                responsePage = await axiosConfig.get(
                    `${Env.API_BASE_URL}/agenda/reception/teacher/${selectedTeacher?.id}/timeslots`
                );
            }
            setTeachers(response.data);
            setRows(responsePage.data);
            setLoading(false);
        } catch (error) {
            setError("Errore nel caricamento dei dati");
            setShowError(true);
            console.error(error);
        }
    };

    const handleButtonClick = async () => {
        try {
            await axiosConfig.patch(
                `${Env.API_BASE_URL}/agenda/reception/timeslot/${selectedSlot}/book`
            );
            await fetchData();
        } catch (error) {
            setError("Impossibile effettuare la prenotazione. Riprova più tardi.");
            setShowError(true);
            console.error(error);
        }
    }

    const onRowsSelectionHandler = (ids: any) => {
        const selectedRowsData = ids.map((id: any) =>
            rows.find((row) => row.id === id)
        );
        const row = selectedRowsData[0];
        setSelectedRow(row || null);
        setSelectedSlot(row?.id ?? -1);
    };

    const isBookingDisabled = () => {
        if (selectedSlot === -1) return true;
        if (!selectedRow) return true;
        return selectedRow.booked >= selectedRow.capacity;
    };

    const columns: GridColDef<(typeof rows)[number]>[] = [
        { field: 'id', headerName: 'ID', width: 90 },
        {
            field: 'date',
            headerName: 'Data',
            type: 'string',
            width: 150,
            editable: false,
        },
        {
            field: 'hour',
            headerName: 'Ora',
            type: 'number',
            width: 150,
            editable: false,
        },
        {
            field: 'mode',
            headerName: 'Modalità',
            type: 'string',
            width: 110,
            editable: false,
        },
        {
            field: 'capacity',
            headerName: 'Capacità',
            type: 'number',
            description: 'This column has a value getter and is not sortable.',
            sortable: false,
            width: 160,
        },
        {
            field: 'booked',
            headerName: 'Prenotati',
            type: 'number',
            description: 'This column has a value getter and is not sortable.',
            sortable: false,
            width: 160,
        },
    ];

    return (
        <Box sx={{
            height: 'calc(95vh - 112px)',
            display: 'flex',
            flexDirection: 'column',
            p: 2,
            gap: 2
        }}>
            <Paper elevation={1} sx={{ p: 2 }}>
                {!loading && (
                    <Autocomplete
                        id="teacher-select"
                        defaultValue={teachers[0]}
                        options={teachers}
                        onChange={(_, v) => {
                            if (v !== null) {
                                console.log(v)
                                setSelectedTeacher(v);
                                fetchData();
                            }
                        }}
                        getOptionLabel={(option) => `${option.name} ${option.surname}`}
                        renderInput={(params) => (
                            <TextField {...params} label="Docente" />
                        )}
                    />
                )}
            </Paper>

            <Paper elevation={1} sx={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                {!loading && (
                    <>
                        <DataGrid
                            rows={rows}
                            columns={columns}
                            initialState={{
                                pagination: {
                                    paginationModel: {
                                        pageSize: 10,
                                    },
                                },
                            }}
                            pageSizeOptions={[5, 10, 25]}
                            paginationMode="client"
                            autoPageSize
                            checkboxSelection={true}
                            disableColumnSelector={true}
                            disableMultipleRowSelection={true}
                            localeText={{
                                columnMenuSortAsc: 'Ordina Ascendente',
                                columnMenuSortDesc: 'Ordina Discendente',
                                columnMenuFilter: 'Filtra',
                                columnMenuUnsort: 'Annulla ordinamento',
                                columnMenuHideColumn: 'Nascondi colonna',
                                footerTotalRows: 'Totale righe',
                                noRowsLabel: 'Nessun risultato',
                                footerTotalVisibleRows: (count) =>
                                    `${count.toLocaleString()} righe`,
                                footerRowSelected: (count) =>
                                    `${count.toLocaleString()} ${
                                        count === 1 ? 'riga selezionata' : 'righe selezionate'
                                    }`,
                                MuiTablePagination: {
                                    labelDisplayedRows: ({ from, to, count }) => `${from}-${to} di ${count}`,
                                    labelRowsPerPage: 'Righe per pagina:'
                                }
                            }}
                            onRowSelectionModelChange={(ids) => onRowsSelectionHandler(ids)}
                            sx={{ flex: 1 }}
                        />
                        <Box sx={{ p: 2, display: 'flex', justifyContent: 'flex-end' }}>
                            <Button
                                variant="contained"
                                onClick={handleButtonClick}
                                disabled={isBookingDisabled()}
                            >
                                Prenota
                            </Button>
                        </Box>
                    </>
                )}
            </Paper>

            <Snackbar
                open={showError}
                autoHideDuration={6000}
                onClose={() => setShowError(false)}
            >
                <Alert
                    onClose={() => setShowError(false)}
                    severity="error"
                    sx={{ width: '100%' }}
                >
                    {error}
                </Alert>
            </Snackbar>
        </Box>
    );
}

export const Reception: React.FC = () => {
    const [value, setValue] = React.useState(0);

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    return (
        <div>

            <Box sx={{width: '100%', bgcolor: 'background.paper'}}>
                <Tabs
                    value={value}
                    onChange={handleChange}
                    variant="fullWidth"
                >
                    <Tab label="Prenota ricevimento"/>
                    <Tab label="Ricevimenti prenotati"/>
                </Tabs>
                <Divider/>
                {
                    value === 0 ? <BookSlot/> : <BookedSlots/>
                }
            </Box>
        </div>

    );
}
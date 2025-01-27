import React, {useEffect, useState} from "react";
import {DataGrid, GridColDef} from "@mui/x-data-grid";
import {Alert, Autocomplete, Box, Button, Divider, Snackbar, Tab, Tabs, TextField,} from "@mui/material";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {ListBooked} from "../../components/ListBooked.tsx";
import Paper from "@mui/material/Paper";

export type BookingId = {
    parentId: string;
    receptionTimeslotId: number;
};

export type Booking = {
    id: BookingId;
    bookingOrder: number;
    confirmed: boolean;
    refused: boolean;
    date: string;
    hour: number;
    mode: string;
    name: string;
    surname: string;
};

export type ReceptionTimeslotResponse = {
    id: number;
    capacity: number;
    booked: number;
    mode: string;
    hour: number;
    date: string;
    name: string;
    surname: string;
};

type Teacher = {
    id: string;
    name: string;
    surname: string;
};

const BookedSlots: React.FC = () => {
    const [booking, setBooking] = useState<Booking[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<Booking[]> = await axiosConfig.get(
                `${Env.API_BASE_URL}/agenda/reception/timeslot/booked`
            );
            setBooking(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
            setLoading(false); // Ensure loading stops even on error
        }
    };

    return (
        <div style={{display: "flex", justifyContent: "center", marginTop: "1rem"}}>
            {loading ? <div>Loading...</div> : <ListBooked list={booking}/>}
        </div>
    );
};

const BookSlot: React.FC = () => {
    const [teachers, setTeachers] = useState<Teacher[]>([]);
    const [selectedTeacher, setSelectedTeacher] = useState<Teacher | null>(null);
    const [loadingTeachers, setLoadingTeachers] = useState<boolean>(true);
    const [loadingTimeslots, setLoadingTimeslots] = useState<boolean>(false);
    const [selectedSlots, setSelectedSlots] = useState<number[]>([]);
    const [selectedRows, setSelectedRows] = useState<ReceptionTimeslotResponse[]>([]);
    const [rows, setRows] = useState<ReceptionTimeslotResponse[]>([]);
    const [bookedSlots, setBookedSlots] = useState<number[]>([]); // IDs of slots already booked by the user
    const [error, setError] = useState<string>("");
    const [showError, setShowError] = useState(false);
    const [bookingInProgress, setBookingInProgress] = useState<boolean>(false);

    // Fetch the list of teachers and the user's booked slots on component mount
    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                // Fetch teachers
                const teachersResponse: AxiosResponse<Teacher[]> = await axiosConfig.get(
                    `${Env.API_BASE_URL}/parents/teachers`
                );
                setTeachers(teachersResponse.data);
                if (teachersResponse.data.length > 0) {
                    setSelectedTeacher(teachersResponse.data[0]);
                }

                // Fetch booked slots by the user
                const bookedResponse: AxiosResponse<Booking[]> = await axiosConfig.get(
                    `${Env.API_BASE_URL}/agenda/reception/timeslot/booked`
                );
                const bookedSlotIds = bookedResponse.data.map(
                    (booking) => booking.id.receptionTimeslotId
                );
                setBookedSlots(bookedSlotIds);
            } catch (error) {
                setError("Errore nel caricamento dei dati iniziali");
                setShowError(true);
                console.error(error);
            } finally {
                setLoadingTeachers(false);
            }
        };

        fetchInitialData();
    }, []);

    // Fetch timeslots whenever the selected teacher changes
    useEffect(() => {
        const fetchTimeslots = async () => {
            if (selectedTeacher) {
                setLoadingTimeslots(true);
                try {
                    const response: AxiosResponse<ReceptionTimeslotResponse[]> = await axiosConfig.get(
                        `${Env.API_BASE_URL}/agenda/reception/teacher/${selectedTeacher.id}/timeslots`
                    );
                    // Filter out timeslots already booked by the user
                    const availableTimeslots = response.data.filter(
                        (slot) => !bookedSlots.includes(slot.id)
                    );
                    setRows(availableTimeslots);
                } catch (error) {
                    setError("Errore nel caricamento dei timeslot");
                    setShowError(true);
                    console.error(error);
                } finally {
                    setLoadingTimeslots(false);
                }
            }
        };

        fetchTimeslots();
    }, [selectedTeacher, bookedSlots]);

    const handleButtonClick = async () => {
        if (selectedSlots.length === 0) {
            setError("Nessuno slot selezionato.");
            setShowError(true);
            return;
        }

        setBookingInProgress(true);
        let successfulBookings: number[] = [];
        let failedBookings: number[] = [];

        // Iterate over selected slots and attempt to book each
        await Promise.all(
            selectedSlots.map(async (slotId) => {
                try {
                    await axiosConfig.patch(
                        `${Env.API_BASE_URL}/agenda/reception/timeslot/${slotId}/book`
                    );
                    successfulBookings.push(slotId);
                } catch (error) {
                    console.error(`Booking failed for slot ID ${slotId}:`, error);
                    failedBookings.push(slotId);
                }
            })
        );

        // Update the UI based on booking results
        if (successfulBookings.length > 0) {
            // Update bookedSlots state
            setBookedSlots((prev) => [...prev, ...successfulBookings]);

            // Remove successfully booked slots from available timeslots
            setRows((prevRows) =>
                prevRows.filter((row) => !successfulBookings.includes(row.id))
            );

            // Clear selected slots that were successfully booked
            setSelectedSlots((prev) =>
                prev.filter((id) => !successfulBookings.includes(id))
            );
            setSelectedRows((prev) =>
                prev.filter((row) => !successfulBookings.includes(row.id))
            );
        }

        if (failedBookings.length > 0) {
            setError(
                `Impossibile prenotare gli slot con ID: ${failedBookings.join(
                    ", "
                )}. Riprova più tardi.`
            );
            setShowError(true);
        }

        setBookingInProgress(false);
    };

    const onRowsSelectionHandler = (ids: any) => {
        const selectedRowsData = ids
            .map((id: any) => rows.find((row) => row.id === id))
            .filter((row: any): row is ReceptionTimeslotResponse => row !== undefined);
        setSelectedRows(selectedRowsData);
        setSelectedSlots(selectedRowsData.map((row: any) => row.id));
    };

    const isBookingDisabled = () => {
        if (selectedSlots.length === 0 || bookingInProgress) return true;
        // Disable if any selected slot is fully booked
        return selectedRows.some((row) => row.booked >= row.capacity);
    };

    const columns: GridColDef[] = [
        {field: "id", headerName: "ID", width: 90},
        {
            field: "date",
            headerName: "Data",
            type: "string",
            width: 150,
            editable: false,
        },
        {
            field: "hour",
            headerName: "Ora",
            type: "number",
            width: 150,
            editable: false,
        },
        {
            field: "mode",
            headerName: "Modalità",
            type: "string",
            width: 110,
            editable: false,
        },
        {
            field: "capacity",
            headerName: "Capacità",
            type: "number",
            description: "This column has a value getter and is not sortable.",
            sortable: false,
            width: 160,
        },
        {
            field: "booked",
            headerName: "Prenotati",
            type: "number",
            description: "This column has a value getter and is not sortable.",
            sortable: false,
            width: 160,
        },
    ];

    return (
        <Box
            sx={{
                height: "calc(95vh - 112px)",
                display: "flex",
                flexDirection: "column",
                p: 2,
                gap: 2,
            }}
        >
            <Paper elevation={1} sx={{p: 2}}>
                {!loadingTeachers && teachers.length > 0 && (
                    <Autocomplete
                        id="teacher-select"
                        value={selectedTeacher}
                        onChange={(_, v) => {
                            if (v !== null) {
                                setSelectedTeacher(v);
                                // Reset selected slots when teacher changes
                                setSelectedSlots([]);
                                setSelectedRows([]);
                            }
                        }}
                        options={teachers}
                        getOptionLabel={(option) => `${option.name} ${option.surname}`}
                        renderInput={(params) => <TextField {...params} label="Docente"/>}
                    />
                )}
                {loadingTeachers && <div>Caricamento dei docenti...</div>}
            </Paper>

            <Paper
                elevation={1}
                sx={{flex: 1, display: "flex", flexDirection: "column"}}
            >
                {!loadingTimeslots ? (
                    <>
                        <DataGrid
                            rows={rows}
                            columns={columns}
                            getRowId={(row) => row.id}
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
                            checkboxSelection={true} // Reintroduced checkbox selection
                            disableColumnSelector={true}
                            disableMultipleRowSelection={false} // Allow multiple selections
                            localeText={{
                                columnMenuSortAsc: "Ordina Ascendente",
                                columnMenuSortDesc: "Ordina Discendente",
                                columnMenuFilter: "Filtra",
                                columnMenuUnsort: "Annulla ordinamento",
                                columnMenuHideColumn: "Nascondi colonna",
                                footerTotalRows: "Totale righe",
                                noRowsLabel: "Nessun risultato",
                                footerTotalVisibleRows: (count) => `${count.toLocaleString()} righe`,
                                footerRowSelected: (count) =>
                                    `${count.toLocaleString()} ${
                                        count === 1 ? "riga selezionata" : "righe selezionate"
                                    }`,
                                MuiTablePagination: {
                                    labelDisplayedRows: ({from, to, count}) =>
                                        `${from}-${to} di ${count}`,
                                    labelRowsPerPage: "Righe per pagina:",
                                },
                            }}
                            onRowSelectionModelChange={(ids) => onRowsSelectionHandler(ids)}
                            sx={{flex: 1}}
                        />
                        <Box sx={{p: 2, display: "flex", justifyContent: "flex-end"}}>
                            <Button
                                variant="contained"
                                onClick={handleButtonClick}
                                disabled={isBookingDisabled()}
                                // Optionally, you can show a loading state on the button
                            >
                                {bookingInProgress ? "Prenotando..." : "Prenota"}
                            </Button>
                        </Box>
                    </>
                ) : (
                    <div>Caricamento dei timeslot...</div>
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
                    sx={{width: "100%"}}
                >
                    {error}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export const Reception: React.FC = () => {
    const [value, setValue] = React.useState(0);

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    return (
        <div>
            <Box sx={{width: "100%", bgcolor: "background.paper"}}>
                <Tabs value={value} onChange={handleChange} variant="fullWidth">
                    <Tab label="Prenota ricevimento"/>
                    <Tab label="Ricevimenti prenotati"/>
                </Tabs>
                <Divider/>
                {value === 0 ? <BookSlot/> : <BookedSlots/>}
            </Box>
        </div>
    );
};

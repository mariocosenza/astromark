import React, {useEffect, useState} from "react";
import {Box, Divider, Tab, Tabs} from "@mui/material";
import {changeStudentOrYear} from "../../services/StateService.ts";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {ListBooked} from "../../components/ListBooked.tsx";

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

const BookedSlots: React.FC = () => {
    const [toggle, _] = changeStudentOrYear();
    const [booking, setBooking] = useState<Booking[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const responseAbsences: AxiosResponse<Booking[]> = await axiosConfig.get(`${Env.API_BASE_URL}/agenda/reception/timeslot/booked`);
            setBooking(responseAbsences.data);
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
    return (
        <div>
            <h1>BookSlot</h1>
        </div>
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
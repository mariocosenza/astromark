import React, {useState} from "react";
import {Box, Button, TextField, Typography} from "@mui/material";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";

export const TicketCreation: React.FC = () => {
    const [newMessage, setNewMessage] = useState<string>('')

    const handleCreateTicket = async () => {
        try {
            if (newMessage.trim() !== '') {
                await axiosConfig.post(`${Env.API_BASE_URL}/tickets/newTicket`, newMessage, {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                setNewMessage('')
                window.location.reload();
            }
        } catch (e) {
            console.log(e)
        }
    }

    return (
        <Box className={'ticket-creation-box'}>
            <Typography variant='h6'>Crea ticket per la segreteria</Typography>
            <TextField
                className={'textfield-item'} margin={'normal'} fullWidth
                placeholder="Inserisci l'oggetto" value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
            />

            <Button variant='contained' color='primary' fullWidth onClick={handleCreateTicket}>
                Crea
            </Button>
        </Box>
    );
};
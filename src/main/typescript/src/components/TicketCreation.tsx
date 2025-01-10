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
        <Box
            sx={{
                width: '90%',
                position: 'sticky',
                bottom: 0,
                marginTop: '1rem',
                padding: '1rem',
                borderRadius: '8px',
                backgroundColor: '#e3f2fd',
                boxShadow: '0 -2px 4px rgba(0, 0, 0, 0.1)'
            }}
        >
            <Typography variant='h6' sx={{ marginBottom: '1rem' }}>Crea ticket per la segreteria</Typography>
            <TextField
                placeholder="Inserisci l'oggetto"
                variant='outlined'
                fullWidth
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                sx={{ marginBottom: '1rem', backgroundColor: '#fff' }}
            />
            <Button variant='contained' color='primary' fullWidth onClick={handleCreateTicket}>
                Crea
            </Button>
        </Box>
    );
};
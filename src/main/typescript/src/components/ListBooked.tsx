import {Avatar, Box, Button, Stack, Typography} from "@mui/material";
import React from "react";
import {Booking} from "../pages/parents/Reception.tsx";
import {isRole} from "../services/AuthService.ts";
import {Role} from "./route/ProtectedRoute.tsx";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";

export type ListBookedProp = {
    list: Booking[]
}
export const ListBooked: React.FC<ListBookedProp> = ({list}) => {

    const handleClick = async (action: 'refuse' | 'confirm', item: Booking) => {
        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/agenda/reception/timeslot/${action}`, item.id, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            window.location.reload();
        } catch (e) {
            console.log(e)
        }
    }


    return (
        <div>
            {
                list.map((item, i) =>
                    <Box className={'listItem'} width={'70vw'} key={'list' + i}
                         sx={{
                             mb: '0.8rem',
                             display: 'flex',
                             flexDirection: 'row',
                             justifyContent: 'space-between',
                             alignItems: 'flex-start'
                         }}>

                        <div style={{display: 'flex', flexDirection: 'row', alignItems: 'flex-start'}}>
                            <Avatar sx={{
                                bgcolor: (item.refused ? '#C04040' : item.confirmed ? '#408540' : '#405f90'),
                                margin: '1rem'
                            }}>B</Avatar>
                            <div style={{flexDirection: 'column'}}>
                                <Typography variant={'h6'} fontWeight={700}>
                                    {
                                        'Ricevimento del ' + item.date + ' ora ' + item.hour + 'ª'
                                    }
                                </Typography>
                                <Typography variant={'h6'}>
                                    <b>{(isRole(Role.PARENT) ? 'Docente:' : isRole(Role.TEACHER) ? 'Genitore:' : 'Utente:')} </b>
                                    {
                                        item.name + ' ' + item.surname
                                    }
                                </Typography>
                                <Typography variant={'h6'}>
                                    <b>Stato: </b>
                                    {
                                        (item.refused ? 'Rifiutato' : item.confirmed ? 'Confermato' : 'In attesa di approvazione')
                                    }
                                </Typography>
                                <Typography variant={'h6'}>
                                    <b>Modalità: </b>
                                    {
                                        item.mode
                                    }
                                </Typography>
                            </div>
                        </div>
                        {isRole(Role.TEACHER) && (
                            <Stack direction={'row'} spacing={2} alignSelf={'flex-end'} margin={'1rem'}>
                                <Button variant={'contained'} sx={{borderRadius: 3}} color={'error'}
                                        disabled={item.refused || item.confirmed}
                                        onClick={() => handleClick('refuse', item)}>
                                    Rifiuta
                                </Button>
                                <Button variant={'contained'} sx={{borderRadius: 3}}
                                        disabled={item.refused || item.confirmed}
                                        onClick={() => handleClick('confirm', item)}>
                                    Accetta
                                </Button>
                            </Stack>
                        )}
                    </Box>
                )}
        </div>);
}
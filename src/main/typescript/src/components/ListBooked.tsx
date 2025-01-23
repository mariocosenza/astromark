import {Avatar, Box, Typography} from "@mui/material";
import React from "react";
import {Booking} from "../pages/parents/Reception.tsx";


export type ListBookedProp = {
    list: Booking[]
}
export const ListBooked: React.FC<ListBookedProp> = ({list}) => {
    return (
        <div>
            {
                list.map((item, i) =>
                    <Box className={'listItem'} width={'90vw'} sx={{mb: '0.8rem'}} key={'list' + i}>
                        <Avatar sx={{bgcolor: '#405f90', ml: '1%', mt: '0.7%'}}>B</Avatar>
                        <div style={{flexDirection: 'column', marginLeft: '1%'}}>
                            <Typography variant={'h6'} fontWeight={700}>
                                {
                                    'Ricevimento del ' + item.date + ' ora ' + item.hour + 'ª ' + ' Docente: ' + item.name + ' ' + item.surname
                                }
                            </Typography>
                            <Typography variant={'h6'}>
                                <b>Stato: </b>
                                {
                                    (item.refused ? 'Rifiutato' : 'Accettato')
                                }
                            </Typography>
                            <Typography variant={'h6'}>
                                <b>Confermato: </b>
                                {
                                    (item.confirmed ? 'Sì' : 'No')
                                }
                            </Typography>
                            <Typography variant={'h6'}>
                                <b>Modalità: </b>
                                {
                                    item.mode
                                }
                            </Typography>
                        </div>
                    </Box>
                )}
        </div>);
}
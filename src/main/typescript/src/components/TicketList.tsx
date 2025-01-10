import React from "react";
import {Avatar, Box, IconButton, Typography} from "@mui/material";
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import {SelectedTicket} from "../services/TicketService.ts";

export type TicketCard = {
    id: string
    avatar: string
    title: string
    description: string
    hexColor: string
}

export type TicketCards = {
    list: TicketCard[]
}

export const TicketList: React.FC<TicketCards & { onTicketClick: () => void }> = ({ list, onTicketClick }) => {

    const handleTicketClick = (id: string) => {
        SelectedTicket.ticketId = id;
        onTicketClick()
    };

    return (
        <Box sx={{ width: '100%' }}>
            {list.map((item) => (
                <Box
                    className={'listItem'}
                    key={item.id}
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between',
                        margin: '0.5rem 0',
                        padding: '1rem',
                        cursor: 'pointer',
                        transition: "transform 0.2s, box-shadow 0.2s",
                        '&:hover': {
                            transform: "scale(1.02)",
                            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.2)",
                        },
                    }}
                    onClick={() => handleTicketClick(item.id)}
                >
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                        <Avatar sx={{ bgcolor: item.hexColor, marginRight: '1rem' }}>{item.avatar}</Avatar>
                        <Box>
                            <Typography variant='subtitle1' sx={{ fontWeight: 'bold' }}>
                                {item.title}
                            </Typography>
                            <Typography variant='body2' color='textSecondary'>
                                {item.description}
                            </Typography>
                        </Box>
                    </Box>
                    <IconButton>
                        <ArrowForwardIosIcon />
                    </IconButton>
                </Box>
            ))}
        </Box>
    );
}
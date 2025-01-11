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

export type Props = {
    list: TicketCard[]
    onTicketClick: () => void;
}

export const TicketList: React.FC<Props> = ({ list, onTicketClick }) => {

    const handleTicketClick = (id: string) => {
        SelectedTicket.ticketId = id;
        onTicketClick()
    };

    return (
        <Box sx={{ width: '100%' }}>
            {list.map((item) => (
                <Box className={'listItem hover-animation'} justifyContent={'space-between'} margin={'0.5rem 0'} padding={'1rem'}
                     key={item.id}
                     onClick={() => handleTicketClick(item.id)}>

                    <Box display={'flex'} alignItems={'center'}>
                        <Avatar sx={{ bgcolor: item.hexColor, marginRight: '1rem' }}> {item.avatar} </Avatar>
                        <Box>
                            <Typography variant='subtitle1' fontWeight={'bold'}>
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
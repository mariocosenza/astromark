import {Avatar, Box, Button, TextField, Typography} from "@mui/material";
import SendIcon from '@mui/icons-material/Send';
import React, {useState} from "react";

export type MessageComponent = {
    avatar: string;
    text: string;
    hexColor: string
}

export type MessageComponents = {
    list: MessageComponent[]
    send: Function
}

export const ChatComponent: React.FC<MessageComponents> = ({list, send}) => {

    const messageList = list;
    const [newMessage, setNewMessage] = useState<string>('');

    const handleSendMessage = async () => {
        if (newMessage.trim() !== '') {
            const messageSent = await send(newMessage);
            if (messageSent !== undefined){
                messageList.push(messageSent)
                setNewMessage('');
            }
        }
    }

    return (
        <Box
            className={'surface-container'}
            sx={{
                width: '100%',
                marginTop: '1rem',
                padding: '1rem',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                height: '100%'
            }}
        >

            <Box sx={{flexGrow: 1, overflowY: 'auto', marginBottom: '1rem'}}>
                {messageList.map((message, i) => (
                    <Box display={'flex'} alignItems={'center'} key={'list' + i}
                         sx={{backgroundColor: '#fff', padding: '0.5rem', borderRadius: '8px', marginBottom: '0.5rem'}}>
                        <Avatar sx={{bgcolor: message.hexColor, marginRight: '1rem'}}>{message.avatar}</Avatar>
                        <Typography variant='body2' color='textPrimary'>{message.text}</Typography>
                    </Box>
                ))}
            </Box>

            <Box sx={{display: 'flex', alignItems: 'center'}}>
                <TextField
                    placeholder='Scrivi il tuo messaggio'
                    variant='outlined'
                    fullWidth
                    size='small'
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    sx={{backgroundColor: '#fff', borderRadius: '8px'}}
                />

                <Button variant='contained' color='primary' sx={{marginLeft: '0.5rem'}} onClick={handleSendMessage}>
                    <SendIcon/>
                </Button>
            </Box>
        </Box>
    );
};
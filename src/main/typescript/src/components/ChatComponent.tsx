import {Avatar, Box, Button, TextField, Typography} from "@mui/material";
import SendIcon from '@mui/icons-material/Send';
import React, {useState} from "react";

export type MessageComponent = {
    avatar: string;
    text: string;
    hexColor: string
}

export type Props = {
    list: MessageComponent[]
    send: Function
}

export const ChatComponent: React.FC<Props> = ({list, send}) => {

    const messageList = list;
    const [newMessage, setNewMessage] = useState<string>('');

    const handleSendMessage = async () => {
        if (newMessage.trim() !== '') {
            const messageSent = await send(newMessage);
            if (messageSent !== undefined) {
                messageList.push(messageSent)
                setNewMessage('');
            }
        }
    }

    return (
        <Box className={'surface-container chat-container'}>

            <Box>
                {messageList.map((message, i) => (
                    <Box className={'message-item'} display={'flex'} alignItems={'center'} key={'list' + i}>
                        <Avatar sx={{bgcolor: message.hexColor, marginRight: '1rem'}}> {message.avatar} </Avatar>
                        <Typography variant='body2' color='textPrimary'> {message.text} </Typography>
                    </Box>
                ))}
            </Box>

            <Box display={'flex'} alignItems={'center'}>
                <TextField
                    className={'textfield-item'} margin={'normal'} size={'small'} fullWidth
                    placeholder='Scrivi il tuo messaggio' value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") {
                            e.preventDefault();
                            handleSendMessage();
                        }
                    }}
                />

                <Button variant='contained' color='primary' sx={{marginLeft: '0.5rem', marginTop: '0.4rem'}}
                        onClick={handleSendMessage}>
                    <SendIcon/>
                </Button>
            </Box>
        </Box>
    );
};
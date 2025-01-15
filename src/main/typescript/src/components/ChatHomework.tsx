import React, { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import axios from "axios";
import { Env } from "../Env";
import { getToken } from "../services/AuthService";
import { Box, Avatar, Typography, TextField, Button } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";

// Define interface for individual message
interface MessageResponse {
    id?: string;
    senderName: string;
    text: string;
    timestamp?: string;
}

// Define interface for the response sent via the topic
interface HomeworkChatResponse {
    id: string;
    title: string;
    studentId: string;
    completed: boolean;
    messages: MessageResponse[];
}

interface ChatHomeworkProps {
    chatId: string; // typically a UUID string
}

// Function to determine avatar color based on the sender's name
const getAvatarColor = (name: string): string => {
    const colors = ["#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3f51b5", "#2196f3", "#03a9f4", "#00bcd4", "#009688", "#4caf50", "#8bc34a", "#cddc39", "#ffeb3b", "#ffc107", "#ff9800", "#ff5722"];
    const index = name.charCodeAt(0) % colors.length;
    return colors[index];
};

export const ChatHomeworkComponent: React.FC<ChatHomeworkProps> = ({ chatId }) => {
    const [messages, setMessages] = useState<MessageResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [newMessage, setNewMessage] = useState<string>("");
    const stompClientRef = useRef<Client | null>(null);
    const API_BASE_URL = Env.API_BASE_URL; // e.g., "http://localhost:8080"

    const fetchMessages = async () => {
        try {
            const response = await axios.get<any>(
                `${API_BASE_URL}/homeworks/${chatId}/messages`,
                {
                    headers: { Authorization: `Bearer ${getToken()}` },
                }
            );
            let messagesArray: MessageResponse[] = [];
            if (Array.isArray(response.data)) {
                messagesArray = response.data;
            } else if (response.data && Array.isArray(response.data.messages)) {
                messagesArray = response.data.messages;
            } else {
                console.error("The response does not contain an array of messages", response.data);
            }
            setMessages(messagesArray);
        } catch (error) {
            console.error("Error fetching messages:", error);
            setMessages([]);
        } finally {
            setLoading(false);
        }
    };

    const connectWebSocket = () => {
        const stompClient = new Client({
            webSocketFactory: () => new SockJS(`${Env.BASE_URL}/ws`),
            connectHeaders: { Authorization: `Bearer ${getToken()}` },
            debug: (str) => console.log(str),
            reconnectDelay: 5000,
        });

        stompClient.onConnect = () => {
            console.log("Connected to WebSocket");
            stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
                if (message.body) {
                    try {
                        const receivedResponse: HomeworkChatResponse = JSON.parse(message.body);
                        setMessages(receivedResponse.messages);
                    } catch (error) {
                        console.error("Error parsing the message:", error);
                    }
                }
            });
        };

        stompClient.onStompError = (frame) => {
            console.error("STOMP error:", frame);
        };

        stompClient.activate();
        stompClientRef.current = stompClient;
    };

    const sendMessage = async (text: string) => {
        const messagePayload = {
            chatId,
            text,
        };

        try {
            await axios.post(
                `${API_BASE_URL}/homeworks/${chatId}/addMessage`,
                JSON.stringify(text),
                {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${getToken()}`,
                    },
                }
            );

            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.publish({
                    destination: `/app/homeworks/chats/${chatId}`,
                    body: JSON.stringify(messagePayload),
                    headers: { Authorization: `Bearer ${getToken()}` },
                });
            } else {
                console.error("STOMP client is not connected");
            }
        } catch (error) {
            console.error("Error sending the message via REST:", error);
        }
        setNewMessage("");
    };

    useEffect(() => {
        fetchMessages();
        connectWebSocket();
        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
                stompClientRef.current = null;
            }
        };
    }, [chatId]);

    return (
        <Box className={'surface-container chat-container'} style={{ maxWidth: '600px', margin: 'auto', padding: '1rem', border: '1px solid #ccc' }}>
            <Typography variant="h6" component="h2">Chat {chatId}</Typography>

            <Box style={{ marginTop: '1rem' }}>
                {loading ? (
                    <Typography variant='body1'>Loading messages...</Typography>
                ) : (
                    <>
                        {messages && messages.length === 0 ? (
                            <Typography variant='body2'>No messages yet</Typography>
                        ) : (
                            <Box>
                                {messages.map((msg, index) => (
                                    <Box className={'message-item'} display={'flex'} alignItems={'center'} key={'list' + index} style={{ marginBottom: '0.75rem' }}>
                                        <Avatar sx={{ bgcolor: getAvatarColor(msg.senderName), marginRight: '1rem' }}> {msg.senderName.charAt(0)} </Avatar>
                                        <Box>
                                            <Typography variant='body2' color='textPrimary'>
                                                <strong>{msg.senderName}:</strong> {msg.text}
                                            </Typography>
                                            {msg.timestamp && (
                                                <Typography variant='caption' color='textSecondary' style={{ marginLeft: '0.5rem', fontSize: '0.8rem' }}>
                                                    {new Date(msg.timestamp).toLocaleString()}
                                                </Typography>
                                            )}
                                        </Box>
                                    </Box>
                                ))}
                            </Box>
                        )}
                    </>
                )}
            </Box>

            <Box display={'flex'} alignItems={'center'} style={{ marginTop: '1rem' }}>
                <TextField
                    className={'textfield-item'}
                    margin={'normal'}
                    size={'small'}
                    fullWidth
                    placeholder='Scrivi il tuo messaggio'
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                />

                <Button
                    variant='contained'
                    color='primary'
                    sx={{ marginLeft: '0.5rem', marginTop: '0.4rem' }}
                    onClick={() => sendMessage(newMessage)}
                >
                    <SendIcon />
                </Button>
            </Box>
        </Box>
    );
};

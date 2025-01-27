import React, {ChangeEvent, useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {AxiosResponse} from "axios";
import {Env} from "../Env";
import {getRole, getToken, isRole} from "../services/AuthService";
import {Alert, Avatar, Box, Button, IconButton, Snackbar, TextField, Typography} from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import AttachFileIcon from "@mui/icons-material/AttachFile"; // Attachment icon
import DownloadIcon from "@mui/icons-material/Download"; // Download icon
import AddTaskIcon from '@mui/icons-material/AddTask';
import {Role} from "./route/ProtectedRoute.tsx";
import axiosConfig from "../services/AxiosConfig.ts";

/**
 * Interface for an individual message.
 */
interface MessageResponse {
    id?: string;
    senderName: string;
    text: string;
    timestamp?: string;
    attachment?: string;
}

/**
 * Interface for the chat response sent via the topic.
 */
interface HomeworkChatResponse {
    id: string;
    title: string;
    studentId: string;
    completed: boolean;
    messages: MessageResponse[];
}

interface ChatHomeworkProps {
    homeworkId: number;
    studentId: string | null;
}

/**
 * Returns a background color based on the sender's name.
 *
 * @param name - The sender's name.
 * @returns A string representing the color.
 */
const getAvatarColor = (name: string): string => {
    const colors = [
        "#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3f51b5", "#2196f3",
        "#03a9f4", "#00bcd4", "#009688", "#4caf50", "#8bc34a", "#cddc39",
        "#ffeb3b", "#ffc107", "#ff9800", "#ff5722"
    ];
    const index = name.charCodeAt(0) % colors.length;
    return colors[index];
};

/**
 * ChatHomeworkComponent - a component that displays the chat for a homework.
 *
 * It fetches messages once the chatId is available, maintains a websocket connection,
 * and scrolls the message box to the bottom when new messages arrive.
 *
 * @param homeworkId - The id of the homework.
 * @param studentId - The id of the student.
 * @returns The ChatHomeworkComponent.
 */
export const ChatHomeworkComponent: React.FC<ChatHomeworkProps> = ({homeworkId, studentId}) => {
    const [messages, setMessages] = useState<MessageResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [isCompleted, setCompleted] = useState<boolean>(false);
    const [newMessage, setNewMessage] = useState<string>("");
    const [chatId, setChatId] = useState<string>("");
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const stompClientRef = useRef<Client | null>(null);
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const messagesContainerRef = useRef<HTMLDivElement | null>(null);
    const [error, setError] = useState<string>("");
    const [showError, setShowError] = useState(false);
    const API_BASE_URL = Env.API_BASE_URL; // e.g. "http://localhost:8080"

    /**
     * Fetches messages. If the user is a student and chatId is not yet set,
     * it requests an uncompleted chat first.
     */
    const fetchMessages = async () => {
        try {
            // If student or teacher, and chatId is not set, get the chatId.
            if (getRole().toUpperCase() === Role.STUDENT && !chatId) {
                const response: AxiosResponse<string> = await axiosConfig.get<string>(
                    `${API_BASE_URL}/homeworks/${homeworkId}/has-uncompleted-chat`
                );
                setChatId(response.data);
            } else if (getRole().toUpperCase() === Role.TEACHER && !chatId) {
                if (studentId){
                    const response: AxiosResponse<string> = await axiosConfig.get<string>(
                        `${API_BASE_URL}/homeworks/${homeworkId}/student/${studentId}`
                    );
                    setChatId(response.data);
                }
            }

            // Proceed with the call only if chatId is set.
            if (chatId) {
                const response = await axiosConfig.get<any>(
                    `${API_BASE_URL}/homeworks/${chatId}/messages`
                );
                let messagesArray: MessageResponse[] = [];
                if (Array.isArray(response.data)) {
                    messagesArray = response.data;
                } else if (response.data && Array.isArray(response.data.messages)) {
                    messagesArray = response.data.messages;
                } else {
                    console.error("The response does not contain an array of messages", response.data);
                }

                // call to check if it's completed
                const completedResponse = await axiosConfig.get<boolean>(`${API_BASE_URL}/homeworks/${chatId}/isCompleted`);
                setCompleted(completedResponse.data)

                setMessages(messagesArray);
            }
        } catch (error) {
            console.error("Error fetching messages:", error);
            setMessages([]);
        } finally {
            setLoading(false);
        }
    };

    /**
     * Connects to the websocket and subscribes to the chat topic.
     */
    const connectWebSocket = () => {
        const stompClient = new Client({
            webSocketFactory: () => new SockJS(`${Env.BASE_URL}/ws`),
            connectHeaders: {Authorization: `Bearer ${getToken()}`},
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

    /**
     * Sends a new message. If a file is selected, it uploads the file in a multipart/form-data request.
     *
     * @param text - The text of the message.
     */
    const sendMessage = async (text: string) => {
        // Check that the message is not empty
        if (!text.trim()) {
            console.warn("The message is empty. No request sent.");
            return;
        }

        const messagePayload = {
            chatId,
            text,
        };

        try {
            // Send the text message.
            const response = await axiosConfig.post(
                `${API_BASE_URL}/homeworks/${chatId}/addMessage`,
                JSON.stringify(text)
            );

            // If a file is selected, proceed with the file upload.
            if (selectedFile) {
                // Check file size (16MB = 16 * 1024 * 1024 bytes)
                const maxSize = 16 * 1024 * 1024; // 16MB in bytes

                if (selectedFile.size > maxSize) {
                    setError("Il file è troppo grande. La dimensione massima consentita è 16MB.");
                    setShowError(true);
                    return;
                }

                try {
                    const formData = new FormData();
                    formData.append("file", selectedFile);

                    await axiosConfig.post(
                        `${API_BASE_URL}/chats/upload/${response.data}`,
                        formData,
                        {
                            headers: {
                                "Content-Type": "multipart/form-data",
                                Authorization: `Bearer ${getToken()}`
                            }
                        }
                    );
                } catch (error) {
                    console.error("Error uploading the file:", error);
                    setError("Si è verificato un errore durante l'upload del file. Riprova più tardi.");
                    setShowError(true);
                }
            }
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.publish({
                    destination: `/app/homeworks/chats/${chatId}`,
                    body: JSON.stringify(messagePayload),
                    headers: {Authorization: `Bearer ${getToken()}`},
                });
            } else {
                console.error("STOMP client is not connected");
            }
        } catch (error) {
            console.error("Error sending the message via REST:", error);
        }

        // Reset the fields
        setNewMessage("");
        setSelectedFile(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = "";
        }
    };

    /**
     * Marks the chat as completed.
     */
    const handleCompleted = async () => {
        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/homeworks/complete`, chatId, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            setCompleted(true)
        } catch (e) {
            console.log(e)
        }
    }

    /**
     * Handles changes on the file input.
     *
     * @param e - The file input change event.
     */
    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setSelectedFile(e.target.files[0]);
        }
    };

    /**
     * Triggers a click on the hidden file input.
     */
    const handleAttachmentClick = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    /**
     * Opens the download link in a new window.
     *
     * @param attachment - The attachment URL.
     */
    const handleDownload = (attachment: string) => {
        window.open(attachment, "_blank");
    };

    // Scroll the messages container to the bottom when new messages arrive.
    useEffect(() => {
        if (messagesContainerRef.current) {
            messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
        }
    }, [messages]);

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
        <Box
            className={"surface-container chat-container"}
            style={{
                marginLeft: "2vw",
                maxWidth: "50vw",
                minHeight: "80vh",
                maxHeight: "80vh",
                margin: "auto",
                padding: "1rem",
                border: "1px solid #ccc"
            }}
        >
            <Typography variant="h6" component="h2">
                Chat
            </Typography>

            <Box
                ref={messagesContainerRef}
                style={{
                    marginTop: "1rem",
                    overflowY: "auto",
                    paddingRight: "0.5rem",
                    scrollbarWidth: "thin"
                }}
            >
                {loading ? (
                    <Typography variant="body1">Loading messages...</Typography>
                ) : (
                    <>
                        {messages && messages.length === 0 ? (
                            <Typography variant="body2">No messages yet</Typography>
                        ) : (
                            <Box>
                                {messages.map((msg, index) => (
                                    <Box
                                        className={"message-item"}
                                        display={"flex"}
                                        alignItems={"center"}
                                        key={"list" + index}
                                        style={{marginBottom: "0.75rem"}}
                                    >
                                        <Avatar sx={{bgcolor: getAvatarColor(msg.senderName), marginRight: "1rem"}}>
                                            {msg.senderName.charAt(0)}
                                        </Avatar>
                                        <Box>
                                            <Typography variant="body2" color="textPrimary">
                                                <strong>{msg.senderName}:</strong> {msg.text}
                                            </Typography>
                                            {msg.timestamp && (
                                                <Typography
                                                    variant="caption"
                                                    color="textSecondary"
                                                    style={{marginLeft: "0.5rem", fontSize: "0.8rem"}}
                                                >
                                                    {new Date(msg.timestamp).toLocaleString()}
                                                </Typography>
                                            )}
                                        </Box>
                                        {/* If the message contains an attachment, show the download button */}
                                        {msg.attachment && (
                                            <IconButton
                                                color="primary"
                                                onClick={() => handleDownload('https://api.astromark.it/' + msg.attachment!)}
                                                title="Download attachment"
                                            >
                                                <DownloadIcon/>
                                            </IconButton>
                                        )}
                                    </Box>
                                ))}
                            </Box>
                        )}
                    </>
                )}
            </Box>

            <Box display={"flex"} alignItems={"center"} style={{marginTop: "1rem"}}>
                {isRole(Role.TEACHER) &&
                    <IconButton
                        onClick={() => handleCompleted()}
                        disabled={isCompleted}
                        sx={{marginRight: "0.5rem"}}
                        title="Complete chat"
                    >
                        <AddTaskIcon color={isCompleted ? 'success' : 'disabled'}/>
                    </IconButton>
                }

                <TextField
                    className={"textfield-item"}
                    margin={"normal"}
                    size={"small"}
                    fullWidth
                    placeholder="Scrivi il tuo messaggio"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") {
                            e.preventDefault();
                            sendMessage(newMessage);
                        }
                    }}
                />

                {/* Attachment button (to the right, before the send button) */}
                <IconButton
                    onClick={handleAttachmentClick}
                    sx={{marginLeft: "0.5rem"}}
                    // If a file is selected, change color (e.g., "secondary")
                    color={selectedFile ? "secondary" : "primary"}
                    title="Add attachment"
                >
                    <AttachFileIcon/>
                </IconButton>

                <Button
                    variant="contained"
                    color="primary"
                    sx={{marginLeft: "0.5rem", marginTop: "0.4rem"}}
                    onClick={() => sendMessage(newMessage)}
                >
                    <SendIcon/>
                </Button>

                {/* Hidden file input */}
                <input
                    type="file"
                    accept={".pdf,.txt,.epub,.csv,.png,.jpg,.jpeg,.doc,.docx,.ppt,.pptx,.xls,.xlsx"}
                    style={{display: "none"}}
                    ref={fileInputRef}
                    onChange={handleFileChange}
                />
                <Snackbar
                    open={showError}
                    autoHideDuration={6000}
                    onClose={() => setShowError(false)}
                >
                    <Alert
                        onClose={() => setShowError(false)}
                        severity="error"
                        sx={{width: '100%'}}
                    >
                        {error}
                    </Alert>
                </Snackbar>
            </Box>
        </Box>
    );
};

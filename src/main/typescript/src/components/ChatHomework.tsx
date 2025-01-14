import React, { useEffect, useState, useRef, FormEvent } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import axios from "axios";
import { Env } from "../Env";
import { getToken } from "../services/AuthService";

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

export const ChatHomeworkComponent: React.FC<ChatHomeworkProps> = ({ chatId }) => {
    const [messages, setMessages] = useState<MessageResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [newMessage, setNewMessage] = useState<string>("");
    const stompClientRef = useRef<Client | null>(null);
    const API_BASE_URL = Env.API_BASE_URL; // e.g., "http://localhost:8080"

    // Fetch existing messages using a REST call
    // (Assuming the REST endpoint returns an array of messages or a HomeworkChatResponse)
    const fetchMessages = async () => {
        try {
            const response = await axios.get<any>(
                `${API_BASE_URL}/homeworks/${chatId}/messages`,
                {
                    headers: { Authorization: `Bearer ${getToken()}` },
                }
            );
            let messagesArray: MessageResponse[] = [];
            // Here we check if the response is an array of messages...
            if (Array.isArray(response.data)) {
                messagesArray = response.data;
            }
            // ...or if it is an object containing the messages array in the "messages" property.
            else if (response.data && Array.isArray(response.data.messages)) {
                messagesArray = response.data.messages;
            } else {
                console.error("The response does not contain an array of messages", response.data);
            }
            setMessages(messagesArray);
        } catch (error) {
            console.error("Error fetching messages:", error);
            setMessages([]); // fallback to empty array in case of error
        } finally {
            setLoading(false);
        }
    };

    // Connect to the WebSocket (endpoint: /ws) and subscribe to the topic
    const connectWebSocket = () => {
        const stompClient = new Client({
            webSocketFactory: () => new SockJS(`${Env.BASE_URL}/ws`),
            connectHeaders: { Authorization: `Bearer ${getToken()}` },
            debug: (str) => console.log(str),
            reconnectDelay: 5000,
        });

        stompClient.onConnect = () => {
            console.log("Connected to WebSocket");
            // Subscribe to the specific chat topic (using the path defined on the server)
            stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
                if (message.body) {
                    try {
                        // Parse the response as HomeworkChatResponse
                        const receivedResponse: HomeworkChatResponse = JSON.parse(message.body);
                        // Update state exclusively from the topic broadcast (use the messages array from the response)
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

    // Send a new message:
    // 1. Via REST POST to "/api/homeworks/{chatId}/addMessage"
    // 2. Via WebSocket to destination "/app/homeworks/chats/{chatId}"
    // The message will be displayed only when it is received via the topic.
    const sendMessage = async (text: string) => {
        // Prepare the payload for WebSocket. The server expects this payload to determine the chat.
        const messagePayload = {
            chatId,
            text,
        };

        try {
            // Send the message via REST to register it definitively
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

            // Publish the message via WebSocket if connected
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
        // Clear the input field. The new message will be displayed when the topic sends the updated HomeworkChatResponse.
        setNewMessage("");
    };

    // Handle the form submission
    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (newMessage.trim() !== "") {
            sendMessage(newMessage.trim());
        }
    };

    useEffect(() => {
        fetchMessages();
        connectWebSocket();
        // Cleanup: deactivate the WS client when chatId changes or on component unmount
        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
                stompClientRef.current = null;
            }
        };
    }, [chatId]);

    return (
        <div style={{ border: "1px solid #ccc", padding: "1rem", maxWidth: "600px", margin: "auto" }}>
            <h2>Chat {chatId}</h2>
            {loading ? (
                <p>Loading messages...</p>
            ) : (
                <>
                    {messages && messages.length === 0 ? (
                        <p>No messages yet</p>
                    ) : (
                        <ul style={{ listStyle: "none", padding: 0 }}>
                            {messages.map((msg, index) => (
                                <li key={index} style={{ marginBottom: "0.75rem" }}>
                                    <strong>{msg.senderName}: </strong>
                                    <span>{msg.text}</span>
                                    {msg.timestamp && (
                                        <em style={{ marginLeft: "0.5rem", fontSize: "0.8rem" }}>
                                            {new Date(msg.timestamp).toLocaleString()}
                                        </em>
                                    )}
                                </li>
                            ))}
                        </ul>
                    )}
                </>
            )}
            <form onSubmit={handleSubmit} style={{ marginTop: "1rem" }}>
                <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Write a new message..."
                    style={{ width: "80%", padding: "0.5rem", fontSize: "1rem" }}
                />
                <button type="submit" style={{ padding: "0.5rem 1rem", marginLeft: "0.5rem", fontSize: "1rem" }}>
                    Send
                </button>
            </form>
        </div>
    );
};

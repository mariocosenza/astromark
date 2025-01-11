import React, {useEffect, useState} from "react";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {blue, lightBlue, orange} from "@mui/material/colors";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import {Box, CircularProgress, Stack} from "@mui/material";
import {Env} from "../../Env.ts";
import {TicketResponse} from "../../entities/TicketResponse.ts";
import {TicketCard, TicketList} from "../../components/TicketList.tsx";
import {ChatComponent, MessageComponent} from "../../components/ChatComponent.tsx";
import {TicketCreation} from "../../components/TicketCreation.tsx";
import {MessageResponse} from "../../entities/MessageResponse.ts";
import {SelectedTicket} from "../../services/TicketService.ts";
import {isRole} from "../../services/AuthService.ts";
import {Role} from "../../components/route/ProtectedRoute.tsx";

export const TeacherTicket: React.FC = () => {
    const [ticketData, setTicketData] = useState<TicketCard[]>([]);
    const [messageData, setMessageData] = useState<MessageComponent[]>([]);
    const [letterName, setLetterName] = useState<string>('')
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try{
            let fetchedTicket: TicketCard[] = [];
            const ticketResponse: AxiosResponse<TicketResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/tickets/ticket`);
            if (ticketResponse.data.length){
                fetchedTicket = ticketResponse.data.map((ticket: TicketResponse) => ({
                    avatar: 'T',
                    title: "Ticket del " + ticket.datetime.toString().substring(0, 10),
                    description: ticket.title,
                    hexColor: lightBlue[500],
                    id: ticket.id
                }));

                if (SelectedTicket.ticketId === null){
                    SelectedTicket.ticketId = ticketResponse.data[ticketResponse.data.length -1].id
                }

                await fetchMessages()
            }

            setTicketData(fetchedTicket);
            setLoading(false);
        }catch (error){
            console.log(error)
        }
    }

    const fetchMessages = async () => {
        try{
            let fetchedMessages: MessageComponent[]  = [];
            const messageResponse: AxiosResponse<MessageResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/tickets/${SelectedTicket.ticketId}/messages`);
            if (messageResponse.data.length){
                fetchedMessages = messageResponse.data.map((message: MessageResponse) => ({
                    avatar: message.senderName.charAt(0),
                    text: message.text,
                    hexColor: message.isSecretary ? orange[500] : blue[500],
                }));
                setLetterName(messageResponse.data[0].senderName.charAt(0))
            }

            setMessageData(fetchedMessages)
        }catch (error){
            console.log(error)
        }
    }

    const sendMessage = async (textMessage: string) => {
        try {
            await axiosConfig.post(`${Env.API_BASE_URL}/tickets/${SelectedTicket.ticketId}/addMessage`, textMessage, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            return {
                avatar: letterName,
                text: textMessage,
                hexColor: isRole(Role.SECRETARY) ? orange[500] : blue[500],
            };

        } catch (error) {
            console.log(error);
        }
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Stack spacing={2} direction={{ xs: 'column', md: 'row' }} justifyContent="center" marginTop={'2vh'}>

                <Box justifyContent="center" width={{xs: '100%', md: '45%'}}>
                    <Box padding={'1rem'}>
                        {loading ? <CircularProgress /> : <TicketList list={ticketData} onTicketClick={fetchMessages} />}
                    </Box>
                    <Box padding={'1rem'}>
                        <TicketCreation />
                    </Box>
                </Box>

                <Box width={{xs: '100%', md: '45%'}}>
                    <ChatComponent list={messageData} send={sendMessage}/>
                </Box>
            </Stack>
        </div>
    );
}
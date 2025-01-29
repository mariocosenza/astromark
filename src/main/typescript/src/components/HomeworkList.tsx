import {Avatar, Box, Button, Typography} from "@mui/material";
import React from "react";
import {homeworkChatId, openChat} from "../pages/./studentsParents/ClassActivity.tsx";
import {getRole} from "../services/AuthService.ts";
import {Role} from "./route/ProtectedRoute.tsx";

export type HomeworkItemProp = {
    id: number
    avatar: string
    title: string
    description: string
    hexColor: string
    chat: boolean
    date: Date
}

export type HomeworkProp = {
    list: HomeworkItemProp[]
    dashboard: boolean
}

export const HomeworkList: React.FC<HomeworkProp> = ({list, dashboard}) => {
    const [open, setOpen] = openChat()
    const [homewokId, setChatId] = homeworkChatId()
    console.log(list)
    return (
        <div>
            {
                list.map((item, i) =>
                    <Box className={'listItem'} minWidth={open ? '43vw' : '85vw'} flex={'auto'} sx={{mb: '0.5rem'}}
                         key={i}>
                        <Avatar sx={{bgcolor: item.hexColor, ml: '1%', mt: '0.7%'}}>{item.avatar}</Avatar>
                        <div style={{flexDirection: 'column', marginLeft: '1%'}}>
                            <Typography variant={'h6'}>
                                {
                                    item.title + ' per ' + new Date(item.date).toLocaleDateString()
                                }
                            </Typography>
                            <Typography variant={'h6'}>
                                {
                                    item.description
                                }
                                {
                                    getRole().toUpperCase() == Role.STUDENT && item.chat && !dashboard &&
                                    <Button style={{alignContent: 'end'}} onClick={() => {
                                        setOpen(!open)
                                        open ? setChatId(-1) : setChatId(item.id)
                                    }}>{homewokId === item.id ? 'Chiudi Chat' : 'Apri Chat'}</Button>
                                }
                            </Typography>
                        </div>
                    </Box>
                )}
        </div>);
}
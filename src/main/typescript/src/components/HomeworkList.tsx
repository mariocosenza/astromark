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
}

export const HomeworkList: React.FC<HomeworkProp> = ({list}) => {
    const [open, setOpen] = openChat()
    const [homewokId, setChatId] = homeworkChatId()
    return (
        <div>
            {
                list.map((item, i) =>
                    <Box className={'listItem'} minWidth={open ? '43vw' : '90vw'} flex={'auto'} sx={{mb: '0.5rem'}}
                         key={i}>
                        <Avatar sx={{bgcolor: item.hexColor, ml: '1%', mt: '0.7%'}}>{item.avatar}</Avatar>
                        <div style={{flexDirection: 'column', marginLeft: '1%'}}>
                            <Typography variant={'h6'}>
                                {
                                    item.title + ' per ' + item.date
                                }
                            </Typography>
                            <Typography variant={'h6'}>
                                {
                                    item.description
                                }
                                {
                                    getRole().toUpperCase() == Role.STUDENT && item.chat &&
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
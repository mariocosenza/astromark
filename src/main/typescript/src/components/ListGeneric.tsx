import {Avatar, Box, Typography} from "@mui/material";
import React from "react";

export type ListItemProp = {
    avatar: string
    title: string
    description: string
    hexColor: string
    date: Date
}

export type ListProp = {
    list: ListItemProp[]
}
export const ListGeneric: React.FC<ListProp> = ({list}) => {
    return (
        <div>
            {
                list.map((item, i) =>
                    <Box className={'listItem'} width={'90vw'} sx={{mb: '0.5rem'}} key={'list' + i}>
                        <Avatar
                            sx={{bgcolor: item.hexColor, ml: '0.8%', mt: '0.4%', p: '0.2rem'}}>{item.avatar}</Avatar>
                        <div style={{flexDirection: 'column', marginLeft: '1%'}}>
                            <Typography variant={'h6'}>
                                {
                                    item.title
                                }
                            </Typography>
                            <Typography variant={'h6'}>
                                {
                                    item.description
                                }
                            </Typography>
                        </div>
                    </Box>
                )}
        </div>);
}
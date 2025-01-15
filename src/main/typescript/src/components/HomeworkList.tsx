import {Avatar, Box, Typography} from "@mui/material";
import React from "react";
import {ListProp} from "./ListGeneric.tsx";


export const HomeworkList: React.FC<ListProp> = ({list}) => {
    return (
        <div>
            {
                list.map((item, i) =>
                    <Box className={'listItem'} flex={'auto'} sx={{mb: '0.5rem'}} key={'list' + i}>
                        <Avatar sx={{ bgcolor: item.hexColor, ml: '1%', mt: '0.7%'}}>{item.avatar}</Avatar>
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
                            </Typography>
                        </div>
                    </Box>
                )}
        </div>);
}
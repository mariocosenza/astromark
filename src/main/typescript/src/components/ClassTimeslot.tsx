import {Box, Stack, Typography} from "@mui/material";
import Person2OutlinedIcon from '@mui/icons-material/Person2Outlined';
import React from "react";

export type ClassTimeslotProps = {
    hour: number;
    title: string | any;
    date: Date | any
}


export const ClassTimeslot: React.FC<ClassTimeslotProps> = ({hour, title}) => {
    return (
        <div className={'classTimeslot'}>
            {
                <Box height={'5rem'}>
                    {title !== null ? <Stack direction={"row"} spacing={2}>
                        <Person2OutlinedIcon/>
                        <Stack direction={"column"} spacing={1}>
                            <Typography variant={'h6'}>{title}</Typography>
                            <Typography variant={'body1'}>Ora {hour}</Typography>
                        </Stack>
                    </Stack> : <Stack direction={"row"} spacing={2}>
                        <Person2OutlinedIcon/>
                        <Stack direction={"column"} spacing={1}>
                            <Typography variant={'h6'}>Nessuna materia</Typography>
                            <Typography variant={'body1'}>Ora {hour}</Typography>
                        </Stack>
                    </Stack>}
                </Box>
            }
        </div>
    );
}
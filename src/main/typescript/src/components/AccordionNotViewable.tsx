import {Accordion, AccordionDetails, AccordionSummary, Avatar, Typography} from "@mui/material";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import React from "react";

export type AccordionNotViewableProps = {
    avatar: string
    id: number,
    title: string,
    date: Date,
    description: string
}

export const AccordionNotViewable : React.FC<AccordionNotViewableProps> = (props : AccordionNotViewableProps) => {
    return (
        <Accordion className={'accordion'}>
        <AccordionSummary
            style={{minWidth: '90vw'}}
            expandIcon={<ArrowDropDownIcon />}
            aria-controls="panel2-content"
            id="panel2-header"
        >
            <Avatar sx={{ bgcolor: '#df3466'}}>{props.avatar}</Avatar>
            <Typography variant={'h6'} sx={{ml: '1vw'}} component="span">{props.title} del {props.date.toString()} </Typography>
        </AccordionSummary>
        <AccordionDetails>
            <Typography>
                {
                    props.description
                }
            </Typography>
        </AccordionDetails>
    </Accordion>);
}
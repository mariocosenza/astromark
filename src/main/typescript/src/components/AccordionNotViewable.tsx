import {Accordion, AccordionDetails, AccordionSummary, Avatar, Typography} from "@mui/material";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import React from "react";
import {useMedia} from "react-use";

export type AccordionNotViewableProps = {
    avatar: string
    id: number,
    title: string,
    date: Date,
    description: string
}

export const AccordionNotViewable: React.FC<AccordionNotViewableProps> = (props: AccordionNotViewableProps) => {
    const isMobile = useMedia('(max-width: 1366px)');
    return (
        <Accordion className={'accordion'}>
            <AccordionSummary
                style={{minWidth: isMobile ? '20vw' : '90vw'}}
                expandIcon={<ArrowDropDownIcon/>}
                aria-controls="panel2-content"
                id="panel2-header"
            >
                <Avatar sx={{bgcolor: '#df3466'}}>{props.avatar}</Avatar>
                <Typography variant={'h6'} sx={{ml: '1vw', whiteSpace: 'pre-wrap'}}
                            component={isMobile ? 'h6' : "span"}>
                    {props.title} del {new Date(props.date).toLocaleDateString()}
                </Typography>
            </AccordionSummary>
            <AccordionDetails>
                <Typography>
                    {props.description}
                </Typography>
            </AccordionDetails>
        </Accordion>
    );
};
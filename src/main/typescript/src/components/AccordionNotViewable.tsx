import {Accordion, AccordionDetails, AccordionSummary, Avatar, Typography} from "@mui/material";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import React from "react";

export const AccordionNotViewable : React.FC = () => {
    return (
        <Accordion className={'accordion'}>
        <AccordionSummary
            style={{minWidth: '90vw'}}
            expandIcon={<ArrowDropDownIcon />}
            aria-controls="panel2-content"
            id="panel2-header"
        >
            <Avatar sx={{ bgcolor: '#df3466'}}>A</Avatar>
            <Typography sx={{ml: '1vw'}} component="span">Accordion 2</Typography>
        </AccordionSummary>
        <AccordionDetails>
            <Typography>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse
                malesuada lacus ex, sit amet blandit leo lobortis eget.
            </Typography>
        </AccordionDetails>
    </Accordion>);
}
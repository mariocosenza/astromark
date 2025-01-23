import {Accordion, AccordionDetails, AccordionSummary, Avatar, Stack, Typography} from "@mui/material";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import VisibilityOutlinedIcon from '@mui/icons-material/VisibilityOutlined';
import React, {useState} from "react";

export type AccordionViewableProps = {
    avatar: string
    id: string,
    date: Date,
    title: string,
    description: string,
    viewed: boolean,
    view: () => void,
}

export const AccordionViewable: React.FC<AccordionViewableProps> = (props: AccordionViewableProps) => {
    const [viewed, setViewed] = useState<boolean>(props.viewed)
    const [viewedColor, setViewedColor] = useState<boolean>(props.viewed);
    return (
        <Accordion className={'accordion'}>
            <AccordionSummary
                style={{minWidth: '90vw'}}
                expandIcon={<ArrowDropDownIcon color={viewedColor ? 'info' : 'error'}/>}
                aria-controls="panel2-content"
                id="panel2-header"
            >
                <Avatar sx={{bgcolor: '#df3466'}}>{props.avatar}</Avatar>
                <Typography variant={'h6'} sx={{ml: '1vw'}}
                            component="span">{props.title} del {props.date.toString()} </Typography>
            </AccordionSummary>
            <AccordionDetails>
                <Stack spacing={1} direction="row">
                    <Typography width={'98%'}>
                        {
                            props.description
                        }
                    </Typography>
                    {!viewed && <VisibilityOutlinedIcon color={'error'} onClick={(_) => {
                        setViewed(true);
                        setViewedColor(true);
                        props.view();
                    }}/>
                    }
                </Stack>
            </AccordionDetails>
        </Accordion>);
}
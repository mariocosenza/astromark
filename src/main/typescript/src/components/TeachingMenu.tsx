import * as React from 'react';
import {useState} from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import SchoolIcon from '@mui/icons-material/School';
import {IconButton} from "@mui/material";
import {changeTeaching, SelectedTeaching} from "../services/TeacherService.ts";

export type Teachings = {
    data: string[];
}

export const TeachingMenu: React.FC<Teachings> = ({data}) => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const [toggle, setToggle] = changeTeaching();
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };


    return (
        <div>
            <IconButton
                id="basic-button"
                aria-label={'Archivio'}
                aria-controls={open ? 'basic-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}
            >
                <SchoolIcon/>
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                sx={{mt: 1.5}}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                {
                    data.map((teaching: string) => <MenuItem selected={SelectedTeaching.teaching === teaching}
                                                             key={teaching} onClick={() => {
                        SelectedTeaching.teaching = teaching
                        setToggle(!toggle)
                        handleClose()
                        window.location.reload();
                    }}>{teaching}</MenuItem>)
                }
            </Menu>
        </div>
    );
}
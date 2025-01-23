import * as React from 'react';
import {useState} from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import ArchiveOutlinedIcon from '@mui/icons-material/ArchiveOutlined';
import {IconButton} from "@mui/material";
import {changeStudentOrYear, SelectedYear} from "../services/StateService.ts";


export type Year = {
    data: number[];
}


export const ArchiveMenu: React.FC<Year> = ({data}) => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const [toggle, setToggle] = changeStudentOrYear();
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
                <ArchiveOutlinedIcon/>
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
                    data.map((year: number) => <MenuItem key={year} onClick={() => {
                        SelectedYear.year = year
                        setToggle(!toggle)
                        handleClose()
                    }}>{year + '/' + (year + 1)}</MenuItem>)
                }
            </Menu>
        </div>
    );
}
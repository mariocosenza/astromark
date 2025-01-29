import * as React from 'react';
import {useState} from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import {IconButton} from "@mui/material";
import {changeStudentOrYear, SelectedStudent} from "../services/StateService.ts";

export type SchoolUserDetail = {
    id: string;
    name: string;
    surname: string;
    email: string;
    residentialAddress: string;
    taxId: string;
    birthDate: Date;
    male: boolean;
}


export type SchoolUserDetailProp = {
    data: SchoolUserDetail[]
}


export const AccountMenu: React.FC<SchoolUserDetailProp> = ({data}) => {
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
                <AccountCircleOutlinedIcon/>
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
                    data.map((student: SchoolUserDetail) => <MenuItem key={student.id} onClick={() => {
                        SelectedStudent.id = student.id
                        handleClose()
                        setToggle(!toggle)
                    }}>{student.name + ' ' + student.surname}</MenuItem>)
                }
            </Menu>
        </div>
    );
}
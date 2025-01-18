import * as React from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import {IconButton} from "@mui/material";
import {useEffect, useState} from "react";
import {SelectedStudent, SelectedYear} from "../services/StateService.ts";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";
import {getStudentYears} from "../services/StudentService.ts";

export type SchoolUserDetail =  {
    id: string;
    name: string;
    surname: string;
    email: string;
    residentialAddress: string;
    taxId: string;
    birthDate: Date;
    male: boolean;
}




export const AccountMenu: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const [data, setData]  = useState<SchoolUserDetail[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const open = Boolean(anchorEl);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData =  () => {
        try {
            axiosConfig.get<SchoolUserDetail[]>(Env.API_BASE_URL + '/parents/students').then((response) => {
               SelectedStudent.id = response.data[0].id
               setData(response.data)
               getStudentYears().then((response) => {
                   console.log(response)
                       if (response !== null && SelectedYear.isNull()) {
                           SelectedYear.year = response[0]
                       }
               })
               setLoading(false);
           });
        } catch (error) {
            console.error(error);
        }
    }

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
                <AccountCircleOutlinedIcon />
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
                  !loading && data.map((student: SchoolUserDetail) => <MenuItem key={student.id} onClick={() => {
                        SelectedStudent.id = student.id
                        handleClose()
                    }}>{student.name + ' ' + student.surname}</MenuItem>)
                }
            </Menu>
        </div>
    );
}
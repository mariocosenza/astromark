import * as React from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import ArchiveOutlinedIcon from '@mui/icons-material/ArchiveOutlined';
import {IconButton} from "@mui/material";
import {useEffect, useState} from "react";
import {getStudentYears} from "../services/StudentService.ts";
import {SelectedYear} from "../services/StateService.ts";





export const ArchiveMenu: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const [data, setData]  = useState<number[]>([]);
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

    const fetchData = () => {
        try {
           getStudentYears().then((response) => {
                    if (response !== null) {
                        if (SelectedYear.isNull()) {
                            SelectedYear.year = response[0]
                        }
                        setData(response)
                    } else {
                        setData(new Array(new Date().getFullYear()))
                    }
            })
            setLoading(false);
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
                <ArchiveOutlinedIcon />
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
                  !loading && data.map((year: number) => <MenuItem key={year} onClick={() => {
                        SelectedYear.year = year
                        handleClose()
                    }}>{year + '/' + (year + 1)}</MenuItem>)
                }
            </Menu>
        </div>
    );
}
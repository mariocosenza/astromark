import React, {useEffect, useState} from "react";
import {CircularProgress, IconButton, Stack, TableContainer, Typography,} from "@mui/material";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import {DateObject} from "react-multi-date-picker";
import Grid from "@mui/material/Grid2";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedSchoolClass} from "../../services/TeacherService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";
import {Communication} from "../studentsParents/Communication.tsx";
import EditOutlinedIcon from "@mui/icons-material/EditOutlined";
import {CommunicationComponent} from "../../components/CommunicationComponent.tsx";

export const TeacherCommunication: React.FC = () => {
    const [rows, setRows] = useState<Communication[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [changeView, setChangeView] = useState<boolean>(false)
    const [selected, setSelected] = useState<Communication>()

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<Communication[]> = await axiosConfig.get(`${Env.API_BASE_URL}/schoolClasses/${SelectedSchoolClass.id}/communications`);
            if (response.data.length) {
                setRows(response.data)
            }

            setLoading(false)
        } catch (error) {
            console.error(error);
        }
    }

    const handleEdit = (row: Communication | null) => {
        setSelected(row || {
            id: 0,
            title: '',
            description: '',
            date: new Date(),
        })

        setChangeView(true)
    }

    return (
        <div>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Comunicazioni
            </Typography>

            {changeView ? (
                <CommunicationComponent row={selected || rows[0]} returnBack={() => {
                    setChangeView(false);
                    fetchData()
                }}/>
            ) : (
                <div>
                    <Grid container spacing={8} alignItems={'center'} justifyContent={'center'} margin={'1rem'}>
                        <Typography variant="h6" className="title" fontWeight="bold">
                            {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
                        </Typography>
                    </Grid>

                    <TableContainer sx={{width: '80%', margin: '0 10%'}}>
                        <Table>
                            <TableHead>
                                <CustomTableRow>
                                    <CustomTableCell width={'20%'}>Giorno</CustomTableCell>
                                    <CustomTableCell width={'65%'}>Avviso</CustomTableCell>
                                    <CustomTableCell width={'15%'}></CustomTableCell>
                                </CustomTableRow>
                            </TableHead>

                            <TableBody>
                                <CustomTableRow key={0}>

                                    <CustomTableCell>
                                        <Stack justifyContent={'center'} alignItems={'center'}>
                                            <Typography fontWeight={'bold'} fontSize={'xx-large'} color={'textDisabled'}>/</Typography>
                                        </Stack>
                                    </CustomTableCell>

                                    <CustomTableCell>
                                        <Typography variant={'h5'} color={'textDisabled'}>
                                            Inserisci Nuova Comunicazione
                                        </Typography>
                                    </CustomTableCell>

                                    <CustomTableCell align={'center'}>
                                        <IconButton onClick={() => handleEdit(null)}>
                                            <EditOutlinedIcon fontSize={'large'}/>
                                        </IconButton>
                                    </CustomTableCell>

                                </CustomTableRow>
                                {loading ? null : rows.map((row) => (
                                    <CustomTableRow key={row.id}>

                                        <CustomTableCell>
                                            <Stack justifyContent={'center'} alignItems={'center'}>
                                                <Typography fontWeight={'bold'} fontSize={'xx-large'}>
                                                    {new DateObject(row.date).format("DD/MM")}
                                                </Typography>
                                            </Stack>
                                        </CustomTableCell>

                                        <CustomTableCell>
                                            <Typography variant={'h6'} fontWeight={'bold'}>
                                                {row.title}
                                            </Typography>
                                            {row.description}
                                        </CustomTableCell>

                                        <CustomTableCell align={'center'}>
                                            <IconButton onClick={() => handleEdit(row)}>
                                                <EditOutlinedIcon fontSize={'large'}/>
                                            </IconButton>
                                        </CustomTableCell>
                                    </CustomTableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>

                </div>
            )}

            <Grid container justifyContent={'center'}>
                {loading ? <CircularProgress size={150}/> : null}
            </Grid>
        </div>
    );
};
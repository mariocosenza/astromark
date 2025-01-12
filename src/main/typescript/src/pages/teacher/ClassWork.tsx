import React from "react";
import {
    CircularProgress,
    IconButton,
    Stack,
    styled, tableCellClasses,
    TableContainer,
    Typography,
} from "@mui/material";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";
import TableBody from "@mui/material/TableBody";
import {blue} from "@mui/material/colors";
import EditOutlinedIcon from '@mui/icons-material/EditOutlined';
import DatePicker, {DateObject} from "react-multi-date-picker";
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import Grid from "@mui/material/Grid2";
import {TeacherClassResponse} from "../../entities/TeacherClassResponse.ts";

interface RowData {
    firm: boolean;
    hour: number;
    name: string;
    subject: string;
    activityBold: string
    activity: string;
    homeworkBold: string;
    homework: string;
}

const rows: RowData[] = [
    { firm: false, hour: 1, name: '', subject: '', activityBold: '', activity: '', homeworkBold: '', homework: '' },
    { firm: true, hour: 2, name: 'Mario Rossi', subject: 'Matematica', activityBold: 'Spiegazione teoria: ', activity: 'Disequazioni', homeworkBold: 'Studio', homework: 'Studiare da pag 3 a pag 4' },
    { firm: true, hour: 3, name: 'Luigi Bianchi', subject: 'Italiano', activityBold: 'Spiegazione teoria: ', activity: 'Dante ', homeworkBold: 'Studio', homework: 'Studiare da pag 3 a pag 4' },
    { firm: true, hour: 4, name: 'Sara Verdi', subject: 'Informatica', activityBold: 'Esercitazione: ', activity: 'C++', homeworkBold: 'Esercizi', homework: 'Studiare da pag 3 a pag 4' },
    { firm: false, hour: 5, name: '', subject: '', activityBold: '', activity: '', homeworkBold: '', homework: '' },
    { firm: true, hour: 6, name: 'Mario Rossi', subject: 'Matematica', activityBold: 'Esercitazione: ', activity: 'Disequazioni', homeworkBold: 'Esercizi', homework: 'Studiare da pag 3 a pag 4' },
];

const CustomTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
    },
}));

const CustomTableCell = styled(TableCell)(({ }) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: blue[900],
        color: 'white',
        borderColor: 'black',
    },

    border: '1px solid gray',
    fontSize: '1.1rem',
}));

export const ClassWork: React.FC = () => {
    const schoolClass: TeacherClassResponse = {id: 0, number: 3, letter: 'A', description: 'Tradizionale'}
    const loading = false;

    const handleRowClick = (row: RowData) => {
        alert(`Hai cliccato su ${row.hour}`);
    };

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                Giornale di Classe
            </Typography>

            <Grid container alignItems={'center'} justifyContent={'center'} margin={'1rem'}>
                <Grid size={2}>
                    {loading ? null : (
                        <Typography variant="h6" className="title" fontWeight="bold">
                            {schoolClass.number + schoolClass.letter + ' - ' + schoolClass.description}
                        </Typography>
                    )}
                </Grid>
                <Grid>
                    <Stack direction={'column'} justifyContent={'center'}>
                        <Typography variant="caption" color={'textSecondary'}>
                            Data
                        </Typography>
                        <DatePicker
                            buttons={true}
                            value={new DateObject()}
                            onChange={console.log}
                        />
                    </Stack>
                </Grid>
            </Grid>

            <TableContainer sx={{ width: '90%', margin: '0 5%', paddingBottom: '1rem'}}>
                <Table>
                    <TableHead>
                        <CustomTableRow>
                            <CustomTableCell>Firma</CustomTableCell>
                            <CustomTableCell>Docente</CustomTableCell>
                            <CustomTableCell>Materia</CustomTableCell>
                            <CustomTableCell>Attività Svolta</CustomTableCell>
                            <CustomTableCell>Compiti Assegnati</CustomTableCell>
                            <CustomTableCell></CustomTableCell>
                        </CustomTableRow>
                    </TableHead>

                    <TableBody>
                        { loading ? null : rows.map((row) => (
                            <CustomTableRow key={row.hour}>
                                <CustomTableCell padding={'none'}>
                                    <Stack direction={'column'} padding={'0.5rem 1rem'} alignItems={'center'}>
                                            {row.firm ? <CheckCircleOutlineIcon fontSize={'large'} color={'success'}/> :
                                                <IconButton>
                                                    <AddCircleOutlineIcon fontSize={'large'} onClick={() => handleRowClick(row)}/>
                                                </IconButton>
                                            }

                                        <Typography variant="caption" color={'textSecondary'}>
                                            {(row.hour + 7) + ':00 - ' + (row.hour + 8) + ':00'}
                                        </Typography>
                                    </Stack>
                                </CustomTableCell>

                                <CustomTableCell>{row.name}</CustomTableCell>
                                <CustomTableCell>{row.subject}</CustomTableCell>

                                <CustomTableCell>
                                    <Typography fontWeight={'bold'} fontSize={'large'}>
                                        {row.activityBold + (row.activityBold === '' ? '' : ':')}
                                    </Typography>
                                    {row.activity}
                                </CustomTableCell>

                                <CustomTableCell>
                                    <Typography fontWeight={'bold'} fontSize={'large'}>
                                        {row.homeworkBold + (row.homeworkBold === '' ? '' : ':')}
                                    </Typography>
                                    {row.homework}
                                </CustomTableCell>

                                <CustomTableCell align={'center'}>
                                    <IconButton onClick={() => handleRowClick(row)}>
                                        <EditOutlinedIcon fontSize={'large'}/>
                                    </IconButton>
                                </CustomTableCell>
                            </CustomTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            { loading ? (
                <Grid container justifyContent={'center'}>
                    <CircularProgress size={150}/>
                </Grid>) : null }
        </div>
    );
};
import React, {useEffect, useState} from "react";


import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import RemoveRedEyeOutlinedIcon from '@mui/icons-material/RemoveRedEyeOutlined';
import {getRole} from "../../services/AuthService.ts";
import {Role} from "../../components/route/ProtectedRoute.tsx";
import {Typography} from "@mui/material";

export interface SemesterReportResponse {
    id: number;
    firstSemester: boolean;
    publicField: boolean;
    passed: boolean;
    viewed: boolean;
    year: number;
    semesterReportMarks: SemesterReportMarkResponse[];
}

export interface SemesterReportMarkResponse {
    id: number;
    title: string;
    mark: number;
}

export const SemesterReport: React.FC = () => {
    const [data, setData] = useState<SemesterReportResponse>();
    const [secondData, setSecondData] = useState<SemesterReportResponse>();
    const [loading, setLoading] = useState<boolean>(true);
    const [toggle, _] = changeStudentOrYear();
    const view = async (id: number) => {
        await axiosConfig.patch(Env.API_BASE_URL + '/students' + '/reports/' + id)
        await fetchData()
    }


    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const reportResponse: AxiosResponse<SemesterReportResponse | any> = await axiosConfig.get(
                `${Env.API_BASE_URL}/students/${SelectedStudent.id}/reports/${SelectedYear.year}`, {params: {"firstSemester": true}}
            );

            const reportResponse1: AxiosResponse<SemesterReportResponse> = await axiosConfig.get(
                `${Env.API_BASE_URL}/students/${SelectedStudent.id}/reports/${SelectedYear.year}`, {params: {"firstSemester": false}}
            );

            if (reportResponse.data) {
                (reportResponse.data as SemesterReportResponse).semesterReportMarks = [...(reportResponse.data as SemesterReportResponse).semesterReportMarks].sort((a, b) => a.title.localeCompare(b.title));
                setData(reportResponse.data as SemesterReportResponse);
            }

            if (reportResponse1.data) {
                (reportResponse1.data as SemesterReportResponse).semesterReportMarks = [...(reportResponse1.data as SemesterReportResponse).semesterReportMarks].sort((a, b) => a.title.localeCompare(b.title));
                setSecondData(reportResponse1.data as SemesterReportResponse);
            }
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    };


    return (
        <div>
            {
                loading || data === undefined ? <div>Nessuna pagella presente</div> :
                    <TableContainer style={{
                        display: 'flex',
                        margin: 'auto',
                        background: '#FEF8C0',
                        borderRadius: '1rem',
                        maxWidth: '95vw',
                        marginTop: '1rem'
                    }} component={Paper}>
                        <Table sx={{minWidth: 250}} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell style={{background: '#7D7842', color: 'white'}}><Typography
                                        variant={'h6'}>Pagella</Typography></TableCell>
                                    <TableCell style={{background: '#7D7842', color: 'white'}} align="right"><Typography
                                        variant={'h6'}>Primo Semestre</Typography></TableCell>
                                    <TableCell style={{background: '#7D7842', color: 'white'}} align="right"><Typography
                                        variant={'h6'}>Secondo Semestre</Typography></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data?.semesterReportMarks.map((row, i) => (
                                    <TableRow
                                        key={row.title}
                                        sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                    >
                                        <TableCell align="left" component="th" scope="row">
                                            <b>    {row.title} </b>
                                        </TableCell>
                                        <TableCell align="right" component="th" scope="row"
                                                   style={{color: row.mark < 6 ? 'red' : 'green'}}>
                                            {row.mark}
                                        </TableCell>
                                        {
                                            secondData?.semesterReportMarks !== null && <TableCell
                                                align="right">{secondData?.semesterReportMarks[i].mark}</TableCell>
                                        }
                                    </TableRow>
                                ))}
                                {getRole().toUpperCase() === Role.PARENT &&
                                    <TableRow>
                                        <TableCell>
                                            Visualizza
                                        </TableCell>
                                        <TableCell align="right" style={{color: 'darkgreen'}}>
                                            {
                                                data?.viewed === false &&
                                                <RemoveRedEyeOutlinedIcon onClick={() => {
                                                    view(data.id)
                                                }}/>
                                            }
                                        </TableCell>
                                        <TableCell align="right" style={{color: 'darkgreen'}}>
                                            {
                                                secondData?.viewed === false &&
                                                <RemoveRedEyeOutlinedIcon onClick={() => view(secondData.id)}/>
                                            }
                                        </TableCell>
                                    </TableRow>
                                }
                            </TableBody>
                        </Table>
                    </TableContainer>
            }
        </div>
    );
}
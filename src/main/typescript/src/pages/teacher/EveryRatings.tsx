import React, {useEffect, useState} from "react";
import {CircularProgress, Stack, TableContainer, Typography,} from "@mui/material";
import {TeacherDashboardNavbar} from "../../components/TeacherDashboardNavbar.tsx";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableBody from "@mui/material/TableBody";
import Grid from "@mui/material/Grid2";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedSchoolClass, SelectedTeaching} from "../../services/TeacherService.ts";
import {CustomTableCell, CustomTableRow} from "../../components/CustomTableComponents.tsx";
import {RatingsResponse} from "../../entities/RatingsResponse.ts";
import {formatMark, formatType, RatingsRow} from "./Ratings.tsx";
import {DateObject} from "react-multi-date-picker";

export type StudentRatingsRow = {
    studentId: string,
    name: string,
    marks: RatingsRow[],
}

export const EveryRatings: React.FC = () => {
    const [rows, setRows] = useState<StudentRatingsRow[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [dates, setDates] = useState<DateObject[]>([]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<RatingsResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/classes/${SelectedSchoolClass.id}/EveryRatings/${SelectedTeaching.teaching}`);
            let rowResponse : StudentRatingsRow[] = [];
            let MarkDates: DateObject[] = [];
            response.data.map((mark: RatingsResponse) => {

                if (!MarkDates.includes(mark.date)){
                    MarkDates.push(mark.date)
                }

                let found = false
                rowResponse.map((student: StudentRatingsRow)=> {
                    if (student.studentId === mark.studentId){
                        student.marks.push({
                            id: mark.id,
                            student: mark.studentId,
                            name: mark.name + ' ' + mark.surname,
                            subject: mark.subject,
                            mark: mark.mark,
                            type: mark.type,
                            desc: mark.description,
                            date: mark.date,
                        })

                        found = true
                        return
                    }
                })

                if (!found) {
                    rowResponse.push({
                        studentId: mark.studentId,
                        name: mark.name + ' ' + mark.surname,
                        marks: [{
                            id: mark.id,
                            student: mark.studentId,
                            name: mark.name + ' ' + mark.surname,
                            subject: mark.subject,
                            mark: mark.mark,
                            type: mark.type,
                            desc: mark.description,
                            date: mark.date,
                        }]
                    })
                }
            });

            setLoading(false)
            setRows(rowResponse)
            setDates(MarkDates.sort())
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div>
            <TeacherDashboardNavbar/>
            <Typography variant="h4" className="title" fontWeight="bold" marginTop={'revert'}>
                {SelectedSchoolClass.title + ' - ' + SelectedSchoolClass.desc}
            </Typography>

            <TableContainer sx={{width: '80%', margin: '2rem 10%'}}>
                <Table>
                    <TableHead>
                        <CustomTableRow>
                            <CustomTableCell width={'30%'}>Alunno</CustomTableCell>
                            <CustomTableCell width={'70%'}>Voto</CustomTableCell>
                        </CustomTableRow>
                    </TableHead>

                    <TableBody>
                        {loading ? null : rows.map((row) => (
                            <CustomTableRow key={row.studentId}>
                                <CustomTableCell>{row.name}</CustomTableCell>
                                <CustomTableCell sx={{padding: '0'}}>
                                    <Stack direction="row" sx={{ padding: '0' }}>
                                        {dates.map((date) => {
                                            const rating = row.marks.find((mark) => mark.date === date);
                                            return (
                                                <Stack key={row.studentId + date.toString()} alignItems={'center'} sx={{
                                                    width: 100/dates.length + '%',
                                                    color: rating?.mark ? 'white' : 'black',
                                                    backgroundColor: (!rating?.mark ? '' : rating.mark < 6 ? 'var(--md-sys-color-error)' : 'green')
                                                }}>
                                                    <Typography fontSize={'xx-large'} fontWeight={'bold'}>
                                                        {rating?.mark ? formatMark(rating.mark) : ''}
                                                    </Typography>
                                                    <Typography fontSize={'small'}>
                                                        {rating ? formatType(rating.type) + ', ' + new DateObject(rating.date).format("DD-MM") : ''}
                                                    </Typography>
                                                </Stack>
                                            );
                                        })}

                                    </Stack>
                                </CustomTableCell>
                            </CustomTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Grid container justifyContent={'center'}>
                {loading ? <CircularProgress size={150}/> : null}
            </Grid>
        </div>
    );
};

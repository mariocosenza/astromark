import React, { useState } from 'react';
import {Card, CardContent, Typography, Stack, RadioGroup, FormControlLabel, Radio, Button, Box, TextField, Autocomplete} from '@mui/material';
import {formatMark, RatingsRow} from "../pages/teacher/Ratings.tsx";
import {DateObject} from "react-multi-date-picker";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";
import {getId} from "../services/AuthService.ts";
import {MarkRequest} from "../entities/MarkRequest.ts";
import {MarkUpdateRequest} from "../entities/MarkUpdateRequest.ts";
import {SelectedTeaching} from "../services/TeacherService.ts";

export const RatingComponent: React.FC<{row: RatingsRow, returnBack: () => void, date: DateObject }> = ({ row , returnBack, date}) => {
    const [mark, setMark] = useState<number | null>(row.mark);
    const [type, setType] = useState<string>(row.type);
    const [note, setNote] = useState<string>(row.desc);

    const marksList = Array.from({ length: 41 }, (_, i) => ((i) / 4));

    const handleSave = async () => {
        if (row.id === null){
            await CreateMark();
        } else {
            await UpdateMark();
        }
        returnBack();
    }

    const CreateMark = async () => {
        const markRequest: MarkRequest = {
            studentId: row.student,
            teachingId: {
                teacherId: getId(),
                subjectTitle: SelectedTeaching.teaching,
            },
            date: date.toDate(),
            description: note,
            mark: Number(mark),
            type: type
        }

        try{
            await axiosConfig.post(`${Env.API_BASE_URL}/students/marks`, markRequest, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

        } catch (error) {
            console.log(error);
        }
    }

    const UpdateMark = async () => {
        const markUpdateRequest: MarkUpdateRequest = {
            id: row.id,
            description: note,
            mark: Number(mark),
            type: type
        }

        try{
            await axiosConfig.patch(`${Env.API_BASE_URL}/students/marks/${row.student}`, markUpdateRequest, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

        } catch (error) {
            console.log(error);
        }
    }

    return (
        <div>
            <Card elevation={10} sx={{ margin: '2rem 30%', borderRadius: 2 }}>
                <CardContent>
                    <Stack direction="row" justifyContent={'space-between'} alignItems={'center'} margin={2} padding={2}>
                        <Typography variant="h5" fontWeight="bold">
                            {row.name}
                        </Typography>
                        <Typography variant="h6">{date.format("DD/MM/YYYY")}</Typography>
                    </Stack>
                </CardContent>
            </Card>

            <Card elevation={10} sx={{ margin: '2rem 30%', borderRadius: 2 }}>
                <CardContent>
                    <Stack spacing={3}>
                        <Stack direction="row" justifyContent={'center'} alignItems={'center'} margin={2} spacing={10}>
                            <Box>
                                <Typography variant="subtitle1">Voto:</Typography>
                                <Autocomplete
                                    options={marksList}
                                    getOptionLabel={(option) => `${formatMark(option)}`}
                                    value={mark}
                                    onChange={(_, newValue) => setMark(newValue)}
                                    renderInput={(params) => (
                                        <TextField {...params} placeholder="Seleziona un voto" variant="outlined" />
                                    )}
                                    fullWidth
                                />

                            </Box>
                            <Box>
                                <Typography variant="subtitle1">Tipo di prova:</Typography>
                                <RadioGroup row value={type}
                                    onChange={(e) => setType(e.target.value)}>

                                    <FormControlLabel
                                        value='ORAL'
                                        control={<Radio />}
                                        label="Orale"
                                    />
                                    <FormControlLabel
                                        value='WRITTEN'
                                        control={<Radio />}
                                        label="Scritto"
                                    />
                                    <FormControlLabel
                                        value='LABORATORY'
                                        control={<Radio />}
                                        label="Laboratorio"
                                    />
                                </RadioGroup>
                            </Box>
                        </Stack>

                        <Stack direction="row" justifyContent={'space-around'} alignItems={'center'} spacing={2}>
                            <Box width={'100%'}>
                                <Typography variant="subtitle1">Nota:</Typography>
                                <TextField fullWidth multiline minRows={3} value={note}
                                    onChange={(e) => setNote(e.target.value)}
                                />
                            </Box>

                            <Button variant="contained" color="primary" sx={{ alignSelf: 'flex-end', borderRadius: 5 }} onClick={handleSave}>
                                Salva
                            </Button>
                        </Stack>
                    </Stack>
                </CardContent>
            </Card>
        </div>
    );
};

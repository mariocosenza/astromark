import React, {useState} from 'react';
import {Button, Card, CardContent, Stack, TextField} from '@mui/material';
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";
import {SelectedSchoolClass} from "../services/TeacherService.ts";
import {Communication} from "../pages/studentsParents/Communication.tsx";
import {CommunicationRequest} from "../entities/CommunicationRequest.ts";

export const CommunicationComponent: React.FC<{ row: Communication, returnBack: () => void }> = ({row, returnBack}) => {
    const [title, setTitle] = useState<string>(row.title);
    const [text, setText] = useState<string>(row.description);
    const [error, setError] = useState<boolean>(false)

    const handleSave = async () => {
        if (title.trim().length > 0) {
            const communication: CommunicationRequest = {
                classId: SelectedSchoolClass.id,
                title: title,
                description: text,
            }

            try {
                if (row.id === 0) {
                    await axiosConfig.post(`${Env.API_BASE_URL}/schoolClasses/communication`, communication, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                } else {
                    await axiosConfig.patch(`${Env.API_BASE_URL}/schoolClasses/communication/${row.id}`, communication, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                }

            } catch (error) {
                console.log(error);
            }
        }
        returnBack();
    }


    return (
        <Card elevation={10} sx={{margin: '2rem 30%', borderRadius: 4}}>
            <CardContent>
                <Stack marginTop={2} padding={1} spacing={3} flex={1}>
                    <TextField fullWidth variant="outlined" label="Titolo"
                               value={title}
                               error={error}
                               helperText={error ? 'Titolo non può essere vuoto' : ''}
                               onChange={(e) => {
                                   setTitle(e.target.value);
                                   setError(!e.target.value)
                               }}/>

                    <TextField fullWidth variant="outlined" multiline rows={4} label="Testo"
                               value={text}
                               onChange={(e) => {
                                   setText(e.target.value);
                                   setError(!title)
                               }}/>
                </Stack>
                <Stack justifyContent={'flex-end'} margin={2}>
                    <Button variant="contained" color="primary"
                            sx={{alignSelf: 'flex-end', borderRadius: 5, justifySelf: ''}} onClick={handleSave}>
                        Salva
                    </Button>
                </Stack>
            </CardContent>
        </Card>
    );
};

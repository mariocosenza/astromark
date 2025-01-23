import React, {useEffect} from "react";
import {
    Box,
    Checkbox,
    CircularProgress,
    Divider,
    InputLabel,
    Select,
    SelectChangeEvent,
    Stack,
    Tab,
    Tabs
} from "@mui/material";
import {AxiosResponse} from "axios";
import {SchoolClass} from "./Communication.tsx";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {ListGeneric} from "../../components/ListGeneric.tsx";
import MenuItem from "@mui/material/MenuItem";
import {ChatHomeworkComponent} from "../../components/ChatHomework.tsx";
import {HomeworkList} from "../../components/HomeworkList.tsx";
import {createGlobalState} from "react-use";


export interface ClassActivityResponse {
    id: number;
    title: string;
    description: string;
    signedHour: TeachingTimeslotResponse;
}

export interface HomeworkResponse {
    id: number;
    title: string;
    description: string;
    chat: boolean;
    dueDate: Date;
    signedHour: TeachingTimeslotResponse;
}

export interface TeachingTimeslotResponse {
    title: string;
    hour: number;
    date: Date;
}

export const openChat = createGlobalState<boolean>(false);
export const homeworkChatId = createGlobalState<number>(-1);

export const Homework: React.FC = () => {
    const [loading, setLoading] = React.useState<boolean>(true);
    const [activity, setActivity] = React.useState<HomeworkResponse[]>([]);
    const [checked, setChecked] = React.useState<boolean>(false);
    const [subject, setSubject] = React.useState<string>('Seleziona Materia');
    const [toggle, _] = changeStudentOrYear();
    const [open,] = openChat();
    const [chatId,] = homeworkChatId();

    const handleChange = (event: SelectChangeEvent) => {
        setSubject(event.target.value as string);
    };


    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const schoolClass: AxiosResponse<SchoolClass[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`);
            const response: AxiosResponse<HomeworkResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/classwork/${schoolClass.data[0].id}/homeworks/all`);
            setActivity(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }


    return (
        <Stack flex={'auto'} flexWrap={'wrap'} minWidth={'100%'} direction={'row'} spacing={'1'}>
            <Box style={{
                justifyContent: 'center',
                marginTop: '1rem',
                marginRight: '5vw',
                marginLeft: open ? '2vw' : '5vw'
            }}>
                {
                    loading ? (
                        <div>Loading...</div>
                    ) : !checked ? (
                        <HomeworkList
                            list={activity
                                .filter(
                                    (v) =>
                                        v.signedHour.title === subject || subject === 'Seleziona Materia'
                                )
                                .map((activity: HomeworkResponse) => ({
                                    id: activity.id,
                                    chat: activity.chat,
                                    avatar: 'A',
                                    title: activity.signedHour.title,
                                    description: activity.title + ': ' + activity.description,
                                    hexColor: 'dodgerblue',
                                    date: activity.signedHour.date,
                                }))}
                        />
                    ) : (
                        <HomeworkList
                            list={activity
                                .filter(
                                    (v) =>
                                        v.signedHour.title === subject || subject === 'Seleziona Materia'
                                )
                                .map((activity: HomeworkResponse) => ({
                                    id: activity.id,
                                    chat: activity.chat,
                                    avatar: 'A',
                                    title: activity.signedHour.title,
                                    description: activity.title + ': ' + activity.description,
                                    hexColor: 'dodgerblue',
                                    date: activity.signedHour.date,
                                }))
                                .reverse()}
                        />
                    )
                }

                <Stack direction={'row'} className="stack-bottom">
                    <InputLabel>
                        Ordine Inverso
                        <Checkbox onClick={() => setChecked(!checked)} checked={checked}/>
                    </InputLabel>
                    <Select
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={subject}
                        label="Materia"
                        onChange={handleChange}
                    >
                        <MenuItem style={{color: 'grey'}} value={"Seleziona Materia"}>{"Seleziona Materia"}</MenuItem>
                        <Divider/>
                        {loading ? (
                            <CircularProgress/>
                        ) : (
                            Array.from(
                                new Set(
                                    activity.map((a: ClassActivityResponse) => a.signedHour.title)
                                )
                            ).map((sub, _) => {
                                return <MenuItem key={sub} value={sub}>{sub}</MenuItem>;
                            })
                        )}
                    </Select>
                </Stack>
            </Box>
            {open &&
                <div style={{minWidth: '40vw', marginTop: '1rem'}}>
                    <ChatHomeworkComponent homeworkId={chatId}/>
                </div>
            }
        </Stack>
    );
}

const Activity: React.FC = () => {
    const [loading, setLoading] = React.useState<boolean>(true);
    const [activity, setActivity] = React.useState<ClassActivityResponse[]>([]);
    const [checked, setChecked] = React.useState<boolean>(false);
    const [subject, setSubject] = React.useState<string>('Seleziona Materia');
    const [toggle, _] = changeStudentOrYear();

    const handleChange = (event: SelectChangeEvent) => {
        setSubject(event.target.value as string);
    };

    useEffect(() => {
        fetchData()
    }, [toggle]);

    const fetchData = async () => {
        try {
            const schoolClass: AxiosResponse<SchoolClass[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`);
            const response: AxiosResponse<ClassActivityResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/classwork/${schoolClass.data[0].id}/activities/all`);
            setActivity(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }


    return (
        <div style={{display: 'flex', justifyContent: 'center', marginTop: '1rem'}}>
            {
                loading ? (
                    <div>Loading...</div>
                ) : !checked ? (
                    <ListGeneric
                        list={activity
                            .filter(
                                (v) =>
                                    v.signedHour.title === subject || subject === 'Seleziona Materia'
                            )
                            .map((activity: ClassActivityResponse) => ({
                                avatar: 'C',
                                title: `${activity.signedHour.date} ${activity.signedHour.title}`,
                                description: activity.description,
                                hexColor: 'dodgerblue',
                                date: activity.signedHour.date,
                            }))}
                    />
                ) : (
                    <ListGeneric
                        list={activity
                            .filter(
                                (v) =>
                                    v.signedHour.title === subject || subject === 'Seleziona Materia'
                            )
                            .map((activity: ClassActivityResponse) => ({
                                avatar: 'C',
                                title: `${activity.signedHour.date} ${activity.signedHour.title}`,
                                description: activity.description,
                                hexColor: 'dodgerblue',
                                date: activity.signedHour.date,
                            }))
                            .reverse()}
                    />
                )
            }
            <Stack direction={'row'} className="stack-bottom">
                <InputLabel>
                    Ordine Inverso
                    <Checkbox onClick={() => setChecked(!checked)} checked={checked}/>
                </InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={subject}
                    label="Materia"
                    onChange={handleChange}
                >
                    <MenuItem style={{color: 'grey'}} value={"Seleziona Materia"}>{"Seleziona Materia"}</MenuItem>
                    <Divider/>
                    {loading ? (
                        <CircularProgress/>
                    ) : (
                        Array.from(
                            new Set(
                                activity.map((a: ClassActivityResponse) => a.signedHour.title)
                            )
                        ).map((sub, _) => {
                            return <MenuItem key={sub} value={sub}>{sub}</MenuItem>;
                        })
                    )}
                </Select>
            </Stack>
        </div>
    );
}

export const ClassActivity: React.FC = () => {
    const [value, setValue] = React.useState(0);

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };

    return (
        <div>

            <Box sx={{width: '100%', bgcolor: 'background.paper'}}>
                <Tabs
                    value={value}
                    onChange={handleChange}
                    variant="fullWidth"
                >
                    <Tab label="Assegno e verifiche"/>
                    <Tab label="attività svolte"/>
                </Tabs>
                <Divider/>
                {
                    value === 0 ? <Homework/> : <Activity/>
                }
            </Box>
        </div>

    );
}
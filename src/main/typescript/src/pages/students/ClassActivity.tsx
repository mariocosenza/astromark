import React, {useEffect} from "react";
import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import {
    Box,
    Checkbox,
    CircularProgress,
    Divider,
    InputLabel,
    Select,
    SelectChangeEvent, Stack,
    Tab,
    Tabs
} from "@mui/material";
import {AxiosResponse} from "axios";
import {SchoolClass} from "./Communication.tsx";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {ListGeneric} from "../../components/ListGeneric.tsx";
import MenuItem from "@mui/material/MenuItem";
import {ChatHomeworkComponent} from "../../components/ChatHomework.tsx";


export interface ClassActvitityResponse {
    id: number;
    title: string;
    description: string;
    signedHour: TeachingTimeslotResponse;
}

export interface TeachingTimeslotResponse {
    title: string;
    hour: number;
    date: string;
}


export const Homework: React.FC = () => {
   return (
         <div>
              <ChatHomeworkComponent chatId={'b3886ede-bf4b-4bdd-b224-bf69fcad4ffa'}/>
         </div>
   );
}

const Activity : React.FC = () => {
    const [loading, setLoading] = React.useState<boolean>(true);
    const [activity, setActivity] = React.useState<ClassActvitityResponse[]>([]);
    const [checked, setChecked] = React.useState<boolean>(false);
    const [subject, setSubject] = React.useState<string>('Seleziona Materia');

    const handleChange = (event: SelectChangeEvent) => {
        setSubject(event.target.value as string);
    };

    useEffect(() => {
        fetchData()
    }, []);

    const fetchData = async () => {
        try {
            const schoolClass: AxiosResponse<SchoolClass[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`);
            const response: AxiosResponse<ClassActvitityResponse[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/classwork/${schoolClass.data[0].id}/activities/all`);
            setActivity(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }


    return (
        <div style={{ display: 'flex', justifyContent: 'center',  marginTop: '1rem'}}>
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
                            .map((activity: ClassActvitityResponse) => ({
                                avatar: 'C',
                                title: `${activity.signedHour.date} ${activity.title}`,
                                description: activity.description,
                                hexColor: 'dodgerblue',
                                date: new Date(),
                            }))}
                    />
                ) : (
                    <ListGeneric
                        list={activity
                            .filter(
                                (v) =>
                                    v.signedHour.title === subject || subject === 'Seleziona Materia'
                            )
                            .map((activity: ClassActvitityResponse) => ({
                                avatar: 'C',
                                title: `${activity.signedHour.date} ${activity.title}`,
                                description: activity.description,
                                hexColor: 'dodgerblue',
                                date: new Date(),
                            }))
                            .reverse()}
                    />
                )
            }
            <Stack direction={'row'} className="stack-bottom">
                <InputLabel>
                    Ordine Inverso
                    <Checkbox onClick={() => setChecked(!checked)} checked={checked} />
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
                        <CircularProgress />
                    ) : (
                        Array.from(
                            new Set(
                                activity.map((a: ClassActvitityResponse) => a.signedHour.title)
                            )
                        ).map((sub, _) => {
                            return <MenuItem value={sub}>{sub}</MenuItem>;
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
            <DashboardNavbar/>
            <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
                <Tabs
                    value={value}
                    onChange={handleChange}
                    variant="fullWidth"
                >
                    <Tab label="Assegno e verifiche" />
                    <Tab label="attività svolte" />
                </Tabs>
                <Divider/>
                {
                    value === 0? <Homework/> : <Activity/>
                }
            </Box>
        </div>

    );
}
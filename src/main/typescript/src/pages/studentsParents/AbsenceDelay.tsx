import React, {useEffect, useState} from "react";
import {Box, Divider, Stack, Tab, Tabs} from "@mui/material";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {AxiosResponse} from "axios";
import {JustifiableList, JustificationListProp} from "../../components/JustifiableList.tsx";

export type JustificationResponse = {
    id: string
    needsJustification: boolean
    justified: boolean
    justificationText: string
    date: string
}

function toJustificationListProp(justification: JustificationResponse[], absence: boolean): JustificationListProp {
    return {
        absence: absence,
        list: justification.map((item) => ({
            id: item.id,
            needsJustification: item.needsJustification,
            justified: item.justified,
            justificationText: item.justificationText,
            avatar: absence? 'A' : 'R',
            hexColor: absence? 'red' : 'orange',
            date: item.date
        }))
    }
}


const Absence : React.FC = () => {
    const [data, setData] = useState<JustificationResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [toggle, _] = changeStudentOrYear();

    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<JustificationResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/year/${SelectedYear.year}/justifiable/absences`);
            setData(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div style={{display: 'flex', justifyContent: 'center', flexDirection: 'row', marginTop: '1rem'}}>
            {
            loading? <div>loading...</div> : <JustifiableList {...toJustificationListProp(data, true)}/>
            }
        </div>
    )
}

const Delay : React.FC = () => {
    const [data, setData] = useState<JustificationResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [toggle, _] = changeStudentOrYear();

    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<JustificationResponse[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/year/${SelectedYear.year}/justifiable/delays`);
            setData(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div style={{display: 'flex', justifyContent: 'center', flexDirection: 'row', marginTop: '1rem'}}>
            {
            loading? <div>loading...</div> : <JustifiableList {...toJustificationListProp(data, false)}/>
            }
        </div>
    )
}

export const AbsenceDelays: React.FC = () => {
    const [value, setValue] = useState<number>(0);
    const [totAbsences, setTotAbsences] = useState<number>(0);
    const [totDelays, setTotDelays] = useState<number>(0);
    const [toggle, _] = changeStudentOrYear();

    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const responseAbsences: AxiosResponse<number> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/year/${SelectedYear.year}/justifiable/absences/total`);
            const responseDelays: AxiosResponse<number> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/year/${SelectedYear.year}/justifiable/delays/total`);
            setTotAbsences(responseAbsences.data);
            setTotDelays(responseDelays.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleChange = (_: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };
    return (
        <div>
            <Stack width={'100%'} spacing={0}
                   direction="row"
                   sx={{height: 'calc(100vh - 70px)'}}>
                <Box sx={{ minWidth: '70%'}}>
                    <Box sx={{ minWidth: '100%', bgcolor: 'background.paper' }}>
                        <Tabs
                            value={value}
                            onChange={handleChange}
                            variant="fullWidth"
                        >
                            <Tab label="Assenze" />
                            <Tab label="Ingressi in ritardo" />
                        </Tabs>
                        <Divider/>
                        {
                            value === 0? <Absence/> : <Delay/>
                        }
                    </Box>
                </Box>
                <Stack spacing={'20%'} direction={'column'} className={'sideContainer'}>
                        <Box bgcolor={'#904a47'} height={'20%'} style={{color: 'white', alignContent: 'center', textAlign: 'center', marginTop: '1rem', borderRadius: '1rem', marginLeft: '5%',  marginRight: '5%', boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)"}}>
                            <h1>Totale assenze e ritardi: {totAbsences + totDelays}</h1>
                        </Box>
                    <Box bgcolor={'#46483b'} height={'20%'} style={{color: 'white', alignContent: 'center', textAlign: 'center', borderRadius: '1rem', marginLeft: '5%',  marginRight: '5%', boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)"}}>
                        <h1>Totale assenze: {totAbsences}</h1>
                    </Box>
                    <Box bgcolor={'#004e5b'} height={'20%'} style={{color: 'white', alignContent: 'center', textAlign: 'center', borderRadius: '1rem', marginLeft: '5%',  marginRight: '5%', boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)"}}>
                        <h1>Totale ritardi: {totDelays}</h1>
                    </Box>
                </Stack>
            </Stack>
        </div>
    );
}
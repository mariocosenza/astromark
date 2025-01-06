import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import React, {useEffect, useState} from "react";
import {
    Box,
    CircularProgress,
    Divider,
    FormControl,
    InputLabel,
    Select,
    SelectChangeEvent,
    Stack,
    Typography
} from "@mui/material";
import DatePicker, {DateObject} from "react-multi-date-picker"
import { LineChart } from '@mui/x-charts/LineChart';
import MenuItem from "@mui/material/MenuItem";
import {deepOrange} from "@mui/material/colors";
import {ListGeneric, ListItemProp} from "../../components/ListGeneric.tsx";
import axiosConfig from "../../services/AxiosConfig.ts";
import {getId} from "../../services/AuthService.ts";
import {Env} from "../../Env.ts";
import {MarkResponse} from "../../entities/MarkResponse.ts";
import {AxiosResponse} from "axios";

const SubjectSelect = () => {
    const [age, setAge] = React.useState('');

    const handleChange = (event: SelectChangeEvent) => {
        setAge(event.target.value as string);
    };

    return (
            <FormControl style={{width: '80%'}}>
                <InputLabel id="demo-simple-select-label">Age</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={age}
                    label="Age"
                    onChange={handleChange}
                >
                    <MenuItem value={10}>Ten</MenuItem>
                    <MenuItem value={20}>Twenty</MenuItem>
                    <MenuItem value={30}>Thirty</MenuItem>
                </Select>
            </FormControl>
    );
}

export const Mark: React.FC = () => {
    const [values, setValues] = useState([
        new DateObject().subtract(4, "days"),
        new DateObject().add(4, "days")
    ])
    const [data, setData]  = useState<ListItemProp[]>([]);
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<MarkResponse[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${getId()}/marks/2024`);
            const re = response.data;
            const correctedData: ListItemProp[] = re.map((mark: MarkResponse) => ({
                avatar: mark.mark.toPrecision(2),
                title: mark.title,
                description: mark.description,
                hexColor: deepOrange[500],
            }));
            setData(correctedData);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    };



    return (
        <div>
            <DashboardNavbar/>
            <Stack spacing={2}
                   direction="row"
                   style={{justifyContent: 'center', marginTop: '1rem'}}
                   sx={{flexWrap: 'wrap'}}>
                <Box className={'centerBox surface-container'} flexDirection={'column'} height={'24vh'} width={'30%'}>
                    <SubjectSelect/>
                    <DatePicker
                        value={values}
                        onChange={setValues}
                        range
                    />
                </Box>
                <Box className={'centerBox secondary-container'} style={{flexDirection: 'column'}} height={'24vh'} width={'30%'}>
                    <Typography variant={'h5'}>
                        Media voti
                    </Typography>
                    <CircularProgress variant="determinate" value={75}/>
                    <Typography variant={'h6'}>
                        7.5
                    </Typography>
                </Box>
                <Box className={'centerBox surface-container'} height={'24vh'} width={'30%'}>
                     <LineChart
                         xAxis={[{ data: [1, 2, 3, 5, 8, 10] }]}
                         series={[
                             {
                                 data: [2, 5.5, 2, 8.5, 1.5, 5],
                             },
                         ]}
                         width={500}
                         height={300}
                     />
                 </Box>
            </Stack>
            <Divider sx={{mt: '1rem'}}/>
            <div  style={{display: 'flex', flexDirection: 'column', alignItems: 'center', overflowY: 'auto', maxHeight: '66vh', marginTop: '1vh'}}>
                {
                    loading? <CircularProgress/> : <ListGeneric list={data}/>
                }
            </div>
        </div>
    );
}
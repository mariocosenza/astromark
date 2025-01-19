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
import {deepOrange, green} from "@mui/material/colors";
import {ListGeneric, ListItemProp} from "../../components/ListGeneric.tsx";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {MarkResponse} from "../../entities/MarkResponse.ts";
import {AxiosResponse} from "axios";
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";


const compareDate   = (date : Date, dateObject: DateObject)=> {
    return new DateObject(date).unix - dateObject.unix;
}

export const Mark: React.FC = () => {
    const [data, setData]  = useState<ListItemProp[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [average, setAverage] = useState<number>(0);
    const [subject, setSubject] = useState('');
    const [toggle, _] = changeStudentOrYear();



    const [values, setValues] = useState<DateObject[]>([
        new DateObject().setMonth(9).setYear(SelectedYear.year).setDay(1),
        new DateObject().add(0, "days"),
    ])


    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<MarkResponse[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/marks/${SelectedYear.year}`);
            const averageResponse: AxiosResponse<number>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/marks/${SelectedYear.year}/averages`);
            const re = response.data;
            const correctedData: ListItemProp[] = re.map((mark: MarkResponse) => ({
                avatar: mark.mark.toPrecision(2),
                title: mark.title,
                description: mark.description,
                hexColor: mark.mark >= 6? green[500] : deepOrange[500],
                date: new Date(mark.date)
            }));
            setAverage(averageResponse.data);

            setData(correctedData);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }

    const handleChange = (event: SelectChangeEvent) => {
        setSubject(event.target.value as string);
    };

    return (
        <div>
            <Stack spacing={2}
                   direction="row"
                   style={{justifyContent: 'center', marginTop: '1rem'}}
                   sx={{flexWrap: 'wrap'}}>
                <Box className={'centerBox surface-container'} flexDirection={'column'} height={'24vh'} width={'30%'}>
                    <FormControl style={{width: '80%'}}>
                        <InputLabel id="demo-simple-select-label">Materia</InputLabel>
                        <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={subject}
                            label="Materia"
                            onChange={handleChange}
                        >
                            <MenuItem value={"Seleziona Materia"}>{"Seleziona Materia"}</MenuItem>
                            {
                                loading? <CircularProgress/> :
                                   Array.from(new Set (data.map((mark: ListItemProp) => mark.title))).map((sub, _) => {
                                             return <MenuItem value={sub}>{sub}</MenuItem>
                                    })
                            }
                        </Select>
                    </FormControl>
                    <DatePicker
                        value={values}
                        onChange={setValues}
                        range
                    />
                </Box>
                <Box className={'centerBox secondary-container'} style={{flexDirection: 'column'}} height={'24vh'} width={'30%'}>
                    <Typography variant={'h5'}>
                        Media voti annuale
                    </Typography>
                    <CircularProgress variant="determinate" value={average * 10}/>
                    <Typography variant={'h6'}>
                        {average}
                    </Typography>
                </Box>
                <Box className={'centerBox surface-container'} height={'24vh'} width={'30%'}>
                     <LineChart
                         xAxis={[{ data: data.map((mark: ListItemProp) => new DateObject(mark.date).dayOfYear) }]}
                         series={[
                             {
                                 data: data.map((mark: ListItemProp) => parseFloat(mark.avatar)),
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
                    loading? <CircularProgress/> : <ListGeneric
                        list={data.filter((mark: ListItemProp) =>
                            compareDate(mark.date, values[0]) >= 0 &&
                            compareDate(mark.date, values[1] === undefined? new DateObject(Date.now()): values[1]) <= 0
                        ).filter((mark: ListItemProp) => subject === "Seleziona Materia" || mark.title === subject || subject === "")
                        }/>
                }
            </div>
        </div>
    );
}
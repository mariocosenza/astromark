import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import React, {useState} from "react";
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

    const testArray : ListItemProp[] = [{avatar: 'A', title: 'Title', description: 'Description', hexColor: deepOrange[500]}]

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
                    <ListGeneric list={testArray}/>
            </div>
        </div>
    );
}
import React, {useState} from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    FormControlLabel,
    Radio,
    RadioGroup,
    Select,
    Stack,
    Typography,
} from "@mui/material";
import PersonOffIcon from "@mui/icons-material/PersonOff";
import MeetingRoomOutlinedIcon from "@mui/icons-material/MeetingRoomOutlined";
import {AttendanceRow} from "../pages/teacher/Attendance.tsx";
import MenuItem from "@mui/material/MenuItem";

export const DelayComponent: React.FC<{ row: AttendanceRow, returnBack: () => void }> = ({row, returnBack}) => {
    const [hour, setHour] = useState<number>(row.delayTimeHour)
    const [minute, setMinute] = useState<number>(row.delayTimeMinute)
    const [isJustified, setIsJustified] = useState<boolean>(false);

    const handleSave = () => {
        row.isDelayed = true
        row.delayTimeHour = hour
        row.delayTimeMinute = minute
        row.delayNeedJustification = !isJustified
        returnBack();
    };

    return (
        <div>
            <Card elevation={10} sx={{margin: '2rem 30%', borderRadius: 2}}>
                <CardContent>
                    <Stack direction="row" justifyContent={'space-between'} alignItems={'center'} margin={2}>
                        <Typography variant="h5" className="title" fontWeight="bold">
                            {row.name}
                        </Typography>
                        <Stack direction="row" spacing={2}>
                            <Stack alignItems="center">
                                <PersonOffIcon color="error" fontSize={'large'}/>
                                {row.totalAbsence}
                            </Stack>
                            <Stack alignItems="center">
                                <MeetingRoomOutlinedIcon sx={{color: '#EDC001'}} fontSize={'large'}/>
                                {row.totalDelay}
                            </Stack>
                        </Stack>
                    </Stack>
                </CardContent>
            </Card>

            <Card elevation={10} sx={{margin: '2rem 30%', borderRadius: 2}}>
                <CardContent>
                    <Card elevation={2}
                          sx={{
                              margin: '2rem 25%', borderRadius: 4, width: '50%',
                              backgroundColor: 'var(--md-sys-color-background)'
                          }}>
                        <CardContent>
                            <Typography variant="subtitle1" mb={2}>
                                Inserisci ora
                            </Typography>
                            <Box display="flex" alignItems="center" justifyContent="center" gap={1}>

                                <Select variant={'outlined'} value={hour.toString().padStart(2, '0')}
                                        onChange={(e) => setHour(Number(e.target.value))}>

                                    {Array.from({length: 24}, (_, i) => i).map((hour) => (
                                        <MenuItem key={hour} value={hour.toString().padStart(2, '0')}>
                                            {hour.toString().padStart(2, '0')}
                                        </MenuItem>
                                    ))}
                                </Select>

                                <Typography>:</Typography>

                                <Select variant={'outlined'} value={minute.toString().padStart(2, '0')}
                                        onChange={(e) => setMinute(Number(e.target.value))}>

                                    {Array.from({length: 60}, (_, i) => i).map((minute) => (
                                        <MenuItem key={minute} value={minute.toString().padStart(2, '0')}>
                                            {minute.toString().padStart(2, '0')}
                                        </MenuItem>
                                    ))}
                                </Select>

                            </Box>
                        </CardContent>
                    </Card>

                    <Stack direction="row" justifyContent={'space-between'} alignItems={'center'} margin={'1rem'}>
                        <Stack direction="row" alignItems={'center'} spacing={'2rem'}>
                            <Typography variant="subtitle1">Il ritardo è giustificato?</Typography>
                            <RadioGroup row value={isJustified ? 'yes' : 'no'}
                                        onChange={(e) => setIsJustified(e.target.value === 'yes')}>
                                <FormControlLabel label='Sì' value='yes' control={<Radio/>}/>
                                <FormControlLabel label='No' value='no' control={<Radio/>}/>
                            </RadioGroup>
                        </Stack>
                        <Button variant="contained" color="primary" sx={{borderRadius: 5, width: '10%'}}
                                onClick={handleSave}>
                            Salva
                        </Button>
                    </Stack>
                </CardContent>
            </Card>
        </div>
    );
};

import React, {useState} from "react";
import {Button, Card, CardContent, FormControlLabel, Radio, RadioGroup, Stack, TextField, Typography,} from "@mui/material";
import {DateObject} from "react-multi-date-picker";
import PersonOffIcon from "@mui/icons-material/PersonOff";
import MeetingRoomOutlinedIcon from "@mui/icons-material/MeetingRoomOutlined";

export const DelayComponent: React.FC<{returnBack: () => void }> = ({returnBack}) => {
    const [time, setTime] = useState<DateObject>(new DateObject())
    const [isJustified, setIsJustified] = useState<boolean>(false);

    const handleSave = () => {
        returnBack();
    };

    const handleTimeChange = (field: "hour" | "minute", value: string) => {
        let newTime = new DateObject()
        let num: number = parseInt(value) || 0;
        field === "hour" ? newTime.setHour(num) : newTime.setMinute(num)
        setTime(newTime);
    };

    return (
        <div>
            <Card elevation={10} sx={{margin: '2rem 30%', borderRadius: 2}}>
                <CardContent>
                    <Stack direction="row" justifyContent={'space-between'} alignItems={'center'} margin={2}>
                        <Typography variant="h5" className="title" fontWeight="bold">
                            Sofia Bianchi
                        </Typography>
                        <Stack direction="row" spacing={2}>
                            <Stack alignItems="center">
                                <PersonOffIcon color="error" fontSize={'large'}/>
                                3
                            </Stack>
                            <Stack alignItems="center">
                                <MeetingRoomOutlinedIcon sx={{color: '#EDC001'}} fontSize={'large'}/>
                                4
                            </Stack>
                        </Stack>
                    </Stack>
                </CardContent>
            </Card>


            <Card elevation={10} sx={{margin: '2rem 30%', borderRadius: 2}}>
                <CardContent>
                    <Card elevation={2}
                          sx={{margin: '2rem 25%', borderRadius: 4, width: '50%',
                              backgroundColor: 'var(--md-sys-color-background)'}}>
                        <CardContent>
                            <Typography variant="subtitle1" mb={2}>
                                Inserisci ora
                            </Typography>
                            <Stack direction="row" justifyContent={'center'} margin={2}>
                                <TextField type="number" variant="outlined" label="Ora"
                                    value={time.hour}
                                    onChange={(e) => handleTimeChange("hour", e.target.value)}
                                    slotProps={{
                                        inputLabel: {
                                            shrink: true,
                                        },
                                    }}
                                />
                                <Typography variant="h3" className={'title'} fontWeight="bold">
                                    :
                                </Typography>
                                <TextField type="number" variant="outlined" label="Minuti"
                                    value={time.minute}
                                    onChange={(e) => handleTimeChange("minute", e.target.value)}
                                    slotProps={{
                                        inputLabel: {
                                            shrink: true,
                                        },
                                    }}
                                />
                            </Stack>
                        </CardContent>
                    </Card>

                    <Stack direction="row" justifyContent={'space-between'} alignItems={'center'} margin={'1rem'}>
                        <Stack direction="row" alignItems={'center'} spacing={'2rem'}>
                            <Typography variant="subtitle1">Il ritardo è giustificato?</Typography>
                            <RadioGroup row value={isJustified ? 'yes' : 'no'}
                                onChange={(e) => setIsJustified(e.target.value === 'yes')}>
                                <FormControlLabel label='Sì' value='yes' control={<Radio />}/>
                                <FormControlLabel label='No' value='no' control={<Radio />}/>
                            </RadioGroup>
                        </Stack>
                        <Button variant="contained" color="primary" sx={{borderRadius: 5, width: '10%'}} onClick={handleSave}>
                            Salva
                        </Button>
                    </Stack>
                </CardContent>
            </Card>
        </div>
    );
};

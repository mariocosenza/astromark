import React, {useEffect, useState} from "react";
import {Alert, Box, Button, InputLabel, Stack, TextField} from "@mui/material";
import {getRole, logout} from "../services/AuthService.ts";
import {Role} from "../components/route/ProtectedRoute.tsx";
import {DashboardNavbar} from "../components/DashboardNavbar.tsx";
import DatePicker, {DateObject} from "react-multi-date-picker";
import axiosConfig from "../services/AxiosConfig.ts";
import {Env} from "../Env.ts";
import {AxiosResponse} from "axios";
import YupPassword from "yup-password";
import * as yup from "yup";


function sendPreferences(confirmPassword: string | undefined,password: string | undefined) {
    if (password === undefined || confirmPassword === undefined) {
        return
    }
    if(confirmPassword === password) {
        axiosConfig.patch(Env.API_BASE_URL + '/school-users/preferences', {
            password: password
        })
        logout()
    }
}

function sendAddress(address: string) {
    axiosConfig.patch(Env.API_BASE_URL + '/school-users/address', address)
}

type SchoolUser = {
    name: string,
    surname: string,
    taxId: string,
    email: string,
    residentialAddress: string,
    birthDate: Date
}


YupPassword(yup) // extend yup

const passwordValidation = yup.object().shape({
    password: yup.string()
        .strict(true)
        .password()
        .min(8, 'Minimo 8 caratteri')
        .minSymbols(1)
        .minLowercase(1)
        .minUppercase(1)
        .minNumbers(1)
        .required("Password obbligatoria"),
});

export const Settings: React.FC = () => {
    const [address, setAddress] = useState<string>()
    const [schoolUser, setSchoolUser] = useState<SchoolUser>()
    const [confirmPassword, setConfirmPassword] = useState<string>()
    const [newPassword, setNewPassword] = useState<string>()
    const [error, setError] = useState<boolean>(false)
    const [saved, setSaved] = useState<boolean>(false)

    const onPasswordChange = (password: string) => {
        if(passwordValidation.isValidSync(password)) {
            setNewPassword(password)
            setError(false)
            setSaved(false)
        } else {
            setError(true)
        }
    }

    const onConfirmPasswordChange = (password: string) => {
        if(passwordValidation.isValidSync(password)) {
            setConfirmPassword(password)
            setError(false)
            setSaved(false)
        } else {
            setError(true)
        }
    }

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const response: AxiosResponse<SchoolUser> = await axiosConfig.get(Env.API_BASE_URL + '/school-users/detailed')
            setSchoolUser(response.data)
            setAddress(response.data.residentialAddress.replaceAll('\"', ""))
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div>
            {
                getRole().toUpperCase() === Role.STUDENT || getRole().toUpperCase() === Role.PARENT ? <DashboardNavbar/> : <h1>Settings</h1>
            }
            <Stack
                spacing={2}
                direction="row"
                flexWrap={'wrap'}
                width={'100%'}
                sx={{
                    height: 'calc(100vh - 64px)',
                    justifyContent: 'center'
                }}
            >
                {
                    (schoolUser !== undefined) &&
                    <Box className={'surface-container'} style={{maxHeight: '80vh', minWidth: '40vw', marginTop: '2rem', display: 'grid', padding: '2vw'}}>
                        <h1>Anagrafica</h1>
                        <TextField disabled={true} id="name" label="Nome" variant="outlined" value={schoolUser.name}/>
                        <TextField disabled={true} id="surname" label="Cognome" variant="outlined" value={schoolUser.surname}/>
                        <TextField disabled={true} id="taxId" label="Codice Fiscale" variant="outlined" value={schoolUser.taxId}/>
                        <TextField
                            id="address"
                            name="address"
                            value={address} // Use a state variable
                            onChange={(e) =>{
                                setSaved(true)
                                setAddress(e.target.value)}}
                            label="Indirizzo"
                            variant="outlined"
                        />


                        <Box>
                            <InputLabel>Data di nascita</InputLabel>
                            <DatePicker disabled={true} value={new DateObject(schoolUser.birthDate)}/>
                        </Box>

                        {
                            saved && <Alert sx={{mb: '1rem'}} severity="success">Indirizzo corretto</Alert>
                        }
                        <Button variant="contained" onClick={() => sendAddress(address as string)} style={{maxHeight: '4rem'}}>Salva</Button>
                    </Box>
                }
                {
                    (schoolUser !== undefined) &&  <Box className={'secondary-container'} style={{maxHeight: '80vh', minWidth: '40vw', marginTop: '2rem', display: 'grid', padding: '2vw'}}>
                        <h1>Informazioni contatto</h1>
                        <TextField disabled={true} id="email" label="Email" value={schoolUser.email} variant="outlined" />
                        <TextField id="password"  onChange={ (e) => onPasswordChange(e.currentTarget.value)} label="Password" variant="outlined" />
                        <TextField id="checkPassoword" onChange={ (e) => onConfirmPasswordChange(e.currentTarget.value)} label="Conferma Password" variant="outlined" />
                        {
                            error && <Alert id="errorLogin" sx={{mb: '1rem'}} severity="error">La password deve contenere almeno un carattere speciale, un numero, una minuscola, una maiuscola e deve essere lunga almeno 8 caratteri</Alert>
                        }
                        <Button variant="contained" style={{maxHeight: '4rem'}}>Consensi privacy</Button>
                        <Button variant="contained" onClick={() => address !== undefined? sendPreferences(confirmPassword, newPassword) : null} style={{maxHeight: '4rem'}}>Salva</Button>
                    </Box>
                }
            </Stack>
        </div>
    );
}
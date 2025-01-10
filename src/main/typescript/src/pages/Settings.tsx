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
        .min(8, 'Minimo 8 caratteri')
        .matches(/[a-z]/, 'Deve contenere almeno una lettera minuscola')
        .matches(/[A-Z]/, 'Deve contenere almeno una lettera maiuscola')
        .matches(/[0-9]/, 'Deve contenere almeno un numero')
        .matches(/[^a-zA-Z0-9]/, 'Deve contenere almeno un simbolo')
        .required("Password obbligatoria"),
});

export const Settings: React.FC = () => {
    const [address, setAddress] = useState<string>();
    const [schoolUser, setSchoolUser] = useState<SchoolUser>();
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [newPassword, setNewPassword] = useState<string>('');
    const [error, setError] = useState<boolean>(false);
    const [saved, setSaved] = useState<boolean>(false);
    const [validationMessage, setValidationMessage] = useState<string>('');

    const validatePassword = async (password: string): Promise<boolean> => {
        try {
            await passwordValidation.validate({ password }, { abortEarly: false });
            return true;
        } catch (err) {
            if (err instanceof yup.ValidationError) {
                setValidationMessage(err.errors[0]);
            }
            return false;
        }
    };

    const onPasswordChange = async (password: string) => {
        setNewPassword(password);
        setSaved(false);

        const isValid = await validatePassword(password);
        setError(!isValid);
    };

    const onConfirmPasswordChange = async (password: string) => {
        setConfirmPassword(password);
        setSaved(false);

        if (password !== newPassword) {
            setError(true);
            setValidationMessage('Le password non coincidono');
            return;
        }

        const isValid = await validatePassword(password);
        setError(!isValid);
    };

    async function sendPreferences(confirmPassword: string | undefined, password: string | undefined) {
        try {
            if (!password || !confirmPassword) {
                setError(true);
                return;
            }
            if (password !== confirmPassword) {
                setError(true);
                return;
            }
            const isValid = await validatePassword(password);
            if (!isValid) {
                setError(true);
                return;
            }
            await axiosConfig.patch(Env.API_BASE_URL + '/school-users/preferences', {
                password: password
            });
            logout();
            window.location.href = '/';

        } catch (error) {
            console.error('Error updating preferences:', error);
            setError(true);
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
                        <TextField id="password"  type={'password'} onChange={ (e) => onPasswordChange(e.currentTarget.value)} label="Password" variant="outlined" />
                        <TextField id="checkPassoword" type={'password'} onChange={ (e) => onConfirmPasswordChange(e.currentTarget.value)} label="Conferma Password" variant="outlined" />
                        {
                            error && <Alert id="errorLogin" sx={{mb: '1rem'}} severity="error">{validationMessage}</Alert>
                        }
                        <Button variant="contained" style={{maxHeight: '4rem'}}>Consensi privacy</Button>
                        <Button variant="contained" onClick={() => address !== undefined? sendPreferences(confirmPassword, newPassword) : null} style={{maxHeight: '4rem'}}>Salva</Button>
                    </Box>
                }
            </Stack>
        </div>
    );
}
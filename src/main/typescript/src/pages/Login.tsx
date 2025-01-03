import {HomePageFooter} from "../components/HomePageFooter.tsx";
import {useFormik} from "formik";
import {Box, Button, TextField, ToggleButton, ToggleButtonGroup, Typography} from "@mui/material";
import * as yup from 'yup'
import YupPassword from 'yup-password'
import React from "react";
import {HomePageNavbar} from "../components/HomePageNavbar.tsx";
import {Env} from "../Env.ts";
import axios from "axios";
import {JwtToken} from "../entities/JwtToken.ts";
import {jwtDecode} from "jwt-decode";

YupPassword(yup) // extend yup

const validationSchema = yup.object().shape({
    username: yup.string()
        .strict(true)
        .min(3, 'Username troppo corto')
        .required("Username obbligatorio"),
    password: yup.string()
        .strict(true)
        .password()
        .min(8, 'Minimo 8 caratteri')
        .minSymbols(0)
        .minLowercase(0)
        .minUppercase(0)
        .minNumbers(0)
        .required("Password obbligatoria"),
    schoolCode: yup.string()
        .strict(true)
        .matches(/^SS\d{5}$/, "Codice scuola errato")
        .required("Codice scuola obbligatorio")
});

interface IFormValues {
    username: string;
    password: string;
    schoolCode: string;
    role: string;
}

const initialValues: IFormValues = {
    username: "",
    password: "",
    schoolCode: "",
    role: "student"
};


export const Login = () => {
    const [role, setRole] = React.useState('student');

    const onSubmit = async (values: IFormValues) => {
        values.role = role
        const response = await axios.post(Env.API_BASE_URL + '/auth/login', values)
        if (response.status === 200) {
            localStorage.setItem('user', JSON.stringify(response.data));
            window.location.replace((jwtDecode(response.data) as JwtToken).role.toLowerCase() + "/dashboard")
        } else if (response.status === 406) {
            window.location.replace("/first-login")
        } else {
            console.log('Credenziali errate')
        }
    };

    const validate = (values: IFormValues) => {
        console.log(values)
    };

    const formik = useFormik<IFormValues>({
        validationSchema,
        initialValues,
        onSubmit,
        validate,
    });


    const handleChange = (
        _event: React.MouseEvent<HTMLElement>,
        newRole: string,
    ) => {
        setRole(newRole);
    };

    return (
        <main style={{
            height: '100vh',
            display: 'flex',
            flexDirection: 'column'
        }}>
            <HomePageNavbar showLogin={false}/>
            <Box p='2%' style={{boxShadow: '0px 4px 50px rgba(0, 0, 0, 0.25)', margin: 'auto', maxWidth: '90vw'}}>
                <div>
                    <form onSubmit={formik.handleSubmit}>
                        <Typography variant="h5" fontWeight={600} mb={2}>Accedi ad AstroMark</Typography>
                        <TextField
                            label="Codice Scuola"
                            variant="outlined"
                            color="primary"
                            type="text"
                            name="schoolCode"
                            placeholder="SS12345"
                            sx={{mb: 3}}
                            fullWidth
                            value={formik.values.schoolCode}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            error={formik.touched.schoolCode && Boolean(formik.errors.schoolCode)}
                            helperText={formik.touched.schoolCode && formik.errors.schoolCode}
                        />
                        <TextField
                            label="Username"
                            variant="outlined"
                            color="primary"
                            type="text"
                            name="username"
                            placeholder="Mario Rossi"
                            sx={{mb: 3}}
                            fullWidth
                            value={formik.values.username}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            error={formik.touched.username && Boolean(formik.errors.username)}
                            helperText={formik.touched.username && formik.errors.username}
                        />
                        <TextField
                            label="Password"
                            variant="outlined"
                            color="primary"
                            type="password"
                            name="password"
                            placeholder="john@johndoe.com"
                            value={formik.values.password}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            error={
                                formik.touched.password && Boolean(formik.errors.password)
                            }
                            helperText={
                                formik.touched.password && formik.errors.password
                            }
                            fullWidth
                            sx={{mb: 3}}
                        />

                        <div className={'centerInForm'}>
                            <ToggleButtonGroup
                                color="primary"
                                value={role}
                                sx={{mb: '1rem'}}
                                exclusive
                                style={{flexWrap: 'wrap'}}
                                onChange={handleChange}
                                aria-label="Platform">
                                <ToggleButton value="teacher">Docente</ToggleButton>
                                <ToggleButton value="parent">Genitore</ToggleButton>
                                <ToggleButton value="student">Studente</ToggleButton>
                                <ToggleButton value="secretary">Segreteria</ToggleButton>
                            </ToggleButtonGroup>
                        </div>
                        <div className={'centerInForm'}>
                            <Button variant="contained" type="submit" size="large" disabled={!formik.isValid}>
                                Accedi
                            </Button>
                        </div>
                    </form>
                </div>

            </Box>
            <HomePageFooter/>
        </main>
    );
};

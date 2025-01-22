import {HomePageFooter} from "../components/HomePageFooter.tsx";
import {useFormik} from "formik";
import {Alert, Box, Button, TextField, ToggleButton, ToggleButtonGroup, Typography} from "@mui/material";
import * as yup from 'yup'
import YupPassword from 'yup-password'
import React, {useEffect, useState} from "react";
import {HomePageNavbar} from "../components/HomePageNavbar.tsx";
import {Env} from "../Env.ts";
import axios from "axios";
import {useNavigate} from "react-router";
import {getRole, isExpired, isLogged, replaceToken} from "../services/AuthService.ts";

YupPassword(yup)

const validationSchemaNormal = yup.object().shape({
    username: yup.string()
        .strict(true)
        .min(3, 'Username troppo corto')
        .required("Username obbligatorio"),
    password: yup.string()  // Removed strict(true)
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

const validationSchemaFirstLogin = yup.object().shape({
    username: yup.string()
        .strict(true)
        .min(3, 'Username troppo corto')
        .required("Username obbligatorio"),
    password: yup.string()  // Removed strict(true)
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
        .required("Codice scuola obbligatorio"),
    newPassword: yup.string()  // Removed strict(true)
        .password()
        .min(8, 'Minimo 8 caratteri')
        .minSymbols(0)
        .minLowercase(0)
        .minUppercase(0)
        .minNumbers(0)
        .required("Nuova password obbligatoria")
        .test("different-password", "La nuova password deve essere diversa dalla precedente",
            function (value) {
                return value !== this.parent.password;
            }),
    checkPassword: yup.string()  // Removed strict(true)
        .oneOf([yup.ref('newPassword')], 'Le password devono coincidere')
        .required("Conferma password obbligatoria")
});

interface IFormValues {
    username: string;
    password: string;
    schoolCode: string;
    role: string;
    newPassword?: string;
    checkPassword?: string;
}

const initialValues: IFormValues = {
    username: "",
    password: "",
    schoolCode: "",
    role: "student",
    newPassword: "",
    checkPassword: ""
};

export const Login = () => {
    const navigator = useNavigate()
    const [error, setError] = useState<boolean>(false);
    const [firstLogin, setFirstLogin] = useState<boolean>(false);
    const [role, setRole] = React.useState('student');

    const validationSchema = firstLogin ? validationSchemaFirstLogin : validationSchemaNormal;

    const onSubmit = async (values: IFormValues) => {
        try {
            values.role = role;
            let response;

            if (!firstLogin) {
                const data = {
                    username: values.username,
                    password: values.password,
                    schoolCode: values.schoolCode,
                    role: values.role
                }
                try {
                    response = await axios.post(Env.API_BASE_URL + '/auth/login', data);
                    localStorage.removeItem("user");
                    replaceToken(JSON.stringify(response.data));
                    navigator("/" + getRole().toLowerCase() + "/dashboard");
                } catch (error: any) {
                    if (error.response && error.response.status === 406) {
                        setFirstLogin(true);
                        setError(false);
                    } else {
                        setError(true);
                    }
                }
            } else {
                if (values.password === values.newPassword) {
                    setError(true);
                    return;
                }

                const data = {
                    username: values.username,
                    password: values.password,
                    schoolCode: values.schoolCode,
                    role: values.role,
                    newPassword: values.newPassword
                }
                try {
                    response = await axios.post(Env.API_BASE_URL + '/auth/first-login', data);
                    localStorage.removeItem("user");
                    replaceToken(JSON.stringify(response.data));
                    navigator("/" + getRole().toLowerCase() + "/dashboard");
                } catch (error) {
                    setError(true);
                }
            }
        } catch (e) {
            setError(true);
        }
    };

    const validate = () => {
        setError(false);
    };

    const formik = useFormik<IFormValues>({
        validationSchema,
        initialValues,
        onSubmit,
        validate,
        enableReinitialize: true
    });

    const handleChange = (
        _event: React.MouseEvent<HTMLElement>,
        newRole: string,
    ) => {
        setRole(newRole);
    };

    useEffect(() => {
        localStorage.removeItem("studentId");
        localStorage.removeItem("year");
        if (isLogged() && !isExpired()) {
            navigator("/" + getRole().toLowerCase() + "/dashboard");
        }
    }, [navigator]);

    return (
        <main style={{
            height: '100vh',
            display: 'flex',
            flexDirection: 'column'
        }}>
            <HomePageNavbar/>
            <Box p='2%' style={{boxShadow: '0px 4px 50px rgba(0, 0, 0, 0.25)', margin: 'auto', maxWidth: '90vw'}}>
                <div>
                    <form onSubmit={formik.handleSubmit}>
                        <Typography variant="h5" fontWeight={600} mb={2}>
                            {firstLogin ? "Primo Accesso - Cambia Password" : "Accedi ad AstroMark"}
                        </Typography>
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
                            placeholder="mario.rossi"
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
                            placeholder="*******"
                            value={formik.values.password}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            error={formik.touched.password && Boolean(formik.errors.password)}
                            helperText={formik.touched.password && formik.errors.password}
                            fullWidth
                            sx={{mb: 3}}
                        />

                        {firstLogin && (
                            <>
                                <TextField
                                    label="Nuova Password"
                                    variant="outlined"
                                    color="primary"
                                    type="password"
                                    name="newPassword"
                                    placeholder="Nuova Password"
                                    value={formik.values.newPassword}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    error={formik.touched.newPassword && Boolean(formik.errors.newPassword)}
                                    helperText={formik.touched.newPassword && formik.errors.newPassword}
                                    fullWidth
                                    sx={{mb: 3}}
                                />
                                <TextField
                                    label="Conferma Password"
                                    variant="outlined"
                                    color="primary"
                                    type="password"
                                    name="checkPassword"
                                    placeholder="Conferma Password"
                                    value={formik.values.checkPassword}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    error={formik.touched.checkPassword && Boolean(formik.errors.checkPassword)}
                                    helperText={formik.touched.checkPassword && formik.errors.checkPassword}
                                    fullWidth
                                    sx={{mb: 3}}
                                />
                            </>
                        )}

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

                        {error && <Alert id="errorLogin" sx={{mb: '1rem'}} severity="error">Credenziali errate</Alert>}

                        <div className={'centerInForm'}>
                            <Button
                                variant="contained"
                                style={{backgroundColor: 'rgb(85,108,151)', color: 'white'}}
                                type="submit"
                                size="large"
                                disabled={!formik.isValid}>
                                {firstLogin ? "Cambia Password" : "Accedi"}
                            </Button>
                        </div>
                    </form>
                </div>
            </Box>
            <HomePageFooter/>
        </main>
    );
};
import ReactDOM from "react-dom/client";
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import './styles/index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme";
import {App} from "./pages/App";
import {Login} from "./pages/Login.tsx";
import {FirstLogin} from "./pages/FirstLogin.tsx";
import {ProtectedRoutePath, Role} from "./components/route/ProtectedRoute.tsx";
import {StrictMode} from "react";
import {MissingRoute} from "./components/route/MissingRoute.tsx";
import {Dashboard} from "./pages/students/Dashboard.tsx";

const root: HTMLElement = document.getElementById("root") as HTMLElement;

ReactDOM.createRoot(root).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App />} />
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/first-login" element={<FirstLogin/>}/>
                    <Route path="/student" element={<Navigate to="/student/dashboard" replace />} />
                    <Route path="/student" element={<ProtectedRoutePath role={Role.STUDENT}/>}>
                        <Route path="dashboard" element={<Dashboard/>}/>
                        <Route path="voti" element={<Dashboard/>}/>
                        <Route path="assenze" element={<Dashboard/>}/>
                        <Route path="assegno" element={<Dashboard/>}/>
                        <Route path="note" element={<Dashboard/>}/>
                        <Route path="pagella" element={<Dashboard/>}/>
                        <Route path="orario" element={<Dashboard/>}/>
                        <Route path="avvisi" element={<Dashboard/>}/>
                        <Route path="impostazioni" element={<Dashboard/>}/>
                    </Route>
                    <Route path="/teacher" element={<Navigate to="/teacher/dashboard" replace />} />
                    <Route path="/teacher" element={<ProtectedRoutePath role={Role.TEACHER}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                    <Route path="/secretary" element={<Navigate to="/secretary/dashboard" replace />} />
                    <Route path="/secretary" element={<ProtectedRoutePath role={Role.SECRETARY}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                    <Route path="/parent" element={<Navigate to="/parent/dashboard" replace />} />
                    <Route path="/parent" element={<ProtectedRoutePath role={Role.PARENT}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                    <Route path="*" element={<MissingRoute/>}/>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>
)

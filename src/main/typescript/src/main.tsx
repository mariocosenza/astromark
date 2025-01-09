import ReactDOM from "react-dom/client";
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import './styles/index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme";
import {App} from "./pages/App";
import {Login} from "./pages/Login.tsx";
import {ProtectedRoutePath, Role} from "./components/route/ProtectedRoute.tsx";
import {StrictMode} from "react";
import {MissingRoute} from "./components/route/MissingRoute.tsx";
import {Dashboard} from "./pages/students/Dashboard.tsx";
import {Mark} from "./pages/students/Mark.tsx";
import {ClassActvitity} from "./pages/students/ClassActvitity.tsx";
import {Settings} from "./pages/Settings.tsx";
import {AbsenceDelays} from "./pages/students/Absence.tsx";
import {Allert} from "./pages/students/Communication.tsx";
import {Note} from "./pages/students/Note.tsx";
import {TeacherDashboard} from "./pages/teacher/TeacherDashboard.tsx";
import {SchoolClass} from "./pages/teacher/SchoolClass.tsx";

const root: HTMLElement = document.getElementById("root") as HTMLElement;

ReactDOM.createRoot(root).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App />} />
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/student" element={<Navigate to="/student/dashboard" replace />} />
                    <Route path="/student" element={<ProtectedRoutePath role={Role.STUDENT}/>}>
                        <Route path="dashboard" element={<Dashboard/>}/>
                        <Route path="voti" element={<Mark/>}/>
                        <Route path="assenze" element={<AbsenceDelays/>}/>
                        <Route path="assegno" element={<ClassActvitity/>}/>
                        <Route path="note" element={<Note/>}/>
                        <Route path="pagella" element={<Dashboard/>}/>
                        <Route path="orario" element={<Dashboard/>}/>
                        <Route path="avvisi" element={<Allert/>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                    </Route>
                    <Route path="/teacher" element={<Navigate to="/teacher/dashboard" replace />} />
                    <Route path="/teacher" element={<ProtectedRoutePath role={Role.TEACHER}/>}>
                        <Route path="dashboard" element={<TeacherDashboard/>}/>
                        <Route path="classi" element={<SchoolClass/>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                    </Route>
                    <Route path="/secretary" element={<Navigate to="/secretary/dashboard" replace />} />
                    <Route path="/secretary" element={<ProtectedRoutePath role={Role.SECRETARY}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                    </Route>
                    <Route path="/parent" element={<Navigate to="/parent/dashboard" replace />} />
                    <Route path="/parent" element={<ProtectedRoutePath role={Role.PARENT}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                    </Route>
                    <Route path="*" element={<MissingRoute/>}/>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>
)

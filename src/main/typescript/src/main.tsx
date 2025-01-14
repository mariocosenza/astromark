import ReactDOM from "react-dom/client";
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import './styles/index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme";
import {App} from "./pages/App";
import {Login} from "./pages/Login.tsx";
import {ProtectedRoutePath, Role} from "./components/route/ProtectedRoute.tsx";
import React, {StrictMode} from "react";
import {MissingRoute} from "./components/route/MissingRoute.tsx";
import {Dashboard} from "./pages/students/Dashboard.tsx";
import {Mark} from "./pages/students/Mark.tsx";
import {ClassActivity} from "./pages/students/ClassActivity.tsx";
import {Settings} from "./pages/Settings.tsx";
import {AbsenceDelays} from "./pages/students/AbsenceDelay.tsx";
import {Allert} from "./pages/students/Communication.tsx";
import {Note} from "./pages/students/Note.tsx";
import {TeacherDashboard} from "./pages/teacher/TeacherDashboard.tsx";
import {SchoolClass} from "./pages/teacher/SchoolClass.tsx";
import {TeacherTicket} from "./pages/teacher/TeacherTicket.tsx";
import {Timetable} from "./pages/students/Timetable.tsx";
import {SemesterReport} from "./pages/students/Report.tsx";
import {isSelectedClass} from "./services/TeacherService.ts";
import {ClassAgenda} from "./pages/teacher/ClassAgenda.tsx";

const root: HTMLElement = document.getElementById("root") as HTMLElement;

const ConditionalRoute = ({node, condition,}: { node: React.ReactNode; condition: () => boolean;}) => {
    return condition() ? <>{node}</> : <Navigate to="/" replace />;
};

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
                        <Route path="assegno" element={<ClassActivity/>}/>
                        <Route path="note" element={<Note/>}/>
                        <Route path="pagella" element={<SemesterReport/>}/>
                        <Route path="orario" element={<Timetable/>}/>
                        <Route path="avvisi" element={<Allert/>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                    </Route>
                    <Route path="/teacher" element={<Navigate to="/teacher/dashboard" replace />} />
                    <Route path="/teacher" element={<ProtectedRoutePath role={Role.TEACHER}/>}>
                        <Route path="dashboard" element={<TeacherDashboard/>}/>
                        <Route path="classi" element={<SchoolClass/>}/>
                        <Route path="ticket" element={<TeacherTicket/>}/>
                        <Route path="agenda" element={<ConditionalRoute node={<ClassAgenda/>} condition={isSelectedClass}/>}/>
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

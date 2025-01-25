import ReactDOM from "react-dom/client";
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import './styles/index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme";
import {App} from "./pages/App";
import {Login} from "./pages/Login.tsx";
import {Role} from "./components/route/ProtectedRoute.tsx";
import {ReactNode, StrictMode} from "react";
import {MissingRoute} from "./components/route/MissingRoute.tsx";
import {Dashboard} from "./pages/./studentsParents/Dashboard.tsx";
import {Mark} from "./pages/./studentsParents/Mark.tsx";
import {ClassActivity} from "./pages/./studentsParents/ClassActivity.tsx";
import {Settings} from "./pages/Settings.tsx";
import {AbsenceDelays} from "./pages/./studentsParents/AbsenceDelay.tsx";
import {Allert} from "./pages/./studentsParents/Communication.tsx";
import {Note} from "./pages/./studentsParents/Note.tsx";
import {TeacherDashboard} from "./pages/teacher/TeacherDashboard.tsx";
import {SchoolClass} from "./pages/teacher/SchoolClass.tsx";
import {TeacherTicket} from "./pages/teacher/TeacherTicket.tsx";
import {Timetable} from "./pages/./studentsParents/Timetable.tsx";
import {SemesterReport} from "./pages/./studentsParents/Report.tsx";
import {isSelectedClass} from "./services/TeacherService.ts";
import {ClassAgenda} from "./pages/teacher/ClassAgenda.tsx";
import {ProtectedStudentParentRoute} from "./components/route/ProtectedStudentParentRoute.tsx";
import {Ticket} from "./pages/parents/Ticket.tsx";
import {Reception} from "./pages/parents/Reception.tsx";
import {SignHour} from "./pages/teacher/SignHour.tsx";
import {Attendance} from "./pages/teacher/Attendance.tsx";
import {Ratings} from "./pages/teacher/Ratings.tsx";
import {EveryRatings} from "./pages/teacher/EveryRatings.tsx";
import {TeacherNote} from "./pages/teacher/TeacherNote.tsx";
import {ProtectedSecretaryRoute} from "./components/route/ProtectedSecretaryRoute.tsx";
import {SecretaryDashboard} from "./pages/secretary/SecretaryDashboard.tsx";
import {ManageClass} from "./pages/secretary/ManageClass.tsx";
import {SecretaryTicket} from "./pages/secretary/SecretaryTicket.tsx";
import {CreateClass} from "./pages/secretary/CreateClass.tsx";
import {ManageTeacher} from "./pages/secretary/ManageTeacher.tsx";
import {ManageTimetable} from "./pages/secretary/ManageTimetable";
import {ClassSchedule} from "./pages/secretary/ClassSchedule.tsx";
import {ProtectedTeacherRoute} from "./components/route/ProtectedTeacherRoute.tsx";


const root: HTMLElement = document.getElementById("root") as HTMLElement;

const ConditionalRoute = ({node, condition,}: { node: ReactNode; condition: () => boolean; }) => {
    return condition() ? <>{node}</> : <Navigate to="/" replace/>;
};

ReactDOM.createRoot(root).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/student" element={<Navigate to="/student/dashboard" replace/>}/>
                    <Route path="/student" element={<ProtectedStudentParentRoute role={Role.STUDENT}/>}>
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
                    <Route path="/teacher" element={<Navigate to="/teacher/dashboard" replace/>}/>
                    <Route path="/teacher" element={<ProtectedTeacherRoute role={Role.TEACHER}/>}>
                        <Route path="dashboard" element={<TeacherDashboard/>}/>
                        <Route path="classi" element={<SchoolClass/>}/>
                        <Route path="ticket" element={<TeacherTicket/>}/>
                        <Route path="agenda"
                               element={<ConditionalRoute node={<ClassAgenda/>} condition={isSelectedClass}/>}/>
                        <Route path="ora" element={<ConditionalRoute node={<SignHour/>} condition={isSelectedClass}/>}/>
                        <Route path="appello"
                               element={<ConditionalRoute node={<Attendance/>} condition={isSelectedClass}/>}/>
                        <Route path="valutazioni"
                               element={<ConditionalRoute node={<Ratings/>} condition={isSelectedClass}/>}/>
                        <Route path="valutazioni/tutte"
                               element={<ConditionalRoute node={<EveryRatings/>} condition={isSelectedClass}/>}/>
                        <Route path="note"
                               element={<ConditionalRoute node={<TeacherNote/>} condition={isSelectedClass}/>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                    </Route>
                    <Route path="/secretary" element={<Navigate to="/secretary/dashboard" replace/>}/>
                    <Route path="/secretary" element={<ProtectedSecretaryRoute role={Role.SECRETARY}/>}>
                        <Route path="dashboard" element={<SecretaryDashboard/>}/>
                        <Route path="manage-class" element={<ManageClass/>}/>
                        <Route path="ticket" element={<SecretaryTicket/>}/>
                        <Route path="add-class" element={<CreateClass/>}/>
                        <Route path='timetable' element={<ManageTimetable/>}/>
                        <Route path="teacher" element={<ManageTeacher/>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                        <Route path="class-schedule/:classId" element={<ClassSchedule/>}/>

                    </Route>
                    <Route path="/parent" element={<Navigate to="/parent/dashboard" replace/>}/>
                    <Route path="/parent" element={<ProtectedStudentParentRoute role={Role.PARENT}/>}>
                        <Route path="dashboard" element={<Dashboard/>}/>
                        <Route path="voti" element={<Mark/>}/>
                        <Route path="assenze" element={<AbsenceDelays/>}/>
                        <Route path="assegno" element={<ClassActivity/>}/>
                        <Route path="note" element={<Note/>}/>
                        <Route path="pagella" element={<SemesterReport/>}/>
                        <Route path="orario" element={<Timetable/>}/>
                        <Route path="avvisi" element={<Allert/>}/>
                        <Route path="impostazioni" element={<Settings/>}/>
                        <Route path="ticket" element={<Ticket/>}/>
                        <Route path="ricevimento" element={<Reception/>}/>
                    </Route>
                    <Route path="*" element={<MissingRoute/>}/>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>
)

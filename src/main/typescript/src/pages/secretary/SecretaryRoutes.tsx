import { Routes, Route, Navigate } from "react-router";
import ManageTimetable from "./ManageTimetable.tsx";
import ClassSchedule from "./ClassSchedule.tsx";

const SecretaryRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/classes" />} />
            <Route path="/classes" element={<ManageTimetable />} />
            <Route path="/class-schedule/:classId" element={<ClassSchedule />} />
        </Routes>
    );
};

export default SecretaryRoutes;

import {DateObject} from "react-multi-date-picker";

export type AttendanceResponse = {
    id: string;
    name: string;
    surname: string;
    isAbsent: boolean;
    isDelayed: boolean;
    delayTime: DateObject;
    totalAbsence: number;
    totalDelay: number;
}
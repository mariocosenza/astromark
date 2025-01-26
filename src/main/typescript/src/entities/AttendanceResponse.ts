import {DateObject} from "react-multi-date-picker";

export type AttendanceResponse = {
    id: string;
    name: string;
    surname: string;
    isAbsent: boolean;
    isDelayed: boolean;
    delayTime: DateObject;
    delayNeedJustification: boolean;
    totalAbsence: number;
    totalDelay: number;
}
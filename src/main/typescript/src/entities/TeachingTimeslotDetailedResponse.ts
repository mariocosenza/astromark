import {DateObject} from "react-multi-date-picker";

export type TeachingTimeslotDetailedResponse = {
    id: number;
    hour: number;
    teacherId: string;
    name: string;
    surname: string;
    subject: string;
    signed: boolean;
    activityTitle: string;
    activityDescription: string;
    homeworkTitle: string;
    homeworkDescription: string;
    homeworkDueDate: DateObject;
}


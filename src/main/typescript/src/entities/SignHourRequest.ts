import {DateObject} from "react-multi-date-picker";

export type SignHourRequest = {
    slotId: number | null;
    hour: number | null;
    date: DateObject | null;
    activityTitle: string;
    activityDescription: string;
    homeworkTitle: string;
    homeworkDescription: string;
    homeworkDueDate: DateObject;
}
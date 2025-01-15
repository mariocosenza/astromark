import {DateObject} from "react-multi-date-picker";

export type SignHourRequest = {
    slotId: number | null;
    activityTitle: string;
    activityDescription: string;
    homeworkTitle: string;
    homeworkDescription: string;
    homeworkDueDate: DateObject;
}
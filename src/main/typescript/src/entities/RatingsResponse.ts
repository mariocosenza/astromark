import {DateObject} from "react-multi-date-picker";

export type RatingsResponse = {
    id: number;
    studentId: string;
    name: string;
    surname: string;
    subject: string;
    mark: number;
    type: string;
    description: string;
    date: DateObject;
}


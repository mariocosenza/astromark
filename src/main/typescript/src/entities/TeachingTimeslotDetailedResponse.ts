import {DateObject} from "react-multi-date-picker";

export type TeachingTimeslotDetailedResponse = {
    id: number | null;
    hour: number;
    subject: string;
    signed: boolean;
    teacher: SchoolUserResponse;
    activity: ClassActivityResponse | null;
    homework: HomeworkBriefResponse | null;
}

export type SchoolUserResponse = {
    id: string;
    name: string;
    surname: string;
}

export type ClassActivityResponse = {
    id: number;
    title: string;
    description: string;
}

export type HomeworkBriefResponse = {
    id: number;
    title: string;
    description: string;
    dueDate: DateObject;
    hasChat: boolean;
}
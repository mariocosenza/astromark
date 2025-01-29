import {DateObject} from "react-multi-date-picker";
import {createGlobalState} from "react-use";
import {ClassActivityResponse, HomeworkBriefResponse, TeachingTimeslotDetailedResponse} from "../entities/TeachingTimeslotDetailedResponse.ts";

export class SelectedTeaching {
    private static _teaching: string | null = localStorage.getItem("teaching") ? localStorage.getItem("teaching") as string : null;

    static get teaching(): string | null {
        return this._teaching;
    }

    static set teaching(value: string) {
        this._teaching = value;
        localStorage.setItem('teaching', value.toString());
    }
}

export const changeTeaching = createGlobalState<boolean>(false)

export class SelectedSchoolClass {
    private static _id: number | null = localStorage.getItem("schoolClassId") ? parseInt(localStorage.getItem("schoolClassId") as string) : null;

    static get id(): number | null {
        return this._id;
    }

    static set id(value: number) {
        this._id = value;
        localStorage.setItem('schoolClassId', value.toString());
    }

    private static _title: string | null = localStorage.getItem("schoolClassTitle") ? localStorage.getItem("schoolClassTitle") as string : null;

    static get title(): string | null {
        return this._title;
    }

    static set title(value: string) {
        this._title = value;
        localStorage.setItem('schoolClassTitle', value.toString());
    }

    private static _desc: string | null = localStorage.getItem("schoolClassDesc") ? localStorage.getItem("schoolClassDesc") as string : null;

    static get desc(): string | null {
        return this._desc;
    }

    static set desc(value: string) {
        this._desc = value;
        localStorage.setItem('schoolClassDesc', value.toString());
    }
}

export const isSelectedClass = () => {
    return SelectedSchoolClass.id !== null;
};

export class SelectedTeachingTimeslot {

    private static _id: number | null = null;

    static get id(): number | null {
        return this._id;
    }

    private static _hour: number | null = null;

    static get hour(): number | null {
        return this._hour;
    }

    private static _homework: HomeworkBriefResponse | null = null;

    static get homework(): HomeworkBriefResponse | null {
        return this._homework;
    }

    private static _activity: ClassActivityResponse | null = null;

    static get activity(): ClassActivityResponse | null {
        return this._activity;
    }

    private static _date: DateObject | null = null;

    static get date(): DateObject | null {
        return this._date;
    }

    static set date(value: DateObject | null) {
        this._date = value;
    }

    static setSlot(slot: TeachingTimeslotDetailedResponse) {
        this._id = slot.id
        this._hour = slot.hour
        this._activity = slot.activity
        this._homework = slot.homework
    }
}

export class SelectedHomework {
    private static _id: number | null = localStorage.getItem("homeworkId") ? parseInt(localStorage.getItem("homeworkId") as string) : null;

    static get id(): number | null {
        return this._id;
    }

    static set id(value: number) {
        this._id = value;
        localStorage.setItem('homeworkId', value.toString());
    }

    private static _title: string = localStorage.getItem("homeworkTitle") ? localStorage.getItem("homeworkTitle") as string : '';

    static get title(): string {
        return this._title;
    }

    static set title(value: string) {
        this._title = value;
        localStorage.setItem('homeworkTitle', value.toString());
    }

    private static _desc: string = localStorage.getItem("homeworkDesc") ? localStorage.getItem("homeworkDesc") as string : '';

    static get desc(): string {
        return this._desc;
    }

    static set desc(value: string) {
        this._desc = value;
        localStorage.setItem('homeworkDesc', value.toString());
    }
}
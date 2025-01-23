import {ClassAgendaRow} from "../pages/teacher/ClassAgenda.tsx";
import {DateObject} from "react-multi-date-picker";

export class SelectedSchoolClass {
    private static _id: number | null = localStorage.getItem("schoolClassId") ? parseInt(localStorage.getItem("schoolClassId") as string) : null;

    static get id(): number | null {
        return this._id;
    }

    static set id(value: number) {
        this._id = value;
        localStorage.setItem('schoolClassId', value.toString());
    }

    private static _title: string = localStorage.getItem("schoolClassTitle")  ? localStorage.getItem("schoolClassTitle") as string : '';

    static get title(): string {
        return this._title;
    }

    static set title(value: string) {
        this._title = value;
        localStorage.setItem('schoolClassTitle', value.toString());
    }

    private static _desc: string = localStorage.getItem("schoolClassDesc")  ? localStorage.getItem("schoolClassDesc") as string : '';

    static get desc(): string {
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

    static setSlot(slot: ClassAgendaRow) {
        this._id = slot.id
        this._hour = slot.hour
        this._activityTitle = slot.activityTitle;
        this._activityDesc = slot.activityDesc;
        this._homeworkTitle = slot.homeworkTitle;
        this._homeworkDesc = slot.homeworkDesc;
        this._homeworkDate = slot.homeworkDate;
    }

    private static _id: number | null = null;

    static get id(): number | null {
        return this._id;
    }

    private static _hour: number | null = null;

    static get hour(): number | null {
        return this._hour;
    }

    private static _activityTitle: string = '';

    static get activityTitle(): string {
        return this._activityTitle;
    }

    private static _activityDesc: string = '';

    static get activityDesc(): string {
        return this._activityDesc;
    }

    private static _homeworkTitle: string = '';

    static get homeworkTitle(): string {
        return this._homeworkTitle;
    }

    private static _homeworkDesc: string = '';

    static get homeworkDesc(): string {
        return this._homeworkDesc;
    }

    private static _homeworkDate: DateObject | null = null;

    static get homeworkDate(): DateObject | null {
        return this._homeworkDate;
    }

    private static _date: DateObject | null = null;

    static get date(): DateObject | null {
        return this._date;
    }

    static set date(value: DateObject | null) {
        this._date = value;
    }
}
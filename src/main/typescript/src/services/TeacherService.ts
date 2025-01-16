import {ClassAgendaRow} from "../pages/teacher/ClassAgenda.tsx";
import {DateObject} from "react-multi-date-picker";

export class SelectedSchoolClass {
    static get id(): number | null {
        return this._id;
    }

    static set id(value: number) {
        this._id = value;
    }

    static get title(): string {
        return this._title;
    }

    static set title(value: string) {
        this._title = value;
    }

    static get desc(): string {
        return this._desc;
    }

    static set desc(value: string) {
        this._desc = value;
    }

    private static _id: number | null = null;
    private static _title: string = '';
    private static _desc: string = '';
}

export const isSelectedClass = () => {
    return SelectedSchoolClass.id !== null;
};

export class SelectedTeachingTimeslot {

    static setSlot(slot: ClassAgendaRow) {
        this._id = slot.id
        this._activityTitle = slot.activityTitle;
        this._activityDesc = slot.activityDesc;
        this._homeworkTitle = slot.homeworkTitle;
        this._homeworkDesc = slot.homeworkDesc;
        this._homeworkDate = slot.homeworkDate;
    }

    static get id(): number | null {
        return this._id;
    }

    static get activityTitle(): string {
        return this._activityTitle;
    }

    static get activityDesc(): string {
        return this._activityDesc;
    }

    static get homeworkTitle(): string {
        return this._homeworkTitle;
    }

    static get homeworkDesc(): string {
        return this._homeworkDesc;
    }

    static get homeworkDate(): DateObject | null {
        return this._homeworkDate;
    }

    private static _id: number | null = null;
    private static _activityTitle: string = '';
    private static _activityDesc: string = '';
    private static _homeworkTitle: string = '';
    private static _homeworkDesc: string = '';
    private static _homeworkDate: DateObject | null = null;
}
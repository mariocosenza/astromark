export class SelectedSchoolClass {
    private static _id: number | null = null;

    static get id(): number | null {
        return this._id;
    }

    static set id(value: number) {
        this._id = value;
    }

    private static _title: string = '';

    static get title(): string {
        return this._title;
    }

    static set title(value: string) {
        this._title = value;
    }

    private static _desc: string = '';

    static get desc(): string {
        return this._desc;
    }

    static set desc(value: string) {
        this._desc = value;
    }
}

export const isSelectedClass = () => {
    return SelectedSchoolClass.id !== null;
};
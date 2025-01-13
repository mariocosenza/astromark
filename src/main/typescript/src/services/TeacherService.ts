
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
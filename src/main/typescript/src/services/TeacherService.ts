
export class SelectedSchoolClass {
    static get SchoolClassId(): number | null {
        return this._schoolClassId;
    }

    static set SchoolClassId(value: number) {
        this._schoolClassId = value;
    }

    private static _schoolClassId: number | null = null;
}

export const isSelectedClass = () => {
    return SelectedSchoolClass.SchoolClassId !== null;
};
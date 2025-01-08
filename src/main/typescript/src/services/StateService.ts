import {getId, getRole} from "./AuthService.ts";
import {Role} from "../components/route/ProtectedRoute.tsx";


export class SelectedYear {
    static get year(): number {
        return this._year;
    }

    static set year(value: number) {
        this._year = value;
    }
    private static _year = new Date().getMonth() > 9 ? new Date().getFullYear() : new Date().getFullYear() - 1;

}

export class SelectedClass {

}

export class SelectedStudent {
    static get id(): string | null {
        if(getRole() !== Role.STUDENT) {
            return this._id;
        } else {
            return getId()
        }
    }

    static set id(value: string) {
        this._id = value;
    }
    private static _id: string | null = null;
}

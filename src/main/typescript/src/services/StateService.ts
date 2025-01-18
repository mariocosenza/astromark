import {getId, getRole} from "./AuthService.ts";
import {Role} from "../components/route/ProtectedRoute.tsx";


export class SelectedYear {
    static get year(): number {
        return this._year as number;
    }

    static isNull() {
        return this._year === null;
    }

    static set year(value: number) {
        localStorage.setItem("year", value.toString());
        this._year = value;
    }
    private static _year : null | number = localStorage.getItem("year") ? parseInt(localStorage.getItem("year") as string) : null;

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
        localStorage.setItem("studentId", value);
        this._id = value;
    }
    private static _id: string | null = localStorage.getItem("studentId") ? localStorage.getItem("studentId") : null;
}

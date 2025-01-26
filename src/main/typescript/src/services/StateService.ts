import {getId, getRole} from "./AuthService.ts";
import {Role} from "../components/route/ProtectedRoute.tsx";
import {createGlobalState} from "react-use";


export class SelectedYear {
    private static _year: null | number = localStorage.getItem("year") ? parseInt(localStorage.getItem("year") as string) : 2024;

    static get year(): number {
        return this._year as number;
    }

    static set year(value: number) {
        localStorage.setItem("year", value.toString());
        this._year = value;
    }

    static isNull() {
        return this._year === null;
    }

}

export const changeStudentOrYear = createGlobalState<boolean>(false)

export class SelectedStudent {
    private static _id: string | null = localStorage.getItem("studentId") ? localStorage.getItem("studentId") : null;

    static get id(): string | null {
        if (getRole() !== Role.STUDENT) {
            return this._id;
        } else {
            return getId()
        }
    }

    static set id(value: string) {
        localStorage.setItem("studentId", value);
        this._id = value;
    }
}

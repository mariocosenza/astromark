import axiosConfig from "./AxiosConfig.ts";
import {Env} from "../Env.ts";
import {SelectedStudent} from "./StateService.ts";
import {AxiosResponse} from "axios";
import {getId, getRole} from "./AuthService.ts";
import {Role} from "../components/route/ProtectedRoute.tsx";

export async function getStudentYears() {
    if(SelectedStudent.id !== null) {
        const response : AxiosResponse<number[]> = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/years`)

       return response.data
    } else if(getRole().toUpperCase() === Role.STUDENT) {
        SelectedStudent.id = getId();
        return getStudentYears();
    } else {
        return null;
    }
}


export const getLatestStudentYear  = getStudentYears().then(function(result) {
    if(result !== null) {
        return result[0]
    } else {
        return new Date().getFullYear();
    }
});

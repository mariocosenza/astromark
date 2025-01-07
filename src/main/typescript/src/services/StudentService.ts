import {AxiosResponse} from "axios";
import axiosConfig from "./AxiosConfig.ts";
import {Env} from "../Env.ts";
import {getId} from "./AuthService.ts";

export const getStudentYears: AxiosResponse<number[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${getId()}/years`);
export const getLatestStudentYear = getStudentYears.data[0];
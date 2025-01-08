import React, {useEffect} from "react";
import {DashboardNavbar} from "../../components/DashboardNavbar.tsx";
import axiosConfig from "../../services/AxiosConfig.ts";
import {SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";
import {AccordionViewable} from "../../components/AccordionViewable.tsx";
import {SchoolClass} from "./Communication.tsx";

export type NoteDto = {
    id: string,
    date: Date,
    description: string,
    viewed: boolean,
}

function view(id: string) {
    axiosConfig.patch(Env.API_BASE_URL + '/students/' + SelectedStudent.id + '/notes' + '/' + id)
}

export const Note : React.FC = () => {

    const [loading, setLoading] = React.useState<boolean>(true);
    const [notes, setNotes] = React.useState<NoteDto[]>([]);

    useEffect(() => {
        fetchData()
    }, []);

    const fetchData = async () => {
        try {
            const schoolClass: AxiosResponse<SchoolClass[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`);
            const response: AxiosResponse<NoteDto[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/notes/${schoolClass.data[0].id}`);
            setNotes(response.data);
            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }


    return (
        <div>
            <DashboardNavbar/>
            <div className={'alert'}>
                {
                    loading ? <div>Loading...</div> : notes.map((noteDto: NoteDto, index: number) => {
                        return <AccordionViewable key={index} date={noteDto.date} id={noteDto.id} title={'Nota'} description={noteDto.description} avatar={'N'} viewed={noteDto.viewed} view={() => view(noteDto.id)}/>
                    })
                }
            </div>
        </div>
    );
}
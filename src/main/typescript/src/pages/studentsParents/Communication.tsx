import React, {useEffect} from "react";
import {AccordionNotViewable} from "../../components/AccordionNotViewable.tsx";
import axiosConfig from "../../services/AxiosConfig.ts";
import {SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {Env} from "../../Env.ts";
import {AxiosResponse} from "axios";

export type Communication = {
    id: number,
    title: string,
    date: Date,
    description: string
}

export type SchoolClass = {
    id: number,
    year: number,
    letter: string,
    number: number
}

export const Allert : React.FC = () => {
    const [allerts, setAllerts] = React.useState<Communication[]>([]);
    const [loading, setLoading] = React.useState<boolean>(true);

    useEffect(() => {
        fetchData()
    }, []);

    const fetchData = async () => {
        try {
            const schoolClass: AxiosResponse<SchoolClass[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`);
            const response: AxiosResponse<Communication[]>  = await axiosConfig.get(`${Env.API_BASE_URL}/schoolClasses/${schoolClass.data[0].id}/communications`);
            setAllerts(response.data);

            setLoading(false);
        } catch (error) {
            console.error(error);
        }
    }


    return (
        <div>
            <div className={'alert'}>
                {
                    loading ? <div>Loading...</div> : allerts.map((allert: Communication, index: number) => {
                        return <AccordionNotViewable key={index} date={allert.date} id={allert.id} title={allert.title} description={allert.description} avatar={'A'}/>
                    })
                }
            </div>
        </div>
    );
}
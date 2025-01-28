export type SignHourRequest = {
    id: number | null;
    hour: number | null;
    subject: string | null;
    date: Date;
    activity: ClassActivityRequest | null;
    homework: HomeworkRequest | null;
}

export type ClassActivityRequest = {
    id: number | null;
    title: string;
    description: string;
}

export type HomeworkRequest = {
    id: number | null;
    title: string;
    description: string;
    dueDate: Date;
    hasChat: boolean;
}
export type TicketResponse = {
    id: string;
    title: string;
    datetime: Date;
    category: string;
    closed: boolean;
    solved: boolean;
    isTeacher: boolean;
}
export type MarkRequest = {
    studentId: string;
    teachingId: {
        teacherId: string;
        subjectTitle: string;
    }
    date: Date;
    description: string;
    mark: number;
    type: string;
}

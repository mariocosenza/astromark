export type AttendanceRequest = {
    studentId: string
    isAbsent: boolean,
    isDelayed: boolean,
    delayTimeHour: number,
    delayTimeMinute: number,
    delayNeedJustification: boolean,
}
export type ProtectedRoutePathProps = {
    role: Role
}

export enum Role {
    STUDENT = "STUDENT",
    TEACHER = "TEACHER",
    SECRETARY = "SECRETARY",
    PARENT = "PARENT"
}
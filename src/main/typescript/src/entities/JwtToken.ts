export type JwtToken = {
    role: string,
    sub: string,
    iat: number,
    exp: number
};
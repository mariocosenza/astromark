import * as yup from 'yup'
import YupPassword from 'yup-password'
YupPassword(yup) // extend yup


export const FirstLogin = () => {
    return <h1>First Login</h1>
}

import {createTheme, Theme} from "@mui/material";

export const theme: Theme = createTheme({
    typography: {
        fontFamily: [
            'Titillium Web',
            'Arial',
            'sans-serif'
        ].join(','),
    }
});
import {createTheme, Theme} from "@mui/material";

export const theme: Theme = createTheme({
    palette: {
        mode: 'light',
        primary: {
            main: '#004F9C',
        },
        secondary: {
            main: '#006879',
        },
        error: {
            main: '#A1101E',
        },
    },
    typography: {
        fontFamily: ['Titillium Web', 'Arial', 'sans-serif'].join(','),
    },
    shape: {
        borderRadius: 6,
    },
    components: {
        MuiAppBar: {
            styleOverrides: {
                colorInherit: {
                    backgroundColor: '#11294E',
                    color: '#fff',
                },
            },
            defaultProps: {
                color: 'inherit',
            },
        },
        MuiTooltip: {
            defaultProps: {
                arrow: true,
            },
        },
    },
});
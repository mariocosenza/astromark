import {styled, tableCellClasses} from "@mui/material";
import TableRow from "@mui/material/TableRow";
import TableCell from "@mui/material/TableCell";

export const CustomTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
    },
}));

export const CustomTableCell = styled(TableCell)(() => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: 'var(--md-sys-color-primary)',
        color: 'white',
        borderColor: 'black',
        textAlign: 'center'
    },

    border: '1px solid gray',
    fontSize: '1.1rem',
}));
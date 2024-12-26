import ReactDOM from "react-dom/client";
import {BrowserRouter} from "react-router";
import './index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme.ts";
import {App} from "./App.tsx";

const root: HTMLElement = document.getElementById("root") as HTMLElement;

ReactDOM.createRoot(root).render(
    <ThemeProvider theme={theme}>
        <BrowserRouter>
                <App/>
        </BrowserRouter>
    </ThemeProvider>
)

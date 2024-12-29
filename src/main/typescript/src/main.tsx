import ReactDOM from "react-dom/client";
import {BrowserRouter, Route, Routes} from "react-router";
import './styles/index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme";
import {App} from "./pages/App";
import {Login} from "./pages/Login.tsx";
import {StrictMode} from "react";

const root: HTMLElement = document.getElementById("root") as HTMLElement;

ReactDOM.createRoot(root).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App />} />
                    <Route path="/login" element={<Login/>}/>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>
)

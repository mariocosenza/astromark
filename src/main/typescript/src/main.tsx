import ReactDOM from "react-dom/client";
import {BrowserRouter, Route, Routes} from "react-router";
import './styles/index.css'
import {ThemeProvider} from "@mui/material";
import {theme} from "./theme/GlobalTheme";
import {App} from "./pages/App";
import {Login} from "./pages/Login.tsx";
import {FirstLogin} from "./pages/FirstLogin.tsx";
import {ProtectedRoutePath, Role} from "./components/route/ProtectedRoute.tsx";
import {StrictMode} from "react";

const root: HTMLElement = document.getElementById("root") as HTMLElement;

ReactDOM.createRoot(root).render(
    <StrictMode>
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App />} />
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/first-login" element={<FirstLogin/>}/>
                    <Route path="/student" element={<ProtectedRoutePath role={Role.STUDENT}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                    <Route path="/teacher" element={<ProtectedRoutePath role={Role.TEACHER}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                    <Route path="/secretary" element={<ProtectedRoutePath role={Role.SECRETARY}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                    <Route path="/parent" element={<ProtectedRoutePath role={Role.PARENT}/>}>
                        <Route path="dashboard" element={<h1>Dashboard</h1>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </ThemeProvider>
    </StrictMode>
)

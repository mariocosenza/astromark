import {Box, Divider, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import ExploreIcon from '@mui/icons-material/ExploreOutlined';
import HouseIcon from '@mui/icons-material/House';
import ManageAccountsOutlinedIcon from '@mui/icons-material/ManageAccountsOutlined';
import DensityMediumOutlinedIcon from '@mui/icons-material/DensityMediumOutlined';
import ArticleIcon from '@mui/icons-material/Article';
import HistoryEduIcon from '@mui/icons-material/HistoryEdu';
import {useState} from "react";
import {NavLink} from "react-router";
import {isSelectedClass} from "../services/TeacherService.ts";


export const TeacherSideNav = () => {
    const [open, setOpen] = useState(false);

    const toggleDrawer = (newOpen: boolean) => () => {
        setOpen(newOpen);
    };

    const DrawerList = (

        <Box className={"drawer"} sx={{width: '15vw'}} minWidth={250} role="presentation" onClick={toggleDrawer(false)}>
            <List>
                <NavLink to="/teacher/dashboard" end>
                    <ListItem key="dashboard" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ExploreIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Dashboard"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to="/teacher/classi" end>
                    <ListItem key="classi" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <HouseIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Classi"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to="/teacher/ticket" end>
                    <ListItem key="ticket" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ArticleIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Ticket"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>

                {isSelectedClass() && (
                    <NavLink to="/teacher/agenda" end>
                        <ListItem key="agenda" disablePadding>
                            <ListItemButton>
                                <ListItemIcon>
                                    <HistoryEduIcon/>
                                </ListItemIcon>
                                <ListItemText primary={"Agenda di Classe"}/>
                            </ListItemButton>
                        </ListItem>
                    </NavLink>
                )}

                <Divider/>
                <NavLink to={"/teacher/impostazioni"} end>
                    <ListItem key="impostazioni" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ManageAccountsOutlinedIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Impostazioni"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
            </List>
        </Box>
    )

    return (
        <div>
            <DensityMediumOutlinedIcon onClick={toggleDrawer(true)}>Open drawer</DensityMediumOutlinedIcon>
            <Drawer open={open} onClose={toggleDrawer(false)}>
                {DrawerList}
            </Drawer>
        </div>
    );
}

import {NavLink} from "react-router";
import {Box, Divider, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText,} from "@mui/material";
import {getRole} from "../services/AuthService.ts";
import {createGlobalState} from "react-use";
import ExploreIcon from "@mui/icons-material/ExploreOutlined";
import ScoreOutlinedIcon from "@mui/icons-material/ScoreOutlined";
import AssignmentTurnedInOutlinedIcon from "@mui/icons-material/AssignmentTurnedInOutlined";
import ReceiptLongOutlinedIcon from "@mui/icons-material/ReceiptLongOutlined";
import DangerousOutlinedIcon from "@mui/icons-material/DangerousOutlined";
import ManageAccountsOutlinedIcon from "@mui/icons-material/ManageAccountsOutlined";
import DensityMediumOutlinedIcon from "@mui/icons-material/DensityMediumOutlined";
import ConfirmationNumberOutlinedIcon from "@mui/icons-material/ConfirmationNumberOutlined";

export const openBar = createGlobalState<boolean>(false)

export const SecretarySideNav = () => {
    const [open, setOpen] = openBar();

    const toggleDrawer = (newOpen: boolean) => () => {
        setOpen(newOpen);
    };

    const role = getRole().toLowerCase();

    const DrawerList = (

        <Box className={"drawer"} sx={{width: '15vw'}} minWidth={250} role="presentation" onClick={toggleDrawer(true)}>
            <List>
                <NavLink to={`/${role}/dashboard`} end>
                    <ListItem key="dashboard" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ExploreIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Dashboard"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/${role}/manage-class`} end>
                    <ListItem key="manage-class" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ScoreOutlinedIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Gestione classi"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/${role}/add-class`} end>
                    <ListItem key="add-class" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <AssignmentTurnedInOutlinedIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Crea classe"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/${role}/teacher`} end>
                    <ListItem key="teacher" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ReceiptLongOutlinedIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Gestione docenti"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/${role}/timetable`} end>
                    <ListItem key="timetable" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <DangerousOutlinedIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Gestione orario"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/secretary/ticket`} end>
                    <ListItem key="ticket" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ConfirmationNumberOutlinedIcon/>
                            </ListItemIcon>
                            <ListItemText primary={"Ticket"}/>
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/${role}/impostazioni`} end style={{position: 'fixed', bottom: 0, width: '15vw'}}>
                    <Divider/>
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
            <DensityMediumOutlinedIcon onClick={toggleDrawer(true)}>Apri drawer</DensityMediumOutlinedIcon>
            <Drawer open={open} onClose={toggleDrawer(false)}>
                {DrawerList}
            </Drawer>
        </div>
    );
}
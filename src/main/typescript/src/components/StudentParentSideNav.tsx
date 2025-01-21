import {Box, Divider, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import ExploreIcon from '@mui/icons-material/ExploreOutlined';
import ScoreOutlinedIcon from '@mui/icons-material/ScoreOutlined';
import AssignmentTurnedInOutlinedIcon from '@mui/icons-material/AssignmentTurnedInOutlined';
import ReceiptLongOutlinedIcon from '@mui/icons-material/ReceiptLongOutlined';
import DangerousOutlinedIcon from '@mui/icons-material/DangerousOutlined';
import SummarizeOutlinedIcon from '@mui/icons-material/SummarizeOutlined';
import CalendarMonthOutlinedIcon from '@mui/icons-material/CalendarMonthOutlined';
import AddAlertOutlinedIcon from '@mui/icons-material/AddAlertOutlined';
import ManageAccountsOutlinedIcon from '@mui/icons-material/ManageAccountsOutlined';
import DensityMediumOutlinedIcon from '@mui/icons-material/DensityMediumOutlined';
import ConfirmationNumberOutlinedIcon from '@mui/icons-material/ConfirmationNumberOutlined';
import GroupsIcon from '@mui/icons-material/Groups';
import {NavLink} from "react-router";
import {getRole} from "../services/AuthService.ts";
import {createGlobalState} from "react-use";

export const openBar = createGlobalState<boolean>(false)

export const StudentParentSideNav = () => {
    const [open, setOpen] = openBar();

    const toggleDrawer = (newOpen: boolean) => () => {
        setOpen(newOpen);
    };

    const role= getRole().toLowerCase();

    const DrawerList = (

        <Box className={"drawer"} sx={{ width: '15vw' }} minWidth={250} role="presentation" onClick={toggleDrawer(true)}>
            <List>
                <NavLink to={`/${role}/dashboard`} end>
                    <ListItem key="dashboard" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ExploreIcon />
                            </ListItemIcon>
                            <ListItemText primary={"Dashboard"} />
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to={`/${role}/assenze`} end>
                <ListItem key="assenze" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <ScoreOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Assenze"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={`/${role}/assegno`} end>
                <ListItem key="assegno" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <AssignmentTurnedInOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Attività classe"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={`/${role}/voti`} end>
                <ListItem key="Voti" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <ReceiptLongOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Voti"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={`/${role}/note`} end>
                <ListItem key="note" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <DangerousOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Note"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={`/${role}/pagella`} end>
                <ListItem key="pagella" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <SummarizeOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Pagella"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={`/${role}/orario`} end>
                <ListItem key="orario" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <CalendarMonthOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Orario"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={`/${role}/avvisi`} end>
                <ListItem key="avvisi" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <AddAlertOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Avvisi"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>

                {
                    role === "parent" && <NavLink to={`/parent/ricevimento`}>
                        <Divider/>
                        <ListItem key="reception" disablePadding>
                            <ListItemButton>
                                <ListItemIcon>
                                    <GroupsIcon />
                                </ListItemIcon>
                                <ListItemText primary={"Ricevimento"} />
                            </ListItemButton>
                        </ListItem>
                    </NavLink>
                }
                {
                    role === "parent" &&
                    <NavLink to={`/parent/ticket`} end>
                        <ListItem key="ticket" disablePadding>
                            <ListItemButton>
                                <ListItemIcon>
                                    <ConfirmationNumberOutlinedIcon />
                                </ListItemIcon>
                                <ListItemText primary={"Ticket"} />
                            </ListItemButton>
                        </ListItem>
                    </NavLink>
                }

                <NavLink to={`/${role}/impostazioni`} end style={{position: 'fixed', bottom: 0, width: '15vw'}}>
                    <Divider/>
                    <ListItem key="impostazioni" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ManageAccountsOutlinedIcon />
                            </ListItemIcon>
                            <ListItemText primary={"Impostazioni"} />
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

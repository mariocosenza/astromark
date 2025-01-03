import {Box, Button, Divider, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import ExploreIcon from '@mui/icons-material/ExploreOutlined';
import ScoreOutlinedIcon from '@mui/icons-material/ScoreOutlined';
import AssignmentTurnedInOutlinedIcon from '@mui/icons-material/AssignmentTurnedInOutlined';
import ReceiptLongOutlinedIcon from '@mui/icons-material/ReceiptLongOutlined';
import DangerousOutlinedIcon from '@mui/icons-material/DangerousOutlined';
import SummarizeOutlinedIcon from '@mui/icons-material/SummarizeOutlined';
import CalendarMonthOutlinedIcon from '@mui/icons-material/CalendarMonthOutlined';
import AddAlertOutlinedIcon from '@mui/icons-material/AddAlertOutlined';
import ManageAccountsOutlinedIcon from '@mui/icons-material/ManageAccountsOutlined';
import {useState} from "react";
import {NavLink} from "react-router";


export const StudentParentSideNav = () => {
    const [open, setOpen] = useState(false);

    const toggleDrawer = (newOpen: boolean) => () => {
        setOpen(newOpen);
    };

    const DrawerList = (

        <Box className={"drawer"} sx={{ width: '15vw' }} minWidth={250} role="presentation" onClick={toggleDrawer(false)}>
            <List>
                <NavLink to="/student/dashboard" end>
                    <ListItem key="dashboard" disablePadding>
                        <ListItemButton>
                            <ListItemIcon>
                                <ExploreIcon />
                            </ListItemIcon>
                            <ListItemText primary={"Dashboard"} />
                        </ListItemButton>
                    </ListItem>
                </NavLink>
                <NavLink to="/student/assenze" end>
                <ListItem key="assenze" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <ScoreOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Assenze"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to="/student/assegno" end>
                <ListItem key="assegno" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <AssignmentTurnedInOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Attività classe"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to="/student/voti" end>
                <ListItem key="Voti" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <ReceiptLongOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Voti"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to="/student/note" end>
                <ListItem key="note" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <DangerousOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Note"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to="/student/pagella" end>
                <ListItem key="pagella" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <SummarizeOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Pagella"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to="/student/orario" end>
                <ListItem key="orario" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <CalendarMonthOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Orario"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <NavLink to={"/student/avvisi"} end>
                <ListItem key="avvisi" disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <AddAlertOutlinedIcon />
                        </ListItemIcon>
                        <ListItemText primary={"Avvisi"} />
                    </ListItemButton>
                </ListItem>
                </NavLink>
                <Divider/>
                <NavLink to={"/student/impostazioni"} end>
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
            <Button onClick={toggleDrawer(true)}>Open drawer</Button>
            <Drawer open={open} onClose={toggleDrawer(false)}>
                {DrawerList}
            </Drawer>
        </div>
    );
}

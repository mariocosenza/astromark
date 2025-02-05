import React, {useEffect, useState} from 'react';
import { decode } from "he";
import {Box, CircularProgress, Paper, Stack, Typography} from '@mui/material';
import {blue, red} from '@mui/material/colors';
import {changeStudentOrYear, SelectedStudent, SelectedYear} from "../../services/StateService.ts";
import {AxiosResponse} from "axios";
import axiosConfig from "../../services/AxiosConfig.ts";
import {Env} from "../../Env.ts";
import {SchoolUserDetail} from "../../components/AccountMenu.tsx";
import {Communication, SchoolClass} from "./Communication.tsx";
import {AccordionNotViewable} from "../../components/AccordionNotViewable.tsx";
import {isRole} from "../../services/AuthService.ts";
import {Role} from "../../components/route/ProtectedRoute.tsx";
import {getStudentYears} from "../../services/StudentService.ts";
import {HomeworkResponse, openChat} from "./ClassActivity.tsx";
import {HomeworkList} from "../../components/HomeworkList.tsx";

// react-use hook for responsive behavior
import {useMedia} from 'react-use';

export const Dashboard: React.FC = () => {
    // This hook checks if the screen size is below 768 px (mobile view)
    const isMobile = useMedia('(max-width: 768px)');

    const [allerts, setAllerts] = useState<Communication[]>([]);
    const [average, setAverage] = useState<number>(0);
    const [attitude, setAttitude] = useState<string>('Prossimamente');
    const [user, setUser] = useState<SchoolUserDetail>();
    const [totAbsences, setTotAbsences] = useState<number>(0);
    const [loading, setLoading] = useState<boolean>(true);
    const [activity, setActivity] = useState<HomeworkResponse>();
    const [, setOpen] = openChat();
    const [toggle, _] = changeStudentOrYear();
    const [firstFetch, setFirstFetch] = useState<boolean>(true);

    useEffect(() => {
        fetchData();
    }, [toggle]);

    const fetchData = async () => {
        try {
            // If the user is a parent, fetch the first student in the list
            if (!isRole(Role.STUDENT)) {
                await axiosConfig.get<SchoolUserDetail[]>(Env.API_BASE_URL + '/parents/students')
                    .then((response) => {
                        if (firstFetch) {
                            SelectedStudent.id = response.data[0].id;
                        }
                        getStudentYears().then((response) => {
                            if (response !== null) {
                                SelectedYear.year = response[0];
                            }
                        });
                    });
                setFirstFetch(false);
                setOpen(false);
            }
        } catch (error) {
            console.error(error);
        }

        try {
            // Fetch absence data
            const responseAbsences: AxiosResponse<number> =
                await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/year/${SelectedYear.year}/justifiable/absences/total`);
            // Fetch user data
            const responseUser: AxiosResponse<SchoolUserDetail> =
                await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}`);

            // Fetch average marks
            const averageResponse: AxiosResponse<number> =
                await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/marks/${SelectedYear.year}/averages`);
            // Fetch the student's class
            const schoolClass: AxiosResponse<SchoolClass[]> =
                await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/classes/${SelectedYear.year}`);
            // Fetch class communications
            const response: AxiosResponse<Communication[]> =
                await axiosConfig.get(`${Env.API_BASE_URL}/schoolClasses/${schoolClass.data[0].id}/communications`);
            // Fetch all homework
            const responseHomework: AxiosResponse<HomeworkResponse[]> =
                await axiosConfig.get(`${Env.API_BASE_URL}/classwork/${schoolClass.data[0].id}/homeworks/all`);

            setActivity(
                responseHomework.data !== null ? responseHomework.data[0] : undefined
            );
            setAllerts(response.data);
            setAverage(averageResponse.data);
            setUser(responseUser.data);
            setTotAbsences(responseAbsences.data);
            setLoading(false);
            // Fetch attitude
            const responseAttitude: AxiosResponse<string> =
                await axiosConfig.get(`${Env.API_BASE_URL}/students/${SelectedStudent.id}/attitude`);
            setAttitude(decode(responseAttitude.data));
        } catch (error) {
            console.error(error);
        }
    };

    return (
        // Outer container using Box for responsiveness
        <Box
            sx={{
                overflowX: 'hidden',
                overflowY: 'hidden',
                paddingBottom: '2rem',
                width: '100vw',
                paddingX: '5vw',
                boxSizing: 'border-box',
                marginTop: '2rem',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
            }}
        >
            {/* Main Stack that wraps content */}
            <Stack
                direction="column"
                spacing={2}
                width="90vw"
                flexGrow={1}
                justifyContent="center"
                alignContent="center"
            >
                {/* First row: two Papers; direction changes based on isMobile */}
                <Stack
                    // If mobile, use column; otherwise row
                    direction={isMobile ? 'column' : 'row'}
                    spacing={2}
                    minHeight="20vh"
                    width="100%"
                >
                    <Paper
                        elevation={3}
                        sx={{
                            // On mobile, set width to 100%, otherwise 33%
                            width: isMobile ? '90%' : '33%',
                            minHeight: '100%',
                            bgcolor: '#006879FF',
                            alignContent: 'center',
                            borderRadius: '1.5rem',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                            marginBottom: isMobile ? '1rem' : 0, // add spacing on mobile
                        }}
                    >
                        <Typography variant="h3" textAlign="center" color="white">
                            {(user?.name === undefined ? "" : user.name) + " " + (user?.surname === undefined ? "" : user.surname)}
                        </Typography>
                    </Paper>
                    <Paper
                        elevation={3}
                        sx={{
                            width: isMobile ? '90%' : '33%',
                            minHeight: '100%',
                            bgcolor: '#904A47FF',
                            alignContent: 'center',
                            borderRadius: '1.5rem',
                            display: 'flex',
                            justifyContent: 'center',
                            alignItems: 'center',
                        }}
                    >
                        <Typography variant="h3" textAlign="center" color="white">
                            {"Assenze: " + totAbsences}
                        </Typography>
                    </Paper>
                </Stack>

                {/* Container for the SVG background with overlayed HomeworkList */}
                <Box
                    sx={{
                        position: 'relative',  // Needed to position absolutely inside
                        width: '100%',
                        height: '30vh',
                        marginTop: isMobile ? '1rem' : '2rem',
                    }}
                >
                    {/* SVG as background */}
                    <Box
                        component="svg"
                        sx={{
                            position: 'absolute',
                            top: 0,
                            left: 0,
                            zIndex: 0,
                            width: '100%',
                            height: '100%',
                            display: 'block',
                        }}
                        viewBox="0 0 1110 257"
                        preserveAspectRatio="none"
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                    >
                        <g filter="url(#filter0_d_78_8847)">
                            <path
                                d="M1085 248H25C13.9543 248 5 239.046 5 228V21C5 9.95431 13.9543 1 25 1H159.946C168.108 1 174.726 7.6172 174.726 15.7799C174.726 23.9426 181.343 30.5598 189.506 30.5598H1085C1096.05 30.5598 1105 39.5142 1105 50.5599V228C1105 239.046 1096.05 248 1085 248Z"
                                fill="#E4E3D3"
                                shapeRendering="crispEdges"
                            />
                            <path
                                d="M1085 248H25C13.9543 248 5 239.046 5 228V21C5 9.95431 13.9543 1 25 1H159.946C168.108 1 174.726 7.6172 174.726 15.7799C174.726 23.9426 181.343 30.5598 189.506 30.5598H1085C1096.05 30.5598 1105 39.5142 1105 50.5599V228C1105 239.046 1096.05 248 1085 248Z"
                                stroke="black"
                                strokeOpacity="0.5"
                                shapeRendering="crispEdges"
                            />
                        </g>
                        <defs>
                            <filter
                                id="filter0_d_78_8847"
                                x="0.5"
                                y="0.5"
                                width="1109"
                                height="256"
                                filterUnits="userSpaceOnUse"
                                colorInterpolationFilters="sRGB"
                            >
                                <feFlood floodOpacity="0" result="BackgroundImageFix"/>
                                <feColorMatrix
                                    in="SourceAlpha"
                                    type="matrix"
                                    values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0"
                                    result="hardAlpha"
                                />
                                <feOffset dy="4"/>
                                <feGaussianBlur stdDeviation="2"/>
                                <feComposite in2="hardAlpha" operator="out"/>
                                <feColorMatrix
                                    type="matrix"
                                    values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0"
                                />
                                <feBlend
                                    mode="normal"
                                    in2="BackgroundImageFix"
                                    result="effect1_dropShadow_78_8847"
                                />
                                <feBlend
                                    mode="normal"
                                    in="SourceGraphic"
                                    in2="effect1_dropShadow_78_8847"
                                    result="shape"
                                />
                            </filter>
                        </defs>
                    </Box>

                    {/* HomeworkList overlay */}
                    <Box
                        sx={{
                            position: 'relative', // Stays above the absolute SVG
                            zIndex: 1,
                            width: '100%',
                            height: '100%',
                            overflow: 'auto',     // scroll if needed
                            padding: '1rem',      // internal padding
                        }}
                    >
                        <Typography variant="h4" textAlign="left" marginLeft={'2vw'} color={blue[900]}>
                            Compiti
                        </Typography>
                        <div style={{maxWidth: '80%', marginTop: '2%', alignContent: 'center', marginLeft: '1vw'}}>
                            <HomeworkList
                                list={(activity !== undefined ? [activity] : []).map(
                                    (activity: HomeworkResponse) => ({
                                        id: activity.id,
                                        chat: activity.chat,
                                        avatar: 'A',
                                        title: activity.signedHour.title,
                                        description: activity.title + ': ' + activity.description,
                                        hexColor: 'dodgerblue',
                                        date: activity.signedHour.date,
                                    })
                                )} dashboard={true}/>
                        </div>
                    </Box>
                </Box>

                {/* Second row: three Papers; direction changes based on isMobile */}
                <Stack
                    direction={isMobile ? 'column' : 'row'}
                    spacing={'2vw'}
                    minHeight="25vh"
                    width="100%"
                >
                    <Paper
                        elevation={3}
                        sx={{
                            width: isMobile ? '100%' : '33%',
                            minHeight: '100%',
                            bgcolor: '#F3F3FAFF',
                            textAlign: 'center',
                            borderRadius: '1rem',
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',

                            marginBottom: isMobile ? '1rem' : 0, // spacing on mobile
                            padding: '1rem',
                        }}
                    >
                        <Typography variant="h3" textAlign="center" mt="1rem" mb="1rem" color={blue[800]}>
                            Media
                        </Typography>
                        <CircularProgress variant="determinate" value={average * 10} size={50}/>
                        <Typography variant="h5" fontStyle={'italic'} mt="1rem">{average}</Typography>
                    </Paper>

                    <Paper
                        elevation={3}
                        sx={{
                            width: isMobile ? '100%' : '33%',
                            minHeight: '100%',
                            textAlign: 'center',
                            bgcolor: '#D7E2FF82',
                            borderRadius: '1rem',
                            display: 'flex',
                            flexDirection: 'column',
                            justifyContent: 'start',
                            alignItems: 'center',
                            marginBottom: isMobile ? '1rem' : 0,
                            padding: '1rem',
                        }}
                    >
                        <Typography variant="h3" textAlign="center" mt="1rem" mb="1rem" color={red[800]}>
                            Avvisi
                        </Typography>
                        <div style={{maxWidth: '80%', marginLeft: isMobile ? '5%' : '10%', marginRight: '10%'}}>
                            {!loading && allerts.length > 0 && (
                                <AccordionNotViewable
                                    key={allerts[0].id}
                                    date={allerts[0].date}
                                    id={allerts[0].id}
                                    title={allerts[0].title}
                                    description={allerts[0].description}
                                    avatar="A"
                                />
                            )}
                        </div>
                    </Paper>

                    <Paper
                        elevation={3}
                        sx={{
                            width: isMobile ? '100%' : '33%',
                            minHeight: '100%',
                            bgcolor: '#F3F3FAFF',
                            borderRadius: '1rem',
                            display: 'flex',
                            flexDirection: 'column',
                            justifyContent: 'start',
                            alignItems: 'flex-start',
                            padding: '1rem',
                        }}
                    >
                        <Typography variant="h3" textAlign="center" mt="1rem" mb="1rem" color={blue[800]}>
                            Orientamento
                        </Typography>
                        <Typography variant="body1" sx={{ml: '2%'}} textAlign={'justify'} style={{overflowY: 'auto'}}>
                            {attitude}
                        </Typography>
                    </Paper>
                </Stack>
            </Stack>
        </Box>
    );
};

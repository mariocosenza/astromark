import {HomePageNavbar} from "../components/HomePageNavbar.tsx";
import {
    Container,
    Typography,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Box,
    useTheme,
    Link,
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

type FAQ = {
    question: string;
    answer: string;
};

type UserType = {
    title: string;
    faqs: FAQ[];
};

// Email di supporto
const supportEmail = 'supporto@astromark.it';

// Dati delle FAQ organizzate per tipologia di utente
const faqData: UserType[] = [
    {
        title: 'Utente non loggato',
        faqs: [
            {
                question: 'Come posso accedere al sistema?',
                answer: 'Puoi accedere al sistema cliccando sulla pagina di login presente nella home page del sito.',
            },
            {
                question: 'Di cosa tratta AstroMark?',
                answer: 'AstroMark è una piattaforma di gestione della didattica che offre diverse funzionalità per studenti, genitori, professori e personale amministrativo.',
            },
            {
                question: 'Come posso contattare l’assistenza?',
                answer: `Se hai bisogno di assistenza, puoi contattare il nostro supporto via email all'indirizzo ${supportEmail}.`,
            },
        ],
    },
    {
        title: 'Utente registrato',
        faqs: [
            {
                question: 'Come posso recuperare la mia password?',
                answer: 'Se hai dimenticato la tua password, puoi utilizzare la funzione di recupero password disponibile nella pagina di login.',
            },
            {
                question: 'Come posso aggiornare i miei dati personali?',
                answer: 'Puoi modificare i tuoi dati di contatto e la password di accesso direttamente dalla tua dashboard personale.',
            },
            {
                question: 'Posso modificare i miei dati anagrafici?',
                answer: 'Sì, puoi visualizzare e modificare i tuoi dati anagrafici come nome, cognome, indirizzo di residenza e altro dalla sezione dedicata nel tuo profilo.',
            },
        ],
    },
    {
        title: 'Genitore',
        faqs: [
            {
                question: 'Come posso prenotare un ricevimento con i professori dei miei figli?',
                answer: 'Puoi prenotare un ricevimento tramite la sezione dedicata ai genitori nel tuo account, selezionando la classe e il professore desiderato.',
            },
            {
                question: 'Come posso giustificare assenze e ritardi dei miei figli?',
                answer: 'Nella sezione apposita, puoi giustificare le assenze e i ritardi dei tuoi figli inserendo le motivazioni necessarie.',
            },
            {
                question: 'Come posso comunicare con la segreteria?',
                answer: 'Puoi creare un ticket nella sezione dedicata alla segreteria e interagire con i nostri operatori tramite messaggi testuali.',
            },
        ],
    },
    {
        title: 'Studente',
        faqs: [
            {
                question: 'Come posso visualizzare i miei voti?',
                answer: 'Puoi accedere all’elenco dei tuoi voti suddivisi per tipologia, materia e data direttamente dalla tua dashboard.',
            },
            {
                question: 'Come posso interagire con i miei insegnanti?',
                answer: 'Se previsto dal docente, puoi interagire con i post dell’assegno tramite la sezione commenti, allegando eventuali file richiesti.',
            },
        ],
    },
    {
        title: 'Professore',
        faqs: [
            {
                question: 'Come posso firmare le ore di lezione?',
                answer: 'Puoi firmare le ore previste dal tuo orario di lezione o eventuali supplenze direttamente dal tuo account.',
            },
            {
                question: 'Come posso inserire i voti degli studenti?',
                answer: 'Dalla tua dashboard, puoi inserire voti aggiungendo dettagli come motivazioni e assegnarli agli studenti pertinenti.',
            },
            {
                question: 'Come posso creare compiti assegnati?',
                answer: 'Puoi inserire e visualizzare i compiti assegnati, aggiungendo una data di scadenza, nella sezione dedicata ai compiti.',
            },
        ],
    },
    {
        title: 'Segreteria',
        faqs: [
            {
                question: 'Come posso creare un nuovo account studente?',
                answer: 'Puoi creare o rimuovere account studente tramite la sezione di gestione account nella tua dashboard.',
            },
            {
                question: 'Come aggiungo nuovi figli a un account genitore?',
                answer: 'Nella sezione dedicata ai genitori, puoi aggiungere nuovi figli inserendo i loro dati personali.',
            },
            {
                question: 'Come posso rispondere alle richieste di assistenza?',
                answer: 'Puoi gestire e rispondere ai ticket di assistenza nella sezione apposita, interagendo tramite messaggi testuali.',
            },
        ],
    },
    {
        title: 'Gestore scuole AstroMark',
        faqs: [
            {
                question: 'Come posso aggiungere una nuova scuola alla piattaforma?',
                answer: 'Puoi gestire le scuole tramite il pannello di gestione, dove puoi inserire e rimuovere scuole dalla piattaforma.',
            },
            {
                question: 'Come assegno classi a un professore?',
                answer: 'Dalla sezione di gestione account, puoi assegnare una o più classi ai professori selezionando i relativi account.',
            },
            {
                question: 'Come posso visualizzare gli orari delle classi?',
                answer: 'Puoi visualizzare gli orari di tutte le classi dell’istituto nella sezione dedicata agli orari.',
            },
        ],
    },
];

const HelpPage: React.FC = () => {
    const theme = useTheme();

    // Funzione per ottenere un colore di sfondo leggero basato sull'indice
    const getBackgroundColor = (index: number) => {
        const colors = [
            theme.palette.action.hover,
            theme.palette.background.default,
            theme.palette.grey[100],
            theme.palette.grey[50],
            theme.palette.grey[200],
            theme.palette.grey[300],
        ];
        return colors[index % colors.length];
    };

    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            {/* Header */}
            <Box textAlign="center" mb={4}>
                <Typography variant="h4" component="h1" gutterBottom color="primary">
                    Pagina di Aiuto
                </Typography>
                <Typography variant="body1" gutterBottom color="textSecondary">
                    Benvenuto nella pagina di supporto di AstroMark. Qui troverai risposte alle domande più frequenti per aiutarti a utilizzare al meglio la piattaforma.
                </Typography>
            </Box>

            {/* Sezioni FAQ per Tipologia di Utente */}
            <Box mt={4}>
                {faqData.map((userType, index) => (
                    <Box
                        key={index}
                        mb={4}
                        p={2}
                        sx={{
                            backgroundColor: getBackgroundColor(index),
                            borderRadius: 2,
                            boxShadow: 1,
                        }}
                    >
                        <Typography variant="h5" component="h2" gutterBottom color="secondary">
                            {userType.title}
                        </Typography>
                        {userType.faqs.map((faq, idx) => (
                            <Accordion key={idx} sx={{ mb: 2, border: `1px solid ${theme.palette.divider}` }}>
                                <AccordionSummary
                                    expandIcon={<ExpandMoreIcon color="primary" />}
                                    aria-controls={`panel${index}-${idx}-content`}
                                    id={`panel${index}-${idx}-header`}
                                >
                                    <Typography variant="subtitle1" color="textPrimary">
                                        {faq.question}
                                    </Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    <Typography variant="body2" color="textSecondary">
                                        {faq.answer}
                                    </Typography>
                                </AccordionDetails>
                            </Accordion>
                        ))}
                    </Box>
                ))}
            </Box>

            {/* Sezione Contatti */}
            <Box mt={6} textAlign="center" p={3} sx={{ backgroundColor: theme.palette.grey[100], borderRadius: 2 }}>
                <Typography variant="h6" gutterBottom color="info.main">
                    Hai bisogno di ulteriore assistenza?
                </Typography>
                <Typography variant="body1" color="textSecondary">
                    Contattaci all'indirizzo email:{' '}
                    <Link href={`mailto:${supportEmail}`} color="secondary">
                        {supportEmail}
                    </Link>
                </Typography>
            </Box>
        </Container>
    );
};



export const Help = () => {
    return (
        <main style={{
            overflowX: 'hidden',
            height: '100vh',
            display: 'flex',
            flexDirection: 'column'
        }}>
            <HomePageNavbar/>
            <Box sx={{height: '70vh', width: '100vw', m: 'auto', flex: 1}}>
                <HelpPage/>
            </Box>
        </main>
    );
};
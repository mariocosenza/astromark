SET SCHEMA 'astromark';

INSERT INTO school (code, phone_number, address, name, email, school_principal_full_name)
VALUES
    ('SS12345', 1234567890, 'Via Roma 1, 00100 Roma', 'Liceo Scientifico Galileo Galilei', 'galileo@example.com', 'Mario Rossi'),
    ('SS67890', 9876543210, 'Via Milano 2, 20100 Milano', 'Istituto Tecnico Leonardo da Vinci', 'leonardo@example.com', 'Anna Verdi'),
    ('SS11223', 3456789012, 'Via Napoli 3, 80100 Napoli', 'Scuola Media Statale Guglielmo Marconi', 'marconi@example.com', 'Luigi Bianchi');

INSERT INTO school_class (school_code, number, letter, year)
VALUES
    ('SS12345', 3, 'A', 2024),
    ('SS12345', 3, 'B', 2024),
    ('SS12345', 3, 'C', 2024),
    ('SS11223', 1, 'C', 2024),
    ('SS11223', 2, 'B', 2024),
    ('SS67890', 5, 'A', 2024),
    ('SS67890', 5, 'BS', 2024);

INSERT INTO communication (school_class_id, title, description, date)
VALUES
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Incontro con i genitori', 'Comunicazione sull''incontro del prossimo mese.', '2024-11-25'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Avvio corsi di recupero', 'Comunicazione sull''avvio dei corsi di recupero di matematica.', '2024-12-25'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Gita Scolastica', 'Informazioni sulla gita a Firenze.', '2025-01-25');

INSERT INTO subject (title)
VALUES
    ('Matematica'),
    ('Italiano'),
    ('Inglese'),
    ('Storia'),
    ('Informatica');

INSERT INTO study_plan (school_class_id, title)
VALUES
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Piano di studi 3A'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'B' AND year = 2024), 'Piano di studi 3B'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), 'Piano di studi 5BS'),
    ((SELECT id FROM school_class WHERE number = 1 AND letter = 'C' AND year = 2024), 'Piano di studi 1C'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), 'Piano di studi 3C'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), 'Piano di studi 5A');

INSERT INTO study_plan_subject (subject_title, study_plan_school_class_id)
VALUES
    ('Matematica', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 3A')),
    ('Italiano', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 3A')),
    ('Inglese', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 3A')),
    ('Storia', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 3A')),
    ('Informatica', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 5BS')),
    ('Italiano', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 1C')),
    ('Storia', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 3C')),
    ('Matematica', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 3C')),
    ('Inglese', (SELECT school_class_id FROM study_plan WHERE title = 'Piano di studi 5A'));

INSERT INTO parent (school_code, username, email, password, name, surname, tax_id, birth_date, residential_address, gender, legal_guardian, pending_state)
VALUES
    ('SS12345', 'paolo.verdi', 'paoloverdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Paolo', 'Verdi', 'VRDPLA80A01H501X', '1980-01-01', 'Via Firenze 4, Roma', true, false, 'NORMAL'),
    ('SS12345', 'laura.bianchi', 'laurabianchi@gmail.com', encode(sha512('Bianchi123*'::bytea), 'hex'), 'Laura', 'Bianchi', 'BNCLRA85B02G479Y', '1985-02-02', 'Via Torino 5, Roma', false, true, 'NORMAL'),
    ('SS12345', 'giulia.neri', 'giulianeri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Giulia', 'Neri', 'NRIGLA70F01A123X', '1970-01-01', 'Via Venezia 3, Roma', false, true, 'NORMAL'),
    ('SS12345', 'paolo.rossi', 'paolorossi@gmail.com', encode(sha512('Rossi123*'::bytea), 'hex'), 'Paolo', 'Rossi', 'RSSPLA75M06A003Y', '1975-06-06', 'Via Firenze 15, Roma', true, true, 'NORMAL'),
    ('SS67890', 'roberto.verdi', 'robertoverdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Roberto', 'Verdi', 'VRDRBT65M01H345Z', '1965-01-01', 'Via Milano 8, Milano', true, true, 'NORMAL'),
    ('SS67890', 'laura.blui', 'laurablui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Laura', 'Blui', 'BLULRA80F08C002Z', '1980-08-08', 'Via Bergamo 7, Milano', false, true, 'NORMAL'),
    ('SS11223', 'anna.neri', 'annaneri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Anna', 'Neri', 'NRINNA85F09D003X', '1985-09-09', 'Via Napoli 10, Napoli', false, true, 'NORMAL');

INSERT INTO secretary (school_code, username, email, password, name, surname, tax_id, birth_date, residential_address, gender, pending_state)
VALUES
    ('SS12345', 'giorgio.neri', 'giorgio.neri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Giorgio', 'Neri', 'NRIGRG90C03F205Z', '1990-03-03', 'Via Venezia 6, Roma', true, 'NORMAL'),
    ('SS12345', 'laura.verdi', 'laura.verdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Laura', 'Verdi', 'VRDLRA85F03D890U', '1985-03-03', 'Via Trieste 7, Roma', false, 'NORMAL'),
    ('SS67890', 'mario.neri', 'mario.neri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Mario', 'Neri', 'NRIMRA78F03D234X', '1978-03-03', 'Via Trieste 6, Roma', true, 'NORMAL');

INSERT INTO teacher (school_code, username, email, password, name, surname, tax_id, birth_date, residential_address, gender, pending_state)
VALUES
    ('SS12345', 'marco.gialli', 'marco.gialli@gmail.com', encode(sha512('Gialli123*'::bytea), 'hex'), 'Marco', 'Gialli', 'GLLMRC75D04H703W', '1975-04-04', 'Via Genova 7, Roma', true, 'NORMAL'),
    ('SS67890', 'elena.blui', 'elena.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Elena', 'Blui', 'BLELNA82E05Z112V', '1982-05-05', 'Via Palermo 8, Milano', false, 'NORMAL'),
    ('SS12345', 'riccardo.blui', 'riccardo.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Riccardo', 'Blui', 'BLURCD78E04G111W', '1978-04-04', 'Via Palermo 6, Milano', true, 'NORMAL'),
    ('SS12345', 'elisa.gialli', 'elisa.gialli@gmail.com', encode(sha512('Gialli123*'::bytea), 'hex'), 'Elisa', 'Gialli', 'GLLSFA82F01B890T', '1982-01-01', 'Via Firenze 1, Roma', false, 'NORMAL'),
    ('SS67890', 'anna.blui', 'anna.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Anna', 'Blui', 'BLUANN70M03F456T', '1970-03-03', 'Via Milano 10, Milano', false, 'NORMAL');

INSERT INTO teacher_class (teacher_id, school_class_id, coordinator)
VALUES
    ((SELECT id FROM teacher WHERE username = 'marco.gialli'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), true),
    ((SELECT id FROM teacher WHERE username = 'elena.blui'), (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'riccardo.blui'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'elisa.gialli'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), true),
    ((SELECT id FROM teacher WHERE username = 'anna.blui'), (SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), false);

INSERT INTO teaching (subject_title, teacher_id, type_of_activity)
VALUES
    ('Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Lezione'),
    ('Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui'), 'Laboratorio');

INSERT INTO student (school_code, username, email, password, name, surname, tax_id, birth_date, residential_address, gender, pending_state)
VALUES
    ('SS12345', 'marco.neri', 'marco.neri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Marco', 'Neri', 'NRIMRC10G06Z404T', '2010-06-06', 'Via Venezia 3, Roma', true, 'NORMAL'),
    ('SS12345', 'luca.verdi', 'luca.verdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Luca', 'Verdi', 'VRDLCA08F06I123U', '2008-06-06', 'Via Firenze 4, Roma', true, 'NORMAL'),
    ('SS12345', 'sofia.bianchi', 'sofia.bianchi@gmail.com', encode(sha512('Bianchi123*'::bytea), 'hex'), 'Sofia', 'Bianchi', 'BNCSFA09L47Z404T', '2009-07-07', 'Via Torino 5, Roma', false, 'NORMAL'),
    ('SS12345', 'giulia.rossi', 'giulia.rossi@gmail.com', encode(sha512('Rossi123*'::bytea), 'hex'), 'Giulia', 'Rossi', 'RSSGLA10F06A002X', '2010-06-06', 'Via Firenze 15, Roma', false, 'NORMAL'),
    ('SS67890', 'elisa.verdi', 'elisa.verdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Elisa', 'Verdi', 'VRDLSE11F07B002Z', '2011-07-07', 'Via Milano 8, Milano', false, 'NORMAL'),
    ('SS67890', 'franco.blui', 'franco.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Franco', 'Blui', 'BLUFNC12M08C001Y', '2012-08-08', 'Via Bergamo 7, Milano', true, 'NORMAL'),
    ('SS11223', 'chiara.neri', 'chiara.neri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Chiara', 'Neri', 'NRICHR13F09D001Z', '2013-09-09', 'Via Napoli 10, Napoli', false, 'NORMAL');

INSERT INTO delay (student_id, needs_justification, justified, justification_text, date_time)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), true, false, null, '2025-01-16 08:10:00');

INSERT INTO absence (student_id, needs_justification, justified, justification_text, date)
VALUES
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), false, true, 'Visita medica.', '2025-01-15');

INSERT INTO note (student_id, description)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), 'Non presta attenzione in classe.');

INSERT INTO mark (student_id, teaching_subject_title, teaching_teacher_id, date, type, mark, description)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-22', 'ORAL', 8, 'Interrogazione su funzioni.'),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-22', 'ORAL', 6, 'Interrogazione su funzioni incompleta.');

INSERT INTO semester_report (first_semester, public, passed, year, student_id) VALUES
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'luca.verdi'));

INSERT INTO semester_report_mark (subject_title, semester_id, mark)
VALUES
    ('Matematica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 7),
    ('Italiano', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 8),
    ('Inglese', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 9),
    ('Storia', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 6);

INSERT INTO class_timetable (school_class_id, start_validity, end_validity)
VALUES
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), '2024-09-01', '2024-10-05'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), '2024-10-05', null),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), '2024-09-01', '2024-10-05'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), '2024-10-06', null),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'B' AND year = 2024), '2024-10-08', null),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), '2024-09-01', null),
    ((SELECT id FROM school_class WHERE number = 1 AND letter = 'C' AND year = 2024), '2024-09-01', null),
    ((SELECT id FROM school_class WHERE number = 2 AND letter = 'B' AND year = 2024), '2024-09-11', null),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), '2024-09-01', '2024-10-06'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), '2024-10-06', null);

INSERT INTO teaching_timeslot (class_timetable_id, hour, date, teaching_subject_title, teaching_teacher_id)
VALUES
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) LIMIT 1), 1, '2025-01-15', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) LIMIT 1), 2, '2025-01-15','Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) LIMIT 1), 3, '2025-01-15', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'B' AND year = 2024) LIMIT 1), 4, '2025-01-17', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024) LIMIT 1), 3, '2025-01-18', 'Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) LIMIT 1), 1, '2025-01-18', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'));

INSERT INTO signed_hour (teaching_timeslot_id, teacher_id, substitution)
VALUES
    ((SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-15' LIMIT 1), (SELECT id FROM teacher WHERE username = 'marco.gialli'), false);

INSERT INTO reception_timetable(teacher_id, text_info_reception, start_validity)
VALUES
    ((SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Ricevimento per genitori.', '2024-10-12');

INSERT INTO reception_timeslot(reception_timetable_id, hour, date, capacity, booked, mode)
VALUES
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli')), 3, '2025-01-17', 4, 1, 'In presenza');

INSERT INTO reception_booking(parent_id, reception_timeslot_id, booking_order, confirmed)
VALUES
    ((SELECT id FROM parent WHERE username = 'paolo.verdi'), (SELECT id FROM reception_timeslot WHERE reception_timetable_id = (SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli')) LIMIT 1), 1, false);

INSERT INTO class_activity (signed_hour_teaching_timeslot_id, title, description)
VALUES
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli') LIMIT 1), 'Lezione di Matematica', 'Ripasso sulle equazioni di secondo grado.');


INSERT INTO homework (signed_hour_teaching_timeslot_id, due_date, title, description)
VALUES
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli') LIMIT 1), '2025-03-18', 'Esercizi sulle equazioni', 'Risolvere gli esercizi da 1 a 10 a pagina 50 del libro di testo.');


INSERT INTO homework_chat (homework_signed_hour_teaching_timeslot_id, title, student_id)
VALUES
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli') LIMIT 1), 'Dubbio esercizio 3', (SELECT id FROM student WHERE username = 'luca.verdi'));

INSERT INTO ticket (parent_id, teacher_id, title, category, datetime, closed, solved)
VALUES
    ((SELECT id FROM parent WHERE username = 'paolo.verdi'), null, 'Richiesta informazioni sulla gita', 'Gite scolastiche', '2025-01-17 10:32:46', false, false),
    ((SELECT id FROM parent WHERE username = 'giulia.neri'), null, 'Problema iscrizione', 'Iscrizione', '2025-02-01 10:00:00', false, false),
    ((SELECT id FROM parent WHERE username = 'roberto.verdi'), null, 'Richiesta documenti', 'Documenti', '2025-02-02 11:30:00', false, false);

INSERT INTO message (ticket_id, homework_chat_id, parent_id, secretary_id, student_id, teacher_id, date_time, text)
VALUES
    ((SELECT id FROM ticket WHERE title = 'Richiesta informazioni sulla gita'), null,  (SELECT id FROM parent WHERE username = 'paolo.verdi'), null, null, null, '2025-01-17 09:10:00', 'Vorrei sapere maggiori dettagli sull''organizzazione della gita.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Chiarimenti compito'), null, null, (SELECT id FROM student WHERE username = 'marco.neri'), null, '2025-02-05 14:30:00', 'Non capisco la domanda 3.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Chiarimenti compito'), null, null, null, (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-02-05 14:35:00', 'Quale parte non ti è chiara?'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Chiarimenti compito'), null, null, (SELECT id FROM student WHERE username = 'marco.neri'), null, '2025-02-05 14:40:00', 'Il modo in cui calcolare il determinante.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi su un esercizio'), null, null, (SELECT id FROM student WHERE username = 'luca.verdi'), null, '2025-02-06 09:00:00', 'Ho difficoltà con l’esercizio 5.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi su un esercizio'), null, null, null, (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-02-06 09:15:00', 'Prova a dividere il problema in piccoli passi.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi su un esercizio'), null, null, (SELECT id FROM student WHERE username = 'luca.verdi'), null, '2025-02-06 09:20:00', 'Grazie, ora è più chiaro!'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi sul saggio'), null, null, (SELECT id FROM student WHERE username = 'elisa.verdi'), null, '2025-02-08 10:00:00', 'Il formato del saggio è obbligatorio?'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi sul saggio'), null, null, null, (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-02-08 10:10:00', 'Sì, segui le linee guida della pagina 12 del libro.'),
    ((SELECT id FROM ticket WHERE title = 'Problema iscrizione'), null, (SELECT id FROM parent WHERE username = 'giulia.neri'), null, null,  null, '2025-02-01 10:05:00', 'Non riesco a completare la procedura di iscrizione.'),
    ((SELECT id FROM ticket WHERE title = 'Problema iscrizione'), null, null, (SELECT id FROM secretary WHERE username = 'laura.verdi'), null, null, '2025-02-01 10:10:00', 'Può fornirmi ulteriori dettagli sul problema?'),
    ((SELECT id FROM ticket WHERE title = 'Problema iscrizione'), null, (SELECT id FROM parent WHERE username = 'giulia.neri'), null, null, null, '2025-02-01 10:15:00', 'Il sistema restituisce un errore durante il pagamento.'),
    ((SELECT id FROM ticket WHERE title = 'Richiesta documenti'), null, (SELECT id FROM parent WHERE username = 'roberto.verdi'), null, null, null, '2025-02-02 11:35:00', 'Avrei bisogno di una copia del certificato di frequenza.'),
    ((SELECT id FROM ticket WHERE title = 'Richiesta documenti'), null, null, (SELECT id FROM secretary WHERE username = 'mario.neri'), null, null, '2025-02-02 11:40:00', 'La sua richiesta è stata inoltrata, riceverà una risposta a breve.'),
    ((SELECT id FROM ticket WHERE title = 'Richiesta documenti'), null, (SELECT id FROM parent WHERE username = 'roberto.verdi'), null, null, null, '2025-02-02 11:45:00', 'Grazie mille!');

INSERT INTO student_parent (student_id, parent_id)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), (SELECT id FROM parent WHERE username = 'paolo.verdi')),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), (SELECT id FROM parent WHERE username = 'laura.bianchi')),
    ((SELECT id FROM student WHERE username = 'marco.neri'), (SELECT id FROM parent WHERE username = 'giulia.neri')),
    ((SELECT id FROM student WHERE username = 'giulia.rossi'), (SELECT id FROM parent WHERE username = 'paolo.rossi')),
    ((SELECT id FROM student WHERE username = 'elisa.verdi'), (SELECT id FROM parent WHERE username = 'roberto.verdi')),
    ((SELECT id FROM student WHERE username = 'franco.blui'), (SELECT id FROM parent WHERE username = 'laura.blui')),
    ((SELECT id FROM student WHERE username = 'chiara.neri'), (SELECT id FROM parent WHERE username = 'anna.neri'));

INSERT INTO red_date (class_timetable_id, date)
VALUES
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) LIMIT 1), '2024-12-25');

INSERT INTO student_school_class (school_class_id, student_id)
VALUES
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), (SELECT id FROM student WHERE username = 'marco.neri')),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'luca.verdi')),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'sofia.bianchi')),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), (SELECT id FROM student WHERE username = 'giulia.rossi')),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'elisa.verdi')),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'franco.blui')),
    ((SELECT id FROM school_class WHERE number = 2 AND letter = 'B' AND year = 2024), (SELECT id FROM student WHERE username = 'chiara.neri'));

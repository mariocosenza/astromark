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
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Gita Scolastica', 'Informazioni sulla gita a Firenze.', '2025-01-25'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Assemblea di classe', 'Convocazione per l’assemblea di classe del mese.', '2025-02-01'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'B' AND year = 2024), 'Distribuzione materiale scolastico', 'Comunicazione sulla distribuzione del materiale per il laboratorio.', '2025-02-05'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), 'Incontro con esperti', 'Conferenza con esperti sul tema delle energie rinnovabili.', '2025-02-10'),
    ((SELECT id FROM school_class WHERE number = 1 AND letter = 'C' AND year = 2024), 'Prova di evacuazione', 'Comunicazione sulla prova di evacuazione programmata.', '2025-02-15'),
    ((SELECT id FROM school_class WHERE number = 2 AND letter = 'B' AND year = 2024), 'Attività sportiva', 'Partecipazione a una giornata di sport interscolastica.', '2025-02-20'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), 'Simulazione esame di maturità', 'Programmazione della prima simulazione per l’esame di maturità.', '2025-02-25'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), 'Progetto PCTO', 'Presentazione del progetto di PCTO per l’anno in corso.', '2025-03-01');

INSERT INTO subject (title)
VALUES
    ('Matematica'),
    ('Fisica'),
    ('Geometria'),
    ('Chimica'),
    ('Italiano'),
    ('Inglese'),
    ('Storia'),
    ('Informatica');

INSERT INTO study_plan (school_class_id, title)
VALUES
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), 'Tradizionale'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'B' AND year = 2024), 'Tradizionale'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), 'Musicale'),
    ((SELECT id FROM school_class WHERE number = 1 AND letter = 'C' AND year = 2024), 'Linguistico'),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), 'Linguistico'),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), 'Tradizionale');

INSERT INTO study_plan_subject (subject_title, study_plan_school_class_id)
VALUES
    ('Matematica', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024))),
    ('Fisica', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024))),
    ('Chimica', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024))),
    ('Italiano', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024))),
    ('Inglese', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024))),
    ('Storia', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024))),
    ('Informatica', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024))),
    ('Geometria', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024))),
    ('Italiano', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 1 AND letter = 'C' AND year = 2024))),
    ('Storia', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024))),
    ('Matematica', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024))),
    ('Inglese', (SELECT school_class_id FROM study_plan WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024)));

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
    ('SS12345', 'alessandro.rossi', 'alessandro.rossi@gmail.com', encode(sha512('Rossi123*'::bytea), 'hex'), 'Alessandro', 'Rossi', 'RSSALN80A01H501X', '1980-01-01', 'Via Firenze 20, Roma', true, 'NORMAL'),
    ('SS12345', 'maria.bianchi', 'maria.bianchi@gmail.com', encode(sha512('Bianchi123*'::bytea), 'hex'), 'Maria', 'Bianchi', 'BNCMRA85B02G479Y', '1985-02-02', 'Via Torino 25, Roma', false, 'NORMAL'),
    ('SS67890', 'elena.blui', 'elena.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Elena', 'Blui', 'BLELNA82E05Z112V', '1982-05-05', 'Via Palermo 8, Milano', false, 'NORMAL'),
    ('SS12345', 'riccardo.blui', 'riccardo.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Riccardo', 'Blui', 'BLURCD78E04G111W', '1978-04-04', 'Via Palermo 6, Milano', true, 'NORMAL'),
    ('SS12345', 'elisa.gialli', 'elisa.gialli@gmail.com', encode(sha512('Gialli123*'::bytea), 'hex'), 'Elisa', 'Gialli', 'GLLSFA82F01B890T', '1982-01-01', 'Via Firenze 1, Roma', false, 'NORMAL'),
    ('SS67890', 'anna.blui', 'anna.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Anna', 'Blui', 'BLUANN70M03F456T', '1970-03-03', 'Via Milano 10, Milano', false, 'NORMAL');

INSERT INTO teacher_class (teacher_id, school_class_id, coordinator)
VALUES
    ((SELECT id FROM teacher WHERE username = 'marco.gialli'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), true),
    ((SELECT id FROM teacher WHERE username = 'marco.gialli'), (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'alessandro.rossi'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'maria.bianchi'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'elena.blui'), (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'riccardo.blui'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), false),
    ((SELECT id FROM teacher WHERE username = 'elisa.gialli'), (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), true),
    ((SELECT id FROM teacher WHERE username = 'anna.blui'), (SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), false);

INSERT INTO teaching (subject_title, teacher_id, type_of_activity)
VALUES
    ('Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Lezione'),
    ('Geometria', (SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Lezione'),
    ('Fisica', (SELECT id FROM teacher WHERE username = 'alessandro.rossi'), 'Lezione'),
    ('Chimica', (SELECT id FROM teacher WHERE username = 'maria.bianchi'), 'Laboratorio'),
    ('Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui'), 'Laboratorio'),
    ('Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui'), 'Lezione'),
    ('Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli'), 'Lezione'),
    ('Inglese', (SELECT id FROM teacher WHERE username = 'anna.blui'), 'Lezione');

INSERT INTO student (school_code, username, email, password, name, surname, tax_id, birth_date, residential_address, gender, pending_state)
VALUES
    ('SS12345', 'pluto.pippo', 'pluto.pippo@gmail.com', encode(sha512('Pluto123!'::bytea), 'hex'), 'Marco', 'Neri', 'PRIMRC10G06Z404T', '2010-06-06', 'Via Venezia 3, Roma', true, 'NORMAL'),
    ('SS12345', 'pl.pi', 'pl.pi@gmail.com', encode(sha512('Pluto123!'::bytea), 'hex'), 'Marco', 'Neri', 'PRIMRC10G36Z404T', '2010-06-06', 'Via Venezia 3, Roma', true, 'NORMAL'),
    ('SS12345', 'pluton.paperino', 'pluton.paperino@gmail.com', encode(sha512('Pluto123!'::bytea), 'hex'), 'Marco', 'Neri', 'PRIMRC10L06Z404T', '2010-06-06', 'Via Venezia 3, Roma', true, 'NORMAL'),
    ('SS12345', 'marco.neri', 'marco.neri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Marco', 'Neri', 'NRIMRC10G06Z404T', '2010-06-06', 'Via Venezia 3, Roma', true, 'NORMAL'),
    ('SS12345', 'luca.verdi', 'luca.verdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Luca', 'Verdi', 'VRDLCA08F06I123U', '2008-06-06', 'Via Firenze 4, Roma', true, 'NORMAL'),
    ('SS12345', 'sofia.bianchi', 'sofia.bianchi@gmail.com', encode(sha512('Bianchi123*'::bytea), 'hex'), 'Sofia', 'Bianchi', 'BNCSFA09L47Z404T', '2009-07-07', 'Via Torino 5, Roma', false, 'NORMAL'),
    ('SS12345', 'giulia.rossi', 'giulia.rossi@gmail.com', encode(sha512('Rossi123*'::bytea), 'hex'), 'Giulia', 'Rossi', 'RSSGLA10F06A002X', '2010-06-06', 'Via Firenze 15, Roma', false, 'NORMAL'),
    ('SS67890', 'elisa.verdi', 'elisa.verdi@gmail.com', encode(sha512('Verdi123*'::bytea), 'hex'), 'Elisa', 'Verdi', 'VRDLSE11F07B002Z', '2011-07-07', 'Via Milano 8, Milano', false, 'NORMAL'),
    ('SS67890', 'franco.blui', 'franco.blui@gmail.com', encode(sha512('Blui123*'::bytea), 'hex'), 'Franco', 'Blui', 'BLUFNC12M08C001Y', '2012-08-08', 'Via Bergamo 7, Milano', true, 'NORMAL'),
    ('SS11223', 'chiara.neri', 'chiara.neri@gmail.com', encode(sha512('Neri123*'::bytea), 'hex'), 'Chiara', 'Neri', 'NRICHR13F09D001Z', '2013-09-09', 'Via Napoli 10, Napoli', false, 'NORMAL');

INSERT INTO delay (student_id, needs_justification, justified, justification_text, date_time)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), true, false, null, '2025-01-16 08:10:00'),
    ((SELECT id FROM student WHERE username = 'marco.neri'), true, false, null, '2025-02-10 08:20:00'),
    ((SELECT id FROM student WHERE username = 'giulia.rossi'), false, true, 'Ritardo causato da traffico.', '2025-02-15 08:30:00'),
    ((SELECT id FROM student WHERE username = 'elisa.verdi'), true, false, null, '2025-02-20 08:25:00');

INSERT INTO absence (student_id, needs_justification, justified, justification_text, date)
VALUES
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), false, true, 'Visita medica.', '2025-01-15'),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), true, true, 'Assenza giustificata per malattia.', '2025-01-22'),
    ((SELECT id FROM student WHERE username = 'franco.blui'), true, false, null, '2025-02-05'),
    ((SELECT id FROM student WHERE username = 'chiara.neri'), false, true, 'Gita scolastica.', '2025-02-10');

INSERT INTO note (student_id, description)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), 'Non presta attenzione in classe.'),
    ((SELECT id FROM student WHERE username = 'marco.neri'), 'Disturbo continuo durante la lezione di storia.'),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), 'Utilizzo non autorizzato del cellulare durante la lezione.'),
    ((SELECT id FROM student WHERE username = 'giulia.rossi'), 'Non ha rispettato le indicazioni fornite dall’insegnante di italiano.'),
    ((SELECT id FROM student WHERE username = 'elisa.verdi'), 'Discussioni accese con i compagni durante il laboratorio di informatica.'),
    ((SELECT id FROM student WHERE username = 'franco.blui'), 'Assenza non giustificata durante un test di matematica.'),
    ((SELECT id FROM student WHERE username = 'chiara.neri'), 'Non ha consegnato i compiti assegnati per la lezione di inglese.');

INSERT INTO mark (student_id, teaching_subject_title, teaching_teacher_id, date, type, mark, description)
VALUES
    ((SELECT id FROM student WHERE username = 'luca.verdi'), 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-22', 'ORAL', 8, 'Interrogazione su funzioni.'),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-22', 'ORAL', 6, 'Interrogazione su funzioni incompleta.'),
    ((SELECT id FROM student WHERE username = 'marco.neri'), 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-02-01', 'WRITTEN', 7, 'Test sulle equazioni.'),
    ((SELECT id FROM student WHERE username = 'marco.neri'), 'Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-02-15', 'ORAL', 6, 'Interrogazione sul Rinascimento.'),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-02-05', 'WRITTEN', 8, 'Compito sui sistemi lineari.'),
    ((SELECT id FROM student WHERE username = 'sofia.bianchi'), 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-02-10', 'ORAL', 7, 'Analisi di un testo poetico.'),
    ((SELECT id FROM student WHERE username = 'giulia.rossi'), 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-02-12', 'WRITTEN', 6, 'Tema: il Romanticismo.'),
    ((SELECT id FROM student WHERE username = 'giulia.rossi'), 'Inglese', (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-02-20', 'ORAL', 7, 'Dialogo su un testo letterario.'),
    ((SELECT id FROM student WHERE username = 'elisa.verdi'), 'Inglese', (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-02-18', 'WRITTEN', 9, 'Traduzione di un brano.'),
    ((SELECT id FROM student WHERE username = 'elisa.verdi'), 'Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui'), '2025-02-25', 'WRITTEN', 8, 'Progetto: sviluppo di un algoritmo.'),
    ((SELECT id FROM student WHERE username = 'franco.blui'), 'Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui'), '2025-02-14', 'WRITTEN', 7, 'Compito sulla programmazione.'),
    ((SELECT id FROM student WHERE username = 'franco.blui'), 'Inglese', (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-02-22', 'ORAL', 8, 'Esposizione di un argomento a scelta.'),
    ((SELECT id FROM student WHERE username = 'chiara.neri'), 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-02-13', 'WRITTEN', 8, 'Analisi di un testo narrativo.'),
    ((SELECT id FROM student WHERE username = 'chiara.neri'), 'Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-02-21', 'ORAL', 7, 'Interrogazione sulla Rivoluzione Francese.');

INSERT INTO semester_report (first_semester, public, passed, year, student_id)
VALUES
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'luca.verdi')),
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'marco.neri')),
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'sofia.bianchi')),
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'giulia.rossi')),
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'elisa.verdi')),
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'franco.blui')),
    (true, true, true, 2024, (SELECT id FROM student WHERE username = 'chiara.neri'));

INSERT INTO semester_report_mark (subject_title, semester_id, mark)
VALUES
    ('Matematica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 7),
    ('Italiano', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 8),
    ('Inglese', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 9),
    ('Storia', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'luca.verdi') AND year = 2024 AND first_semester = true), 6),
    ('Matematica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'marco.neri') AND year = 2024 AND first_semester = true), 8),
    ('Italiano', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'marco.neri') AND year = 2024 AND first_semester = true), 7),
    ('Inglese', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'marco.neri') AND year = 2024 AND first_semester = true), 6),
    ('Storia', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'marco.neri') AND year = 2024 AND first_semester = true), 7),
    ('Matematica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'sofia.bianchi') AND year = 2024 AND first_semester = true), 7),
    ('Italiano', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'sofia.bianchi') AND year = 2024 AND first_semester = true), 8),
    ('Inglese', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'sofia.bianchi') AND year = 2024 AND first_semester = true), 9),
    ('Matematica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'giulia.rossi') AND year = 2024 AND first_semester = true), 6),
    ('Italiano', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'giulia.rossi') AND year = 2024 AND first_semester = true), 8),
    ('Inglese', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'elisa.verdi') AND year = 2024 AND first_semester = true), 10),
    ('Matematica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'elisa.verdi') AND year = 2024 AND first_semester = true), 7),
    ('Informatica', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'franco.blui') AND year = 2024 AND first_semester = true), 9),
    ('Italiano', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'chiara.neri') AND year = 2024 AND first_semester = true), 8),
    ('Storia', (SELECT id FROM semester_report WHERE student_id = (SELECT id FROM student WHERE username = 'chiara.neri') AND year = 2024 AND first_semester = true), 7);

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
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 1, '2025-01-15', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-15', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-15', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 1, '2025-01-18', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'B' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-17', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-19', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-19', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 1, '2025-01-20', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 1, '2025-01-21', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-22', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 6, '2025-01-23', 'Matematica', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-20', 'Fisica', (SELECT id FROM teacher WHERE username = 'alessandro.rossi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-21', 'Fisica', (SELECT id FROM teacher WHERE username = 'alessandro.rossi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-22', 'Fisica', (SELECT id FROM teacher WHERE username = 'alessandro.rossi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-23', 'Fisica', (SELECT id FROM teacher WHERE username = 'alessandro.rossi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-20', 'Chimica', (SELECT id FROM teacher WHERE username = 'maria.bianchi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-21', 'Chimica', (SELECT id FROM teacher WHERE username = 'maria.bianchi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 5, '2025-01-22', 'Chimica', (SELECT id FROM teacher WHERE username = 'maria.bianchi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-23', 'Chimica', (SELECT id FROM teacher WHERE username = 'maria.bianchi')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 1, '2025-01-15', 'Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-17', 'Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-17', 'Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 5, '2025-01-17', 'Storia', (SELECT id FROM teacher WHERE username = 'riccardo.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-16', 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 5, '2025-01-16', 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-15', 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-15', 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-15', 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-18', 'Italiano', (SELECT id FROM teacher WHERE username = 'elisa.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-16', 'Inglese', (SELECT id FROM teacher WHERE username = 'anna.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), 4, '2025-01-18', 'Inglese', (SELECT id FROM teacher WHERE username = 'anna.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024) AND end_validity IS NULL), 2, '2025-01-18', 'Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-18', 'Informatica', (SELECT id FROM teacher WHERE username = 'elena.blui')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-21', 'Geometria', (SELECT id FROM teacher WHERE username = 'marco.gialli')),
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 5 AND letter = 'BS' AND year = 2024) AND end_validity IS NULL), 3, '2025-01-23', 'Geometria', (SELECT id FROM teacher WHERE username = 'marco.gialli'));

INSERT INTO signed_hour (teaching_timeslot_id, teacher_id, time_sign, substitution)
VALUES
    ((SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-15' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-15 08:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-15' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-15 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-15' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-15 10:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-18' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-18 08:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-17' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-17 11:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-19' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-19 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-21' AND teaching_subject_title = 'Matematica'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-21 08:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-22' AND teaching_subject_title = 'Fisica'), (SELECT id FROM teacher WHERE username = 'alessandro.rossi'), '2025-01-22 10:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-21' AND teaching_subject_title = 'Fisica'), (SELECT id FROM teacher WHERE username = 'alessandro.rossi'), '2025-01-21 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-22' AND teaching_subject_title = 'Chimica'), (SELECT id FROM teacher WHERE username = 'maria.bianchi'), '2025-01-22 12:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-23' AND teaching_subject_title = 'Chimica'), (SELECT id FROM teacher WHERE username = 'maria.bianchi'), '2025-01-23 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-15' AND teaching_subject_title = 'Storia'), (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-01-15 08:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-17' AND teaching_subject_title = 'Storia'), (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-01-17 10:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-17' AND teaching_subject_title = 'Storia'), (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-01-17 11:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-17' AND teaching_subject_title = 'Storia'), (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-01-17 12:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-16' AND teaching_subject_title = 'Italiano'), (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-01-16 11:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-16' AND teaching_subject_title = 'Italiano'), (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-01-16 12:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-15' AND teaching_subject_title = 'Italiano'), (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-01-15 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-15' AND teaching_subject_title = 'Italiano'), (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-01-15 10:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-15' AND teaching_subject_title = 'Italiano'), (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-01-15 11:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-18' AND teaching_subject_title = 'Italiano'), (SELECT id FROM teacher WHERE username = 'elisa.gialli'), '2025-01-18 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-16' AND teaching_subject_title = 'Inglese'), (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-01-16 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-18' AND teaching_subject_title = 'Inglese'),  (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-01-18 11:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-18' AND teaching_subject_title = 'Informatica'), (SELECT id FROM teacher WHERE username = 'elena.blui'), '2025-01-18 09:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-18' AND teaching_subject_title = 'Informatica'), (SELECT id FROM teacher WHERE username = 'elena.blui'), '2025-01-18 10:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-21' AND teaching_subject_title = 'Geometria'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-21 10:15', false),
    ((SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-23' AND teaching_subject_title = 'Geometria'), (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-01-23 10:15',  false);

INSERT INTO reception_timetable(teacher_id, text_info_reception, start_validity)
VALUES
    ((SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Ricevimento per genitori.', '2024-10-12'),
    ((SELECT id FROM teacher WHERE username = 'elena.blui'), 'Ricevimento per i progetti di informatica.', '2024-11-01'),
    ((SELECT id FROM teacher WHERE username = 'riccardo.blui'), 'Ricevimento per discussione di storia.', '2024-11-01'),
    ((SELECT id FROM teacher WHERE username = 'elisa.gialli'), 'Ricevimento per italiano e letteratura.', '2024-11-01'),
    ((SELECT id FROM teacher WHERE username = 'anna.blui'), 'Ricevimento per inglese e approfondimenti.', '2024-11-01');

INSERT INTO reception_timeslot(reception_timetable_id, hour, date, capacity, booked, mode)
VALUES
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli')), 3, '2025-01-17', 4, 1, 'In presenza'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli')), 4, '2025-01-18', 6, 1, 'Online'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'elena.blui')), 1, '2025-01-20', 5, 2, 'Online'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'elena.blui')), 2, '2025-01-21', 3, 1, 'In presenza'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'riccardo.blui')), 3, '2025-01-22', 4, 3, 'In presenza'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'riccardo.blui')), 4, '2025-01-23', 5, 2, 'Online'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'elisa.gialli')), 1, '2025-01-24', 4, 2, 'Online'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'elisa.gialli')), 2, '2025-01-25', 3, 1, 'In presenza'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'anna.blui')), 1, '2025-01-26', 5, 3, 'In presenza'),
    ((SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'anna.blui')), 2, '2025-01-27', 6, 2, 'Online');

INSERT INTO reception_booking(parent_id, reception_timeslot_id, booking_order, confirmed)
VALUES
    ((SELECT id FROM parent WHERE username = 'paolo.verdi'), (SELECT id FROM reception_timeslot WHERE reception_timetable_id = (SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'marco.gialli')) AND date = '2025-01-17' AND hour = 3), 1, false),
    ((SELECT id FROM parent WHERE username = 'paolo.verdi'), (SELECT id FROM reception_timeslot WHERE reception_timetable_id = (SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'elena.blui')) AND date = '2025-01-20' AND hour = 1),1, true),
    ((SELECT id FROM parent WHERE username = 'laura.bianchi'), (SELECT id FROM reception_timeslot WHERE reception_timetable_id = (SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'elena.blui')) AND date = '2025-01-21' AND hour = 2), 2, false),
    ((SELECT id FROM parent WHERE username = 'giulia.neri'), (SELECT id FROM reception_timeslot WHERE reception_timetable_id = (SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'riccardo.blui')) AND date = '2025-01-22' AND hour = 3), 1, true),
    ((SELECT id FROM parent WHERE username = 'roberto.verdi'), (SELECT id FROM reception_timeslot WHERE reception_timetable_id = (SELECT id FROM reception_timetable WHERE teacher_id = (SELECT id FROM teacher WHERE username = 'riccardo.blui')) AND date = '2025-01-23' AND hour = 4), 1, false);

INSERT INTO class_activity (signed_hour_teaching_timeslot_id, title, description)
VALUES
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-15' AND teaching_subject_title = 'Matematica')), 'Lezione di Matematica', 'Approfondimento sulle funzioni quadratiche.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-18' AND teaching_subject_title = 'Matematica')), 'Lezione di Matematica', 'Ripasso sulle equazioni di secondo grado.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-19' AND teaching_subject_title = 'Matematica')), 'Approfondimento su funzioni lineari', 'Discussione sui concetti fondamentali delle funzioni lineari.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-21' AND teaching_subject_title = 'Matematica')), 'Ripasso di geometria', 'Esercizi e teoria sui poligoni regolari e circonferenze.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-22' AND teaching_subject_title = 'Fisica')), 'Lezione su moto rettilineo uniforme', 'Analisi teorica e risoluzione di problemi sul moto rettilineo uniforme.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-21' AND teaching_subject_title = 'Fisica')), 'Esercizi su velocità e accelerazione', 'Problemi pratici per calcolare velocità, accelerazione e tempi di percorrenza.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-22' AND teaching_subject_title = 'Chimica')), 'Introduzione alla tavola periodica', 'Analisi degli elementi chimici e delle loro proprietà.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-23' AND teaching_subject_title = 'Chimica')), 'Esperimento: elettrolisi dell’acqua', 'Dimostrazione pratica dell’elettrolisi per separare idrogeno e ossigeno.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-18' AND teaching_subject_title = 'Informatica')), 'Laboratorio di Informatica', 'Sviluppo di un programma semplice in Python.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-17' AND teaching_subject_title = 'Storia')), 'Lezione di Storia', 'Discussione sulle principali invenzioni.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-17' AND teaching_subject_title = 'Storia')), 'Lezione di Storia', 'Analisi degli eventi della Seconda Guerra Mondiale.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-16' AND teaching_subject_title = 'Italiano')), 'Lezione di Italiano', 'Introduzione alla Divina Commedia di Dante Alighieri.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-16' AND teaching_subject_title = 'Inglese')), 'Lezione di Inglese', 'Approfondimento sulla grammatica.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-18' AND teaching_subject_title = 'Inglese')), 'Lezione di Inglese', 'Studio e analisi di un testo letterario inglese.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-21' AND teaching_subject_title = 'Geometria')),'Esercizi sulla Circonferenza','Spiegazone sulla circonferenza.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-23' AND teaching_subject_title = 'Geometria')),'Ripasso sulla Circonferenza','Esercizi avanzati sulla circonferenza.');

INSERT INTO homework (signed_hour_teaching_timeslot_id, due_date, title, description)
VALUES
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-18' AND teaching_subject_title = 'Matematica')), '2025-02-25', 'Esercizi sulle equazioni', 'Risolvere gli esercizi da 1 a 10 a pagina 50 del libro di testo.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-19' AND teaching_subject_title = 'Matematica')), '2025-02-25', 'Esercizi su funzioni lineari', 'Completare gli esercizi 1-10 a pagina 45 del libro di testo.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 1 AND date = '2025-01-21' AND teaching_subject_title = 'Matematica')), '2025-02-28', 'Ripasso di geometria', 'Disegnare i principali poligoni e calcolare le loro aree e perimetri.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-22' AND teaching_subject_title = 'Fisica')), '2025-02-27', 'Problemi sul moto rettilineo uniforme', 'Risolvere i problemi 5-8 a pagina 120 del libro di testo.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-21' AND teaching_subject_title = 'Fisica')), '2025-02-27', 'Esercizi su velocità', 'Calcolare la velocità media in diversi scenari.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-22' AND teaching_subject_title = 'Chimica')), '2025-02-28', 'Ripasso sulla tavola periodica', 'Memorizzare i gruppi e i periodi principali della tavola periodica.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 2 AND date = '2025-01-23' AND teaching_subject_title = 'Chimica')), '2025-02-17', 'Relazione sull’elettrolisi', 'Scrivere una breve relazione sull’esperimento condotto in classe.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 5 AND date = '2025-01-17' AND teaching_subject_title = 'Storia')), '2025-02-24', 'Esercizi Rivoluzione Francese', 'Completa le domande a pagina 150.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 4 AND date = '2025-01-18' AND teaching_subject_title = 'Inglese')), '2025-02-15', 'Scrivi un saggio', 'Scrivi un saggio di 500 parole.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-21' AND teaching_subject_title = 'Geometria')),'2025-02-28','Esercizi sulla circonferenza','Completa gli esercizi 1-10 del capitolo 3.'),
    ((SELECT teaching_timeslot_id FROM signed_hour WHERE teaching_timeslot_id = (SELECT id FROM teaching_timeslot WHERE hour = 3 AND date = '2025-01-23' AND teaching_subject_title = 'Geometria')),'2025-02-20','Proprietà Circonferenza','Risolvi i problemi 5-8 a pagina 85.');

INSERT INTO homework_chat (homework_signed_hour_teaching_timeslot_id, title, student_id)
VALUES
    ((SELECT signed_hour_teaching_timeslot_id FROM homework WHERE title = 'Esercizi sulle equazioni'), 'Dubbio esercizio 3', (SELECT id FROM student WHERE username = 'luca.verdi')),
    ((SELECT signed_hour_teaching_timeslot_id FROM homework WHERE title = 'Esercizi Rivoluzione Francese'), 'Chiarimenti compito', (SELECT id FROM student WHERE username = 'marco.neri')),
    ((SELECT signed_hour_teaching_timeslot_id FROM homework WHERE title = 'Esercizi Rivoluzione Francese'), 'Dubbi su un esercizio', (SELECT id FROM student WHERE username = 'luca.verdi')),
    ((SELECT signed_hour_teaching_timeslot_id FROM homework WHERE title = 'Esercizi Rivoluzione Francese'), 'Scrivi un saggio', (SELECT id FROM student WHERE username = 'elisa.verdi'));

INSERT INTO ticket (parent_id, teacher_id, title, category, datetime, closed, solved)
VALUES
    (null, (SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Problema con la piattaforma', 'Piattaforma', '2025-01-15 10:00:00', false, false),
    (null, (SELECT id FROM teacher WHERE username = 'marco.gialli'), 'Chiarimento sulle lezioni', 'Didattica', '2025-03-20 14:00:00', false, false),
    ((SELECT id FROM parent WHERE username = 'paolo.verdi'), null, 'Richiesta informazioni sulla gita', 'Gite scolastiche', '2025-01-17 10:32:46', false, false),
    ((SELECT id FROM parent WHERE username = 'giulia.neri'), null, 'Problema iscrizione', 'Iscrizione', '2025-02-01 10:00:00', false, false),
    ((SELECT id FROM parent WHERE username = 'roberto.verdi'), null, 'Richiesta documenti', 'Documenti', '2025-02-02 11:30:00', false, false);

INSERT INTO message (ticket_id, homework_chat_id, parent_id, secretary_id, student_id, teacher_id, date_time, text)
VALUES
    (null, (SELECT id FROM homework_chat WHERE title = 'Chiarimenti compito'), null, null, (SELECT id FROM student WHERE username = 'marco.neri'), null, '2025-02-05 14:30:00', 'Non capisco la domanda 3.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Chiarimenti compito'), null, null, null, (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-02-05 14:35:00', 'Quale parte non ti è chiara?'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Chiarimenti compito'), null, null, (SELECT id FROM student WHERE username = 'marco.neri'), null, '2025-02-05 14:40:00', 'Il modo in cui calcolare il determinante.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi su un esercizio'), null, null, (SELECT id FROM student WHERE username = 'luca.verdi'), null, '2025-02-06 09:00:00', 'Ho difficoltà con l’esercizio 5.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi su un esercizio'), null, null, null, (SELECT id FROM teacher WHERE username = 'riccardo.blui'), '2025-02-06 09:15:00', 'Prova a dividere il problema in piccoli passi.'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Dubbi su un esercizio'), null, null, (SELECT id FROM student WHERE username = 'luca.verdi'), null, '2025-02-06 09:20:00', 'Grazie, ora è più chiaro!'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Scrivi un saggio'), null, null, (SELECT id FROM student WHERE username = 'elisa.verdi'), null, '2025-02-08 10:00:00', 'Il formato del saggio è obbligatorio?'),
    (null, (SELECT id FROM homework_chat WHERE title = 'Scrivi un saggio'), null, null, null, (SELECT id FROM teacher WHERE username = 'anna.blui'), '2025-02-08 10:10:00', 'Sì, segui le linee guida della pagina 12 del libro.'),
    ((SELECT id FROM ticket WHERE title = 'Problema con la piattaforma'), null, null, null, null, (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-03-15 10:05:00', 'Buongiorno, sto riscontrando problemi nell’accedere alla piattaforma.'),
    ((SELECT id FROM ticket WHERE title = 'Problema con la piattaforma'), null, null, (SELECT id FROM secretary WHERE username = 'giorgio.neri'), null, null, '2025-03-15 10:10:00', 'Buongiorno, siamo a conoscenza del problema. È dovuto a un aggiornamento in corso.'),
    ((SELECT id FROM ticket WHERE title = 'Problema con la piattaforma'), null, null, null, null, (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-03-15 10:15:00', 'Grazie per l’informazione. Quanto tempo richiederà la risoluzione?'),
    ((SELECT id FROM ticket WHERE title = 'Problema con la piattaforma'), null, null, (SELECT id FROM secretary WHERE username = 'giorgio.neri'), null, null, '2025-03-15 10:20:00', 'Il problema dovrebbe essere risolto entro un’ora. Grazie per la pazienza.'),
    ((SELECT id FROM ticket WHERE title = 'Chiarimento sulle lezioni'), null, null, null, null, (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-03-20 14:10:00', 'Buongiorno, ho bisogno di chiarimenti sull’organizzazione delle lezioni.'),
    ((SELECT id FROM ticket WHERE title = 'Chiarimento sulle lezioni'), null, null, (SELECT id FROM secretary WHERE username = 'giorgio.neri'), null, null, '2025-03-20 14:15:00', 'Buongiorno Professore, quali aspetti vorrebbe chiarire?'),
    ((SELECT id FROM ticket WHERE title = 'Chiarimento sulle lezioni'), null, null, null, null, (SELECT id FROM teacher WHERE username = 'marco.gialli'), '2025-03-20 14:20:00', 'Mi servirebbero informazioni sulle date dei prossimi incontri di recupero.'),
    ((SELECT id FROM ticket WHERE title = 'Richiesta informazioni sulla gita'), null,  (SELECT id FROM parent WHERE username = 'paolo.verdi'), null, null, null, '2025-01-17 09:10:00', 'Vorrei sapere maggiori dettagli sull''organizzazione della gita.'),
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
    ((SELECT id FROM class_timetable WHERE school_class_id = (SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024) AND end_validity IS NULL), '2024-12-25');

INSERT INTO student_school_class (school_class_id, student_id)
VALUES
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), (SELECT id FROM student WHERE username = 'marco.neri')),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'luca.verdi')),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'sofia.bianchi')),
    ((SELECT id FROM school_class WHERE number = 3 AND letter = 'C' AND year = 2024), (SELECT id FROM student WHERE username = 'giulia.rossi')),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'elisa.verdi')),
    ((SELECT id FROM school_class WHERE number = 5 AND letter = 'A' AND year = 2024), (SELECT id FROM student WHERE username = 'franco.blui')),
    ((SELECT id FROM school_class WHERE number = 2 AND letter = 'B' AND year = 2024), (SELECT id FROM student WHERE username = 'chiara.neri'));

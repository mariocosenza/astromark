DROP SCHEMA IF EXISTS astromark CASCADE;
CREATE SCHEMA astromark;
SET SCHEMA 'astromark';

CREATE TABLE school
(
    code                       VARCHAR(7) CHECK (code ~ '^SS\d{5}$'),
    phone_number               INT          NOT NULL,
    address                    VARCHAR(512) NOT NULL,
    name                       VARCHAR(256),
    email                      VARCHAR(256) NOT NULL,
    school_principal_full_name VARCHAR(129), -- name + surname + one white space

    CONSTRAINT pk_school_code PRIMARY KEY (code)
);

CREATE TABLE school_class
(
    id          SERIAL,
    school_code VARCHAR(7) NOT NULL,
    number      SMALLINT   NOT NULL,
    letter      VARCHAR(2) NOT NULL, -- allowed combination like 'BS'
    year        INT        NOT NULL,

    CONSTRAINT pk_school_class PRIMARY KEY (id),
    CONSTRAINT fk_school_class_school FOREIGN KEY (school_code) REFERENCES school (code) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE communication
(
    id              SERIAL,
    school_class_id SERIAL,
    title           VARCHAR(256) NOT NULL,
    description     TEXT DEFAULT '',
    date            DATE DEFAULT NOW(),

    CONSTRAINT pk_communication PRIMARY KEY (id),
    CONSTRAINT fk_communication_school_class FOREIGN KEY (school_class_id) REFERENCES school_class (id) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE subject
(
    title VARCHAR(64),

    CONSTRAINT pk_subject_title PRIMARY KEY (title)
);

CREATE TABLE study_plan
(
    school_class_id SERIAL,
    title           VARCHAR(64) NOT NULL,

    CONSTRAINT pk_study_plan PRIMARY KEY (school_class_id),
    CONSTRAINT fk_study_plan_school_class FOREIGN KEY (school_class_id) REFERENCES school_class (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE study_plan_subject
(
    subject_title              VARCHAR(255),
    study_plan_school_class_id SERIAL,

    CONSTRAINT pk_study_plan_subject PRIMARY KEY (subject_title, study_plan_school_class_id),
    CONSTRAINT fk_study_plan_subject_school_class FOREIGN KEY (study_plan_school_class_id) REFERENCES study_plan (school_class_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_study_plan_subject_subject FOREIGN KEY (subject_title) REFERENCES subject (title) ON UPDATE CASCADE ON DELETE NO ACTION
);



CREATE TYPE PENDINGSTATE AS ENUM ('FIRST_LOGIN', 'NORMAL', 'REMOVE');



CREATE TABLE parent
(
    id                  UUID         DEFAULT GEN_RANDOM_UUID(),
    school_code         VARCHAR(7)   NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              VARCHAR(16),
    birth_date          DATE         NOT NULL, -- should be at least eighteen years old
    residential_address VARCHAR(512) NOT NULL,
    gender              BOOL         DEFAULT FALSE,
    legal_guardian      BOOL         DEFAULT FALSE,
    PENDING_STATE       PENDINGSTATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_parent PRIMARY KEY (id),
    CONSTRAINT uk_parent_tax_id UNIQUE (tax_id),
    CONSTRAINT fk_parent_school FOREIGN KEY (school_code) REFERENCES school (code)
);

CREATE TABLE secretary
(
    id                  UUID         DEFAULT GEN_RANDOM_UUID(),
    school_code         VARCHAR(7)   NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              VARCHAR(16),
    birth_date          DATE         NOT NULL, -- should be at least eighteen years old
    residential_address VARCHAR(512) NOT NULL,
    gender              BOOL         DEFAULT FALSE,
    PENDING_STATE       PENDINGSTATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_secretary PRIMARY KEY (id),
    CONSTRAINT uk_secretary_tax_id UNIQUE (tax_id),
    CONSTRAINT fk_secretary_school FOREIGN KEY (school_code) REFERENCES school (code)
);

CREATE TABLE teacher
(
    id                  UUID         DEFAULT GEN_RANDOM_UUID(),
    school_code         VARCHAR(7)   NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              VARCHAR(16),
    birth_date          DATE         NOT NULL, -- should be at least eighteen years old
    residential_address VARCHAR(512) NOT NULL,
    gender              BOOL         DEFAULT FALSE,
    PENDING_STATE       PENDINGSTATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_teacher PRIMARY KEY (id),
    CONSTRAINT uk_teacher_tax_id UNIQUE (tax_id),
    CONSTRAINT fk_teacher_school FOREIGN KEY (school_code) REFERENCES school (code)
);


CREATE TABLE teacher_class
(
    teacher_id      UUID,
    school_class_id SERIAL,
    coordinator     BOOLEAN DEFAULT FALSE NOT NULL,


    CONSTRAINT pk_teacher_class PRIMARY KEY (teacher_id, school_class_id),
    CONSTRAINT fk_teacher_class_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_teacher_class_school_class FOREIGN KEY (school_class_id) REFERENCES school_class (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE teaching
(
    subject_title    VARCHAR(255),
    teacher_id       UUID,
    type_of_activity VARCHAR(64) NOT NULL,

    CONSTRAINT pk_teaching PRIMARY KEY (teacher_id, subject_title),
    CONSTRAINT fk_teaching_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_teaching_subject FOREIGN KEY (subject_title) REFERENCES subject (title) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE student
(
    id                  UUID         DEFAULT GEN_RANDOM_UUID(),
    school_code         VARCHAR(7)   NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              VARCHAR(16),
    birth_date          DATE         NOT NULL, -- should be at least ten years old
    residential_address VARCHAR(512) NOT NULL,
    attitude            TEXT,
    graduation_mark     SMALLINT CHECK (graduation_mark >= 60 AND graduation_mark <= 100),
    gender              BOOL         DEFAULT FALSE,
    PENDING_STATE       PENDINGSTATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_student PRIMARY KEY (id),
    CONSTRAINT uk_student_tax_id UNIQUE (tax_id),
    CONSTRAINT fk_student_school FOREIGN KEY (school_code) REFERENCES school (code)
);

CREATE TABLE delay
(
    id                  UUID               DEFAULT GEN_RANDOM_UUID(),
    student_id          UUID      NOT NULL,
    needs_justification BOOL               DEFAULT TRUE,
    justified           BOOL CHECK ((needs_justification = FALSE AND justified = TRUE) OR needs_justification = TRUE),
    justification_text  VARCHAR(512),
    date_time           TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_delay PRIMARY KEY (id),
    CONSTRAINT fk_delay_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE absence
(
    id                  UUID          DEFAULT GEN_RANDOM_UUID(),
    student_id          UUID NOT NULL,
    needs_justification BOOL          DEFAULT TRUE,
    justified           BOOL CHECK ((needs_justification = FALSE AND justified = TRUE) OR needs_justification = TRUE),
    justification_text  VARCHAR(512),
    date                DATE NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_absence PRIMARY KEY (id),
    CONSTRAINT fk_absence_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE note
(
    id          UUID                  DEFAULT GEN_RANDOM_UUID(),
    student_id  UUID         NOT NULL,
    viewed      BOOL                  DEFAULT TRUE,
    description VARCHAR(512) NOT NULL,
    date        DATE         NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_note PRIMARY KEY (id),
    CONSTRAINT fk_note_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TYPE MARKTYPE AS ENUM ('ORAL', 'WRITTEN', 'LABORATORY');

CREATE TABLE mark
(
    id                     SERIAL,
    student_id             UUID                       NOT NULL,
    teaching_subject_title VARCHAR(255)               NOT NULL,
    teaching_teacher_id    UUID                       NOT NULL,
    date                   DATE                       NOT NULL,
    type                   MARKTYPE DEFAULT 'WRITTEN' NOT NULL,
    description            VARCHAR(512),
    mark                   DOUBLE PRECISION CHECK (mark >= 0 AND mark <= 10),


    CONSTRAINT pk_mark PRIMARY KEY (id),
    CONSTRAINT fk_mark_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mark_teaching FOREIGN KEY (teaching_subject_title, teaching_teacher_id) REFERENCES teaching (subject_title, teacher_id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE semester_report
(
    id             SERIAL,
    first_semester BOOL DEFAULT TRUE  NOT NULL,
    public         BOOL DEFAULT FALSE NOT NULL,
    passed         BOOL DEFAULT FALSE NOT NULL,
    viewed         BOOL DEFAULT FALSE NOT NULL,
    year           SMALLINT CHECK (year >= 2000), -- year at least 2000
    student_id     UUID,

    CONSTRAINT pk_semester_report PRIMARY KEY (id),
    CONSTRAINT uk_semester_report UNIQUE (first_semester, year, student_id),
    CONSTRAINT fk_semester_report_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE semester_report_mark
(
    id            SERIAL,
    subject_title VARCHAR(64) NOT NULL,
    semester_id   SERIAL      NOT NULL,
    mark          SMALLINT    NOT NULL CHECK (mark >= 0 AND mark <= 10),

    CONSTRAINT pk_semester_report_mark PRIMARY KEY (id),
    CONSTRAINT uk_semester_report_mark UNIQUE (subject_title, semester_id),
    CONSTRAINT fk_semester_report_mark_semester_report FOREIGN KEY (semester_id) REFERENCES semester_report (id),
    CONSTRAINT fk_semester_report_mark_subject FOREIGN KEY (subject_title) REFERENCES subject (title)
);

CREATE TABLE class_timetable
(
    id              INT GENERATED ALWAYS AS IDENTITY,
    school_class_id SERIAL NOT NULL,
    expected_hours  SMALLINT        DEFAULT 27 NOT NULL CHECK (expected_hours >= 0 AND expected_hours <= 40),
    start_validity  DATE   NOT NULL DEFAULT NOW(),
    end_validity    DATE CHECK (end_validity IS NULL OR end_validity > start_validity),

    CONSTRAINT pk_class_timetable PRIMARY KEY (id),
    CONSTRAINT fk_class_timetable_school_class FOREIGN KEY (school_class_id) REFERENCES school_class (id)
);

CREATE TABLE reception_timetable
(
    id                  INT GENERATED ALWAYS AS IDENTITY,
    teacher_id          UUID NOT NULL,
    text_info_reception TEXT,
    start_validity      DATE NOT NULL DEFAULT NOW(),
    end_validity        DATE CHECK (end_validity IS NULL OR end_validity > start_validity),

    CONSTRAINT pk_reception_timetable PRIMARY KEY (id),
    CONSTRAINT fk_reception_timetable_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);


CREATE TABLE teaching_timeslot
(

    id                 INT GENERATED ALWAYS AS IDENTITY,
    class_timetable_id INT                                      NOT NULL,
    hour               SMALLINT CHECK (hour >= 1 AND hour <= 8) NOT NULL,
    date               DATE                                     NOT NULL,

    CONSTRAINT pk_teaching_timeslot PRIMARY KEY (id),
    CONSTRAINT uk_teaching_timeslot UNIQUE (class_timetable_id, hour, date),
    CONSTRAINT fk_teaching_timeslot_class_timetable FOREIGN KEY (class_timetable_id) REFERENCES class_timetable (id) ON UPDATE CASCADE ON DELETE RESTRICT
);



CREATE TABLE signed_hour
(
    teaching_timeslot_id INT,
    teacher_id           UUID                    NOT NULL,
    time_sign            TIMESTAMP DEFAULT NOW() NOT NULL,
    substitution         BOOL      DEFAULT FALSE NOT NULL,

    CONSTRAINT pk_signed_hour PRIMARY KEY (teaching_timeslot_id),
    CONSTRAINT fk_signed_hour_teaching_timeslot FOREIGN KEY (teaching_timeslot_id) REFERENCES teaching_timeslot (id),
    CONSTRAINT fk_signed_hour_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);



CREATE TABLE reception_timeslot
(

    id                     INT GENERATED ALWAYS AS IDENTITY,
    reception_timetable_id INT                                      NOT NULL,
    hour                   SMALLINT CHECK (hour >= 1 AND hour <= 8) NOT NULL,
    date                   DATE                                     NOT NULL,
    capacity               SMALLINT                                 NOT NULL CHECK (capacity > 0)                       DEFAULT 6,
    booked                 SMALLINT                                 NOT NULL CHECK (booked <= capacity AND booked >= 0) DEFAULT 0,
    mode                   VARCHAR(128)                             NOT NULL,

    CONSTRAINT pk_reception_timeslot PRIMARY KEY (id),
    CONSTRAINT uk_reception_timeslot UNIQUE (reception_timetable_id, hour, date),
    CONSTRAINT fk_reception_timeslot_reception_timetable FOREIGN KEY (reception_timetable_id) REFERENCES reception_timetable (id) ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE reception_booking
(
    parent_id             UUID,
    reception_timeslot_id INT,
    booking_order         SMALLINT CHECK (booking_order >= 1) NOT NULL,
    confirmed             BOOLEAN DEFAULT FALSE               NOT NULL,
    refused               BOOLEAN DEFAULT FALSE               NOT NULL,

    CONSTRAINT pk_reception_booking PRIMARY KEY (parent_id, reception_timeslot_id),
    CONSTRAINT fk_reception_booking_parent FOREIGN KEY (parent_id) REFERENCES parent (id),
    CONSTRAINT fk_reception_booking_reception_timeslot FOREIGN KEY (reception_timeslot_id) REFERENCES reception_timeslot (id)
);

CREATE TABLE class_activity
(
    signed_hour_teaching_timeslot_id SERIAL,
    description                      TEXT,
    title                            VARCHAR(256) NOT NULL,

    CONSTRAINT pk_class_activity PRIMARY KEY (signed_hour_teaching_timeslot_id),
    CONSTRAINT fk_class_activity_signed_hour FOREIGN KEY (signed_hour_teaching_timeslot_id) REFERENCES signed_hour (teaching_timeslot_id)
);

CREATE TABLE homework
(
    signed_hour_teaching_timeslot_id SERIAL,
    due_date                         DATE         NOT NULL CHECK (due_date > NOW()),
    description                      TEXT,
    title                            VARCHAR(256) NOT NULL,

    CONSTRAINT pk_homework PRIMARY KEY (signed_hour_teaching_timeslot_id),
    CONSTRAINT fk_homework_signed_hour FOREIGN KEY (signed_hour_teaching_timeslot_id) REFERENCES signed_hour (teaching_timeslot_id)
);

CREATE TABLE homework_chat
(
    id                                        UUID DEFAULT gen_random_uuid(),
    homework_signed_hour_teaching_timeslot_id SERIAL             NOT NULL,
    title                                     VARCHAR(128)       NOT NULL,
    student_id                                UUID               NOT NULL,
    completed                                 BOOL DEFAULT FALSE NOT NULL,


    CONSTRAINT pk_homework_chat PRIMARY KEY (id),
    CONSTRAINT fk_homework_chat FOREIGN KEY (student_id) REFERENCES student (id),
    CONSTRAINT fk_homework_chat_homework FOREIGN KEY (homework_signed_hour_teaching_timeslot_id) REFERENCES homework (signed_hour_teaching_timeslot_id)
);

CREATE TABLE ticket
(
    id         UUID DEFAULT gen_random_uuid(),
    parent_id  UUID,
    teacher_id UUID CHECK ((parent_id ISNULL AND teacher_id IS NOT NULL) OR
                           (parent_id IS NOT NULL AND teacher_id ISNULL)),
    title      VARCHAR(128)       NOT NULL,
    category   VARCHAR(64),
    dateTime   TIMESTAMP          NOT NULL DEFAULT NOW(),
    closed     BOOL DEFAULT FALSE NOT NULL,
    solved     BOOL DEFAULT FALSE NOT NULL,

    CONSTRAINT pk_ticket PRIMARY KEY (id),
    CONSTRAINT fk_ticket_parent FOREIGN KEY (parent_id) REFERENCES parent (id),
    CONSTRAINT fk_ticket_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);

CREATE TABLE message
(
    id               UUID      DEFAULT gen_random_uuid(),
    ticket_id        UUID,
    homework_chat_id UUID,
    parent_id        UUID,
    secretary_id     UUID,
    student_id       UUID,
    teacher_id       UUID,
    date_time        TIMESTAMP DEFAULT NOW() NOT NULL,
    text             TEXT,
    attachment       VARCHAR(1024),


    CONSTRAINT pk_message PRIMARY KEY (id),
    CONSTRAINT fk_message_ticket FOREIGN KEY (ticket_id) REFERENCES ticket (id),
    CONSTRAINT fk_message_homework_chat FOREIGN KEY (homework_chat_id) REFERENCES homework_chat (id),
    CONSTRAINT fk_message_student FOREIGN KEY (student_id) REFERENCES student (id),
    CONSTRAINT fk_message_parent FOREIGN KEY (parent_id) REFERENCES parent (id),
    CONSTRAINT fk_message_secretary FOREIGN KEY (secretary_id) REFERENCES secretary (id),
    CONSTRAINT fk_message_teacher FOREIGN KEY (teacher_id) REFERENCES teacher (id)
);


CREATE TABLE student_parent
(
    student_id UUID DEFAULT gen_random_uuid(),
    parent_id  UUID DEFAULT gen_random_uuid(),

    CONSTRAINT pk_student_parent PRIMARY KEY (student_id, parent_id),
    CONSTRAINT fk_student_parent_student FOREIGN KEY (student_id) REFERENCES student (id),
    CONSTRAINT fk_student_parent_parent FOREIGN KEY (parent_id) REFERENCES parent (id)

);


CREATE TABLE red_date
(
    date               DATE NOT NULL,
    class_timetable_id SERIAL,

    CONSTRAINT pk_red_date PRIMARY KEY (class_timetable_id, date),
    CONSTRAINT fk_red_date_class_timetable FOREIGN KEY (class_timetable_id) REFERENCES class_timetable (id)

);

CREATE TABLE student_school_class
(
    school_class_id SERIAL,
    student_id      UUID,

    CONSTRAINT pk_student_school_class PRIMARY KEY (school_class_id, student_id),
    CONSTRAINT fk_student_school_class_school_class FOREIGN KEY (school_class_id) REFERENCES school_class(id),
    CONSTRAINT fk_student_school_class_student FOREIGN KEY (student_id) REFERENCES student(id)
);
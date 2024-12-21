CREATE SCHEMA ASTROMARK;
SET SCHEMA 'astromark';

CREATE TABLE school
(
    code                       CHARACTER(7) CHECK (code ~ '^SS\d{5}$'),
    phone_number               INT          NOT NULL,
    address                    VARCHAR(512) NOT NULL,
    name                       VARCHAR(256),
    email                      VARCHAR(256) NOT NULL,
    school_principal_full_name VARCHAR(129), -- name + surname + one white space

    CONSTRAINT pk_school_code PRIMARY KEY (code)
);

CREATE TABLE school_class
(
    id          serial,
    school_code CHARACTER(7) NOT NULL,
    number      SMALLINT     NOT NULL,
    letter      CHARACTER(2) NOT NULL, -- allowed combination like 'BS'
    year        INT          NOT NULL,

    CONSTRAINT pk_school_class PRIMARY KEY (id),
    CONSTRAINT fk_school_class_school FOREIGN KEY (school_code) REFERENCES school (code) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE communication
(
    id    serial,
    school_class_id serial,
    title VARCHAR(256) NOT NULL,
    description TEXT DEFAULT '',
    date DATE DEFAULT now(),

    CONSTRAINT pk_communication PRIMARY KEY (id),
    CONSTRAINT fk_communication_school_class FOREIGN KEY (school_class_id) REFERENCES school_class(id) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE subject
(
    title VARCHAR(64),

    CONSTRAINT pk_subject_title PRIMARY KEY (title)
);

CREATE TABLE study_plan
(
    school_class_id serial,
    title           VARCHAR(64),

    CONSTRAINT pk_study_plan PRIMARY KEY (school_class_id),
    CONSTRAINT fk_study_plan_school_class FOREIGN KEY (school_class_id) REFERENCES school_class (school_code) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE study_plan_subject
(
    subject_title              VARCHAR(64),
    study_plan_school_class_id serial,

    CONSTRAINT pk_study_plan_subject PRIMARY KEY (subject_title, study_plan_school_class_id),
    CONSTRAINT fk_study_plan_subject_school_class FOREIGN KEY (study_plan_school_class_id) REFERENCES school_class (school_code) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_study_plan_subject_subject FOREIGN KEY (subject_title) REFERENCES subject (title) ON UPDATE CASCADE ON DELETE NO ACTION
);



CREATE TYPE PENDING_STATE AS ENUM ('FIRST_LOGIN', 'NORMAL', 'REMOVE');




CREATE TABLE parent
(
    id                  UUID          DEFAULT GEN_RANDOM_UUID(),
    school_code         CHARACTER(7) NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              CHARACTER(16),
    birth_date          DATE         NOT NULL CHECK (NOW() - birth_date = 6570), -- eighteen year difference
    residential_address VARCHAR(512) NOT NULL,
    gender              BOOL          DEFAULT FALSE,
    legal_guardian      BOOL          DEFAULT FALSE,
    pending_state       PENDING_STATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_parent PRIMARY KEY (id),
    CONSTRAINT uk_parent_tax_id UNIQUE (tax_id)
);

CREATE TABLE secretary
(
    id                  UUID          DEFAULT GEN_RANDOM_UUID(),
    school_code         CHARACTER(7) NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              CHARACTER(16),
    birth_date          DATE         NOT NULL CHECK (NOW() - birth_date = 6570), -- eighteen year difference
    residential_address VARCHAR(512) NOT NULL,
    gender              BOOL          DEFAULT FALSE,
    pending_state       PENDING_STATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_secretary PRIMARY KEY (id),
    CONSTRAINT uk_secretary_tax_id UNIQUE (tax_id)
);

CREATE TABLE teacher
(
    id                  UUID          DEFAULT GEN_RANDOM_UUID(),
    school_code         CHARACTER(7) NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              CHARACTER(16),
    birth_date          DATE         NOT NULL CHECK (NOW() - birth_date = 6570), -- eighteen year difference
    residential_address VARCHAR(512) NOT NULL,
    gender              BOOL          DEFAULT FALSE,
    pending_state       PENDING_STATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_teacher PRIMARY KEY (id),
    CONSTRAINT uk_teacher_tax_id UNIQUE (tax_id)
);


CREATE TABLE teacher_class
(
    teacher_id UUID,
    school_class_id SERIAL,
    coordinator BOOLEAN DEFAULT FALSE NOT NULL,


    CONSTRAINT pk_teacher_class PRIMARY KEY (teacher_id, school_class_id),
    CONSTRAINT fk_teacher_class_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_teacher_class_school_class FOREIGN KEY (school_class_id) REFERENCES school_class(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE teaching
(
    subject_title VARCHAR(64),
    teacher_id    UUID,
    type_of_activity VARCHAR(64) NOT NULL,

    CONSTRAINT pk_teaching PRIMARY KEY (teacher_id, subject_title),
    CONSTRAINT fk_teaching_teacher FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE CASCADE  ON UPDATE CASCADE,
    CONSTRAINT fk_teaching_subject FOREIGN KEY (subject_title) REFERENCES subject(title) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE student
(
    id                  UUID          DEFAULT GEN_RANDOM_UUID(),
    school_code         CHARACTER(7) NOT NULL,
    username            VARCHAR(256) NOT NULL,
    email               VARCHAR(256) NOT NULL,
    password            VARCHAR(512) NOT NULL CHECK (LENGTH(password) >= 8),
    name                VARCHAR(64)  NOT NULL,
    surname             VARCHAR(64)  NOT NULL,
    tax_id              CHARACTER(16),
    birth_date          DATE         NOT NULL CHECK (NOW() - birth_date = 3650), -- ten year difference
    residential_address VARCHAR(512) NOT NULL,
    attitude            TEXT,
    graduation_mark     SMALLINT CHECK (graduation_mark >= 60 AND graduation_mark <= 100),
    latest_school_class BIGINT,
    gender              BOOL          DEFAULT FALSE,
    pending_state       PENDING_STATE DEFAULT 'FIRST_LOGIN',

    CONSTRAINT pk_student PRIMARY KEY (id),
    CONSTRAINT uk_student_tax_id UNIQUE (tax_id)
);

CREATE TABLE delay
(
    id UUID  DEFAULT GEN_RANDOM_UUID(),
    student_id UUID NOT NULL,
    needs_justification BOOL DEFAULT TRUE,
    justified BOOL CHECK ((needs_justification = false AND justified = true) OR needs_justification = true),
    justification_text VARCHAR(512),
    date_time TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT pk_delay PRIMARY KEY (id),
    CONSTRAINT fk_delay_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE absence
(
    id UUID  DEFAULT GEN_RANDOM_UUID(),
    student_id UUID NOT NULL,
    needs_justification BOOL DEFAULT TRUE,
    justified BOOL CHECK ((needs_justification = false AND justified = true) OR needs_justification = true),
    justification_text VARCHAR(512),
    date DATE NOT NULL DEFAULT now(),

    CONSTRAINT pk_absence PRIMARY KEY (id),
    CONSTRAINT fk_absence_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE note
(
    id UUID  DEFAULT GEN_RANDOM_UUID(),
    student_id UUID NOT NULL,
    viewed BOOL DEFAULT TRUE,
    description VARCHAR(512) NOT NULL,
    date DATE NOT NULL DEFAULT now(),

    CONSTRAINT pk_note PRIMARY KEY (id),
    CONSTRAINT fk_note_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TYPE MARK_TYPE AS ENUM ('ORAL', 'WRITTEN', 'LABORATORY');

CREATE TABLE mark
(
    id SERIAL,
    student_id UUID NOT NULL,
    teaching_subject_title VARCHAR(64) NOT NULL,
    teaching_teacher_id    UUID NOT NULL,
    date DATE NOT NULL,
    type MARK_TYPE DEFAULT 'WRITTEN' NOT NULL,
    description VARCHAR(512),
    mark DOUBLE PRECISION CHECK (mark >= 0  AND mark <= 10),


    CONSTRAINT pk_mark PRIMARY KEY (id),
    CONSTRAINT fk_mark_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mark_teaching FOREIGN KEY (teaching_subject_title) REFERENCES teaching(subject_title, teacher_id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE semester_report
(
    first_semester BOOL DEFAULT TRUE NOT NULL,
    public BOOL DEFAULT FALSE NOT NULL,
    passed BOOL DEFAULT FALSE NOT NULL,
    viewed BOOL DEFAULT FALSE NOT NULL,
    year SMALLINT CHECK (year >= 2000), -- year at least 2000
    student_id UUID,

    CONSTRAINT pk_semester_report PRIMARY KEY (first_semester, year, student_id),
    CONSTRAINT fk_semester_report_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE semester_report_mark
(
    id SERIAL,
    subject_title VARCHAR(64) NOT NULL,
    semester_report_year SMALLINT NOT NULL,
    semester_report_student_id UUID NOT NULL,
    semester_report_first_semester BOOL NOT NULL,
    mark SMALLINT NOT NULL CHECK (mark >= 0 AND  mark <= 10),

    CONSTRAINT pk_semester_report_mark PRIMARY KEY (id),
    CONSTRAINT uk_semester_report_mark UNIQUE (subject_title, semester_report_first_semester, semester_report_student_id, semester_report_year),
    CONSTRAINT fk_semester_report_mark_semester_report FOREIGN KEY (semester_report_year, semester_report_student_id, semester_report_first_semester) REFERENCES semester_report(first_semester, student_id, year),
    CONSTRAINT fk_semester_report_mark_subject FOREIGN KEY (subject_title) REFERENCES subject(title)
);
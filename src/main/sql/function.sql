SET SCHEMA 'astromark';

CREATE OR REPLACE FUNCTION is_signed_hour_day_valid(
    p_teaching_timeslot_id INTEGER,
    p_date DATE
) RETURNS BOOLEAN AS $$
DECLARE
    expected_day WEEK_DAY;
    actual_day WEEK_DAY;
BEGIN
    -- Get the expected day from teaching_timeslot
    SELECT day INTO expected_day
    FROM teaching_timeslot
    WHERE id = p_teaching_timeslot_id;

     -- If teaching_timeslot_id does not exist, return FALSE
    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    -- Find the week day from given date
    actual_day :=
            CASE EXTRACT(ISODOW FROM p_date)
                WHEN 1 THEN 'MONDAY'
                WHEN 2 THEN 'TUESDAY'
                WHEN 3 THEN 'WEDNESDAY'
                WHEN 4 THEN 'THURSDAY'
                WHEN 5 THEN 'FRIDAY'
                WHEN 6 THEN 'SATURDAY'
                WHEN 7 THEN 'SUNDAY'
                END;

    -- Compare effective day with expected day
    IF actual_day = expected_day THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION is_reception_booking_day_valid(
    p_reception_timeslot_id INTEGER,
    p_date DATE
) RETURNS BOOLEAN AS $$
DECLARE
    expected_day WEEK_DAY;
    actual_day WEEK_DAY;
BEGIN
    -- Get the expected day from reception_timeslot
    SELECT day INTO expected_day
    FROM reception_timeslot
    WHERE id = p_reception_timeslot_id;

    -- If reception_timeslot_id does not exist, return FALSE
    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    -- Find the week day from given date
    actual_day :=
            CASE EXTRACT(ISODOW FROM p_date)
                WHEN 1 THEN 'MONDAY'
                WHEN 2 THEN 'TUESDAY'
                WHEN 3 THEN 'WEDNESDAY'
                WHEN 4 THEN 'THURSDAY'
                WHEN 5 THEN 'FRIDAY'
                WHEN 6 THEN 'SATURDAY'
                WHEN 7 THEN 'SUNDAY'
                END;

    -- Compare effective day with expected day
    IF actual_day = expected_day THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$ LANGUAGE plpgsql;

ALTER TABLE signed_hour
ADD CONSTRAINT ck_signed_hour_day CHECK (is_signed_hour_day_valid(teaching_timeslot_id, date));

ALTER TABLE reception_booking
ADD CONSTRAINT ck_signed_hour_day CHECK (is_reception_booking_day_valid(reception_timeslot_id, date));
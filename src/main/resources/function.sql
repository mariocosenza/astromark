SET SCHEMA 'astromark';

CREATE OR REPLACE FUNCTION check_single_role(
    p_student_id UUID,
    p_teacher_id UUID,
    p_parent_id UUID,
    p_secretary_id UUID
) RETURNS BOOLEAN AS
$$
DECLARE
    non_null_count INTEGER := 0;
BEGIN
    -- Increment the count for each non-null ID
    IF p_student_id IS NOT NULL THEN
        non_null_count := non_null_count + 1;
    END IF;
    IF p_teacher_id IS NOT NULL THEN
        non_null_count := non_null_count + 1;
    END IF;
    IF p_parent_id IS NOT NULL THEN
        non_null_count := non_null_count + 1;
    END IF;
    IF p_secretary_id IS NOT NULL THEN
        non_null_count := non_null_count + 1;
    END IF;

    -- Return TRUE if exactly one ID is not null
    RETURN (non_null_count = 1);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_ticket_or_homework_chat_id_valid(
    p_ticket_id UUID,
    p_homework_chat_id UUID
) RETURNS BOOLEAN AS
$$
DECLARE
    non_null_count INTEGER := 0;
BEGIN
    -- Increment count for each non-null ID
    IF p_ticket_id IS NOT NULL THEN
        non_null_count := non_null_count + 1;
    END IF;
    IF p_homework_chat_id IS NOT NULL THEN
        non_null_count := non_null_count + 1;
    END IF;

    -- Return TRUE if exactly one ID is not null
    RETURN (non_null_count = 1);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION calculate_booking_order(
    p_reception_timeslot_id INTEGER
) RETURNS SMALLINT AS
$$
DECLARE
    cap       SMALLINT;
    book      SMALLINT;
    order_val SMALLINT;
BEGIN
    -- Retrieve capacity and booked from reception_timeslot
    SELECT capacity, booked
    INTO cap, book
    FROM reception_timeslot
    WHERE id = p_reception_timeslot_id;

    -- Raise an error if the reception_timeslot_id does not exist
    IF NOT FOUND THEN
        RAISE EXCEPTION 'reception_timeslot_id % does not exist in reception_timeslot table.', p_reception_timeslot_id;
    END IF;

    -- Calculate booking_order as capacity - booked
    order_val := cap - book;

    -- Ensure booking_order is at least 1
    IF order_val < 1 THEN
        RAISE EXCEPTION 'No available bookings for reception_timeslot_id %.', p_reception_timeslot_id;
    END IF;

    RETURN order_val;
END;
$$ LANGUAGE plpgsql;


ALTER TABLE astromark.message
    ADD CONSTRAINT ck_message_role CHECK (check_single_role(student_id, teacher_id, parent_id, secretary_id));

ALTER TABLE astromark.message
    ADD CONSTRAINT ck_chat_type CHECK (check_ticket_or_homework_chat_id_valid(ticket_id, homework_chat_id));


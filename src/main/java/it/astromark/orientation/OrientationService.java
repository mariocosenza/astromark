package it.astromark.orientation;

import it.astromark.user.student.entity.Student;
import jakarta.validation.constraints.NotNull;
import org.hibernate.service.spi.ServiceException;

public interface OrientationService {
    /**
     * Determines the student's attitude by sending their marks to an AI orientation service.
     *
     * @param student The student whose attitude is to be assessed
     * @return A {@link String} representing the student's attitude or orientation
     * @throws ServiceException if the AI service call fails or returns an empty response
     */
    String attitude(@NotNull Student student);
}

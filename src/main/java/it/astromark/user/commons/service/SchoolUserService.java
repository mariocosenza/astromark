package it.astromark.user.commons.service;

import it.astromark.user.commons.dto.SchoolUserDetailed;
import it.astromark.user.commons.dto.SchoolUserResponse;
import it.astromark.user.commons.dto.SchoolUserUpdate;
import it.astromark.user.parent.entity.Parent;
import it.astromark.user.teacher.entity.Teacher;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Service interface for managing school user-related operations.
 * Provides methods to handle common functionalities for all types of school users.
 */
public interface SchoolUserService {
    /**
     * Checks if the specified parent is the parent of a specific student.
     *
     * @param parent    the `Parent` object
     * @param studentId the UUID of the student
     * @return true if the parent is the parent of the student, false otherwise
     * Pre-condition: The `parent` and `studentId` must not be null. The student must exist.
     * Post-condition: Returns true if the parent is associated with the specified student.
     */
    boolean isStudentParent(@NotNull Parent parent,@NotNull  UUID studentId);

    /**
     * Checks if the logged-in user is the parent of a specific student.
     *
     * @param studentId the UUID of the student
     * @return true if the logged-in user is the parent of the student, false otherwise
     * Pre-condition: The `studentId` must not be null. The student must exist.
     * Post-condition: Returns true if the logged-in user is associated as the parent of the student.
     */
    boolean isLoggedUserParent(@NotNull UUID studentId);

    /**
     * Checks if the logged-in teacher is responsible for a specific class.
     *
     * @param classId the ID of the class
     * @return true if the logged-in teacher is responsible for the class, false otherwise
     * Pre-condition: The `classId` must not be null. The class must exist.
     * Post-condition: Returns true if the logged-in teacher is associated with the class.
     */
    boolean isLoggedTeacherClass(@NotNull Integer classId);

    /**
     * Checks if a specific teacher is responsible for a specific class.
     *
     * @param teacher the `Teacher` object
     * @param classId the ID of the class
     * @return true if the teacher is responsible for the class, false otherwise
     * Pre-condition: The `teacher` and `classId` must not be null. The class must exist.
     * Post-condition: Returns true if the teacher is associated with the class.
     */
    boolean isTeacherClass(@NotNull Teacher teacher, @NotNull Integer classId);

    /**
     * Checks if the logged-in parent has a child in a specific class.
     *
     * @param classId the ID of the class
     * @return true if the logged-in parent has a child in the class, false otherwise
     * Pre-condition: The `classId` must not be null. The class must exist.
     * Post-condition: Returns true if the logged-in parent is associated with the class.
     */
    boolean isLoggedParentStudentClass(@NotNull Integer classId);

    /**
     * Updates the preferences for the logged-in user.
     *
     * @param schoolUserUpdate the object containing the updated preferences
     * @return a `SchoolUserResponse` object representing the updated user details
     * Pre-condition: The `schoolUserUpdate` must not be null and must contain valid preference data.
     * Post-condition: The user's preferences are updated, and a response object is returned.
     */
    SchoolUserResponse updatePreferences(@NotNull SchoolUserUpdate schoolUserUpdate);

    /**
     * Updates the address for the logged-in user.
     *
     * @param address the new address
     * @return a `SchoolUserResponse` object representing the updated user details
     * Pre-condition: The `address` must not be null or empty.
     * Post-condition: The user's address is updated, and a response object is returned.
     */
    SchoolUserResponse updateAddress(String address);

    /**
     * Checks if the logged-in teacher is responsible for a specific student.
     *
     * @param studentId the UUID of the student
     * @return true if the logged-in teacher is responsible for the student, false otherwise
     * Pre-condition: The `studentId` must not be null. The student must exist.
     * Post-condition: Returns true if the logged-in teacher is associated with the student.
     */
    boolean isLoggedTeacherStudent(@NotNull UUID studentId);

    /**
     * Checks if the logged-in student matches the specified student ID.
     *
     * @param studentId the UUID of the student
     * @return true if the logged-in student matches the specified ID, false otherwise
     * Pre-condition: The `studentId` must not be null. The student must exist.
     * Post-condition: Returns true if the logged-in student matches the specified ID.
     */
    boolean isLoggedStudent(@NotNull UUID studentId);

    /**
     * Retrieves detailed information about the logged-in user.
     *
     * @return a `SchoolUserDetailed` object containing detailed user information
     * Pre-condition: The user must be logged in.
     * Post-condition: Returns detailed information about the logged-in user.
     */
    SchoolUserDetailed getByIdDetailed();

}

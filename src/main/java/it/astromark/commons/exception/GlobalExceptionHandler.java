package it.astromark.commons.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String AUTHORIZATION_DENIED = "You are not allowed to access this resource";

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleGlobalDataException(DataAccessException exception) {
        log.info("DataAccess exception thrown: {}", exception.getLocalizedMessage());
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DataAccess exception thrown!");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        log.warn("Validation errors: {}", errors);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAuthorizationDeniedException(AuthorizationDeniedException exception) {
        log.info("Authorization denied: {}", exception.getLocalizedMessage());
        return new ExceptionResponse(HttpStatus.FORBIDDEN.value(), "Authorization denied " + exception.getLocalizedMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleAllRuntimeExceptions(RuntimeException exception) {
        log.error("An unexpected error occurred: {}", exception.getLocalizedMessage());
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: "
        );
    }
}

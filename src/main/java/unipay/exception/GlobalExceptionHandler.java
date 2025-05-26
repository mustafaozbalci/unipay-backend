package unipay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Catches exceptions thrown by controllers and services,
 * mapping them to appropriate HTTP status codes and response bodies.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles attempts to register a username that is already in use.
     * Returns HTTP 400 Bad Request with the error message.
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles attempts to register an email that is already in use.
     * Returns HTTP 400 Bad Request with the error message.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles cases where a user’s balance is insufficient for an operation.
     * Returns HTTP 400 Bad Request with the error message.
     */
    @ExceptionHandler(BalanceNotEnoughException.class)
    public ResponseEntity<?> handleBalanceNotEnoughException(BalanceNotEnoughException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles requests for a non-existent user.
     * Returns HTTP 404 Not Found with the error message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles requests for a non-existent restaurant.
     * Returns HTTP 404 Not Found with the error message.
     */
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<?> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles requests for a non-existent order.
     * Returns HTTP 404 Not Found with the error message.
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles illegal arguments passed to methods.
     * Returns HTTP 400 Bad Request with the error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles illegal state transitions or conflicts.
     * Returns HTTP 409 Conflict with the error message.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles security violations, such as unauthorized access.
     * Returns HTTP 403 Forbidden with the error message.
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    /**
     * Catches all other uncaught exceptions.
     * Returns HTTP 500 Internal Server Error with a generic message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}

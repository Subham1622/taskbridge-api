package com.taskbridge.notifications.exception;

/**
 * Exception thrown when a notification cannot be found within the current tenant context.
 * <p>
 * This exception is used to enforce organization scoping and ensure that notifications
 * are only accessible within the appropriate tenant boundaries.
 */
public class NotificationNotFoundException extends RuntimeException {

    /**
     * Constructs a new NotificationNotFoundException with the specified error message.
     *
     * @param message The custom error message describing the exception.
     */
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
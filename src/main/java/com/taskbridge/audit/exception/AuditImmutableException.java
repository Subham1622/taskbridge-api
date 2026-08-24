package com.taskbridge.audit.exception;

/**
 * Exception thrown when an attempt is made to update or delete an immutable audit record.
 * TaskBridge audit records are immutable and must never be modified or removed after creation.
 */
public class AuditImmutableException extends RuntimeException {

    /**
     * Constructs a new AuditImmutableException with the specified detail message.
     *
     * @param message The custom error message describing the immutability violation.
     */
    public AuditImmutableException(String message) {
        super(message);
    }
}
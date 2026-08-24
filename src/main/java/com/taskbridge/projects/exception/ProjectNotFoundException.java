package com.taskbridge.projects.exception;

/**
 * Exception thrown when a Project is not found.
 */
public class ProjectNotFoundException extends RuntimeException {
	public ProjectNotFoundException(String message) {
		super(message);
	}
}
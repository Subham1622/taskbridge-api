package com.taskbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TaskbridgeApiApplication is the entry point for the TaskBridge API application.
 * <p>
 * This class initializes and runs the Spring Boot application.
 * </p>
 * 
 * @author TaskBridge
 * @version 1.0.0
 */
@SpringBootApplication
public class TaskbridgeApiApplication {

	/**
	 * The main method serves as the entry point for the Java application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskbridgeApiApplication.class, args);
	}
}
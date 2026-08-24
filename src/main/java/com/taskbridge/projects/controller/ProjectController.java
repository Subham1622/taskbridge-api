package com.taskbridge.projects.controller;

import com.taskbridge.projects.dto.ProjectRequestDTO;
import com.taskbridge.projects.dto.ProjectResponseDTO;
import com.taskbridge.projects.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Project operations.
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping
	public ResponseEntity<ProjectResponseDTO> createProject(
			@Valid @RequestBody ProjectRequestDTO requestDTO,
			@RequestHeader("X-Tenant-ID") String tenantId) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(projectService.createProject(requestDTO, tenantId));
	}

	@PatchMapping("/{projectId}/status")
	public ResponseEntity<ProjectResponseDTO> updateProjectStatus(
			@PathVariable Long projectId,
			@RequestParam String status,
			@RequestHeader("X-Tenant-ID") String tenantId) {
		return ResponseEntity.ok(projectService.updateProjectStatus(projectId, status, tenantId));
	}

	@GetMapping("/team/{teamId}")
	public ResponseEntity<List<ProjectResponseDTO>> getProjectsByTeam(
			@PathVariable Long teamId,
			@RequestHeader("X-Tenant-ID") String tenantId) {
		return ResponseEntity.ok(projectService.getProjectsByTeam(teamId, tenantId));
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<Void> deleteProject(
			@PathVariable Long projectId,
			@RequestHeader("X-Tenant-ID") String tenantId) {
		projectService.deleteProject(projectId, tenantId);
		return ResponseEntity.noContent().build();
	}
}
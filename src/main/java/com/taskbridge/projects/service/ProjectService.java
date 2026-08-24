package com.taskbridge.projects.service;

import com.taskbridge.projects.dto.ProjectRequestDTO;
import com.taskbridge.projects.dto.ProjectResponseDTO;
import com.taskbridge.projects.entity.Project;
import com.taskbridge.projects.exception.ProjectNotFoundException;
import com.taskbridge.projects.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Project operations.
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

	private final ProjectRepository projectRepository;

	/**
	 * Creates a new project.
	 *
	 * @param requestDTO the project request DTO
	 * @param tenantId   the tenant ID
	 * @return the created project response DTO
	 */
	@Transactional
	public ProjectResponseDTO createProject(ProjectRequestDTO requestDTO, String tenantId) {
		logger.info("Creating project for tenant: {}", tenantId);

		Project project = new Project();
		project.setName(requestDTO.name());
		project.setDescription(requestDTO.description());
		project.setStatus(requestDTO.status());
		project.setTeamId(requestDTO.teamId());
		project.setTenantId(tenantId);

		Project savedProject = projectRepository.save(project);
		return mapToResponseDTO(savedProject);
	}

	/**
	 * Updates the status of a project.
	 *
	 * @param projectId the project ID
	 * @param status    the new status
	 * @param tenantId  the tenant ID
	 * @return the updated project response DTO
	 */
	@Transactional
	public ProjectResponseDTO updateProjectStatus(Long projectId, String status, String tenantId) {
		logger.info("Updating status of project {} for tenant: {}", projectId, tenantId);

		Project project = projectRepository.findByIdAndTenantId(projectId, tenantId)
        .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

		project.setStatus(status);
		Project updatedProject = projectRepository.save(project);
		return mapToResponseDTO(updatedProject);
	}

	/**
	 * Retrieves projects by team ID.
	 *
	 * @param teamId   the team ID
	 * @param tenantId the tenant ID
	 * @return list of project response DTOs
	 */
	public List<ProjectResponseDTO> getProjectsByTeam(Long teamId, String tenantId) {
		logger.info("Fetching projects for team {} and tenant: {}", teamId, tenantId);

		return projectRepository.findByTeamIdAndTenantId(teamId, tenantId)
				.stream()
				.map(this::mapToResponseDTO)
				.collect(Collectors.toList());
	}

	/**
	 * Deletes a project.
	 *
	 * @param projectId the project ID
	 * @param tenantId  the tenant ID
	 */
	@Transactional
	public void deleteProject(Long projectId, String tenantId) {
		logger.info("Deleting project {} for tenant: {}", projectId, tenantId);

		Project project = projectRepository.findByIdAndTenantId(projectId, tenantId)
        .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

		projectRepository.delete(project);
	}

	private ProjectResponseDTO mapToResponseDTO(Project project) {
		return new ProjectResponseDTO(
				project.getId(),
				project.getName(),
				project.getDescription(),
				project.getStatus(),
				project.getTeamId(),
				project.getCreatedAt(),
				project.getUpdatedAt()
		);
	}
}
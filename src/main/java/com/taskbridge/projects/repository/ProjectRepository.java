package com.taskbridge.projects.repository;

import com.taskbridge.projects.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

	/**
	 * Finds all projects by team ID and tenant ID.
	 *
	 * @param teamId   the team ID
	 * @param tenantId the tenant ID
	 * @return list of projects
	 */
	@Query("SELECT p FROM Project p WHERE p.teamId = :teamId AND p.tenantId = :tenantId")
	List<Project> findByTeamIdAndTenantId(@Param("teamId") Long teamId, @Param("tenantId") String tenantId);
}
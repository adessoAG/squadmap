package de.adesso.squadmap.adapter.persistence;

import de.adesso.squadmap.application.port.driven.project.CreateProjectPort;
import de.adesso.squadmap.common.PersistenceAdapter;
import de.adesso.squadmap.domain.Project;
import de.adesso.squadmap.domain.exceptions.ProjectAlreadyExistsException;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
class CreateProjectAdapter implements CreateProjectPort {

    private final ProjectRepository projectRepository;
    private final PersistenceMapper<Project, ProjectNeo4JEntity> projectPersistenceMapper;

    @Override
    public long createProject(Project project) {
        if (projectRepository.existsByTitle(project.getTitle())) {
            throw new ProjectAlreadyExistsException(project.getTitle());
        }
        return projectRepository.save(projectPersistenceMapper.mapToNeo4JEntity(project)).getProjectId();
    }
}

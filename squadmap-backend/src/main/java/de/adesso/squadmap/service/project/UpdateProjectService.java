package de.adesso.squadmap.service.project;

import de.adesso.squadmap.domain.Project;
import de.adesso.squadmap.exceptions.project.ProjectAlreadyExistsException;
import de.adesso.squadmap.exceptions.project.ProjectNotFoundException;
import de.adesso.squadmap.port.driver.project.update.UpdateProjectCommand;
import de.adesso.squadmap.port.driver.project.update.UpdateProjectUseCase;
import de.adesso.squadmap.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateProjectService implements UpdateProjectUseCase {

    private final ProjectRepository projectRepository;

    public UpdateProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void updateProject(UpdateProjectCommand command, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        if (projectRepository.existsByTitle(command.getTitle()) && !project.getTitle().equals(command.getTitle())) {
            throw new ProjectAlreadyExistsException();
        }
        project.setTitle(command.getTitle());
        project.setDescription(command.getDescription());
        project.setSince(command.getSince());
        project.setUntil(command.getUntil());
        project.setIsExternal(command.isExternal());
        projectRepository.save(project);
    }
}

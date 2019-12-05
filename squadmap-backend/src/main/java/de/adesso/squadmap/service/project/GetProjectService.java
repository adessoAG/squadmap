package de.adesso.squadmap.service.project;

import de.adesso.squadmap.domain.Project;
import de.adesso.squadmap.port.driver.project.get.GetProjectResponse;
import de.adesso.squadmap.port.driver.project.get.GetProjectUseCase;
import de.adesso.squadmap.repository.ProjectRepository;
import de.adesso.squadmap.utility.ProjectMapper;
import org.springframework.stereotype.Service;

@Service
public class GetProjectService implements GetProjectUseCase {

    private ProjectRepository projectRepository;

    public GetProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @Override
    public GetProjectResponse getProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        return ProjectMapper.mapProjectToGetProjectResponse(project);
    }
}

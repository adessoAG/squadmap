package de.adesso.squadmap.application.service.project;

import de.adesso.squadmap.application.port.driven.project.UpdateProjectPort;
import de.adesso.squadmap.application.port.driver.project.update.UpdateProjectCommand;
import de.adesso.squadmap.application.port.driver.project.update.UpdateProjectUseCase;
import de.adesso.squadmap.domain.mapper.ProjectDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
class UpdateProjectService implements UpdateProjectUseCase {

    private final UpdateProjectPort updateProjectPort;
    private final ProjectDomainMapper projectDomainMapper;

    @Override
    @Transactional
    public void updateProject(UpdateProjectCommand command, Long projectId) {
        updateProjectPort.updateProject(projectDomainMapper.mapToDomainEntity(command, projectId));
    }
}

package de.adesso.squadmap.application.service.project;

import de.adesso.squadmap.application.port.driven.project.DeleteProjectPort;
import de.adesso.squadmap.application.port.driver.project.delete.DeleteProjectUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
class DeleteProjectService implements DeleteProjectUseCase {

    private final DeleteProjectPort deleteProjectPort;

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        deleteProjectPort.deleteProject(projectId);
    }
}

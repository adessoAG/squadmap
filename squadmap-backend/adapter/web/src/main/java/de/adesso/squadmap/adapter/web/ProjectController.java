package de.adesso.squadmap.adapter.web;

import de.adesso.squadmap.adapter.web.webentities.project.CreateProjectRequest;
import de.adesso.squadmap.adapter.web.webentities.project.UpdateProjectRequest;
import de.adesso.squadmap.application.port.driver.project.create.CreateProjectUseCase;
import de.adesso.squadmap.application.port.driver.project.delete.DeleteProjectUseCase;
import de.adesso.squadmap.application.port.driver.project.get.GetProjectResponse;
import de.adesso.squadmap.application.port.driver.project.get.GetProjectUseCase;
import de.adesso.squadmap.application.port.driver.project.get.ListProjectUseCase;
import de.adesso.squadmap.application.port.driver.project.update.UpdateProjectUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/project")
@RequiredArgsConstructor
class ProjectController {

    private final GetProjectUseCase getProjectUseCase;
    private final ListProjectUseCase listProjectUseCase;
    private final CreateProjectUseCase createProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final DeleteProjectUseCase deleteProjectUseCase;

    @GetMapping("/all")
    public List<GetProjectResponse> getAllProjects() {
        return listProjectUseCase.listProjects();
    }

    @GetMapping("/{projectId}")
    public GetProjectResponse getProject(@PathVariable long projectId) {
        return getProjectUseCase.getProject(projectId);
    }

    @PostMapping("/create")
    public Long createProject(@RequestBody CreateProjectRequest command) {
        return createProjectUseCase.createProject(command.asCommand());
    }

    @PutMapping("/update/{projectId}")
    public void updateProject(@PathVariable long projectId, @RequestBody UpdateProjectRequest command) {
        updateProjectUseCase.updateProject(command.asCommand(), projectId);
    }

    @DeleteMapping("/delete/{projectId}")
    public void deleteProject(@PathVariable long projectId) {
        deleteProjectUseCase.deleteProject(projectId);
    }
}

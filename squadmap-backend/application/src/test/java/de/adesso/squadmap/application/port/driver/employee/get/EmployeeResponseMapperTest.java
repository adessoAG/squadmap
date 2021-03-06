package de.adesso.squadmap.application.port.driver.employee.get;


import de.adesso.squadmap.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class EmployeeResponseMapperTest {

    private EmployeeResponseMapperImplementation employeeResponseMapper = new EmployeeResponseMapperImplementation();

    @Test
    void checkIfMapToResponseMapsToResponse() {
        //given
        Employee employee = EmployeeMother.complete().build();
        Project project = ProjectMother.complete().build();
        WorkingOn workingOn = WorkingOnMother.complete()
                .employee(employee)
                .project(project)
                .build();
        List<WorkingOn> workingOnList = Collections.singletonList(workingOn);

        //when
        GetEmployeeResponse getEmployeeResponse = employeeResponseMapper.mapToResponseEntity(employee, workingOnList);

        //then
        assertThat(getEmployeeResponse.getEmployeeId()).isEqualTo(employee.getEmployeeId());
        assertThat(getEmployeeResponse.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(getEmployeeResponse.getLastName()).isEqualTo(employee.getLastName());
        assertThat(getEmployeeResponse.getBirthday()).isEqualTo(employee.getBirthday());
        assertThat(getEmployeeResponse.getEmail()).isEqualTo(employee.getEmail());
        assertThat(getEmployeeResponse.getPhone()).isEqualTo(employee.getPhone());
        assertThat(getEmployeeResponse.getIsExternal()).isEqualTo(employee.getIsExternal());
        assertThat(getEmployeeResponse.getImage()).isEqualTo(employee.getImage());

        assertThat(getEmployeeResponse.getProjects().size()).isOne();
        GetWorkingOnResponseWithoutEmployee workingOnResponse = getEmployeeResponse.getProjects().get(0);

        assertThat(workingOnResponse.getWorkingOnId()).isEqualTo(workingOn.getWorkingOnId());
        assertThat(workingOnResponse.getSince()).isEqualTo(workingOn.getSince());
        assertThat(workingOnResponse.getUntil()).isEqualTo(workingOn.getUntil());
        assertThat(workingOnResponse.getWorkload()).isEqualTo(workingOn.getWorkload());

        assertThat(workingOnResponse.getProject()).isNotNull();
        GetProjectResponseWithoutEmployee projectResponse = workingOnResponse.getProject();

        assertThat(projectResponse.getProjectId()).isEqualTo(project.getProjectId());
        assertThat(projectResponse.getTitle()).isEqualTo(project.getTitle());
        assertThat(projectResponse.getDescription()).isEqualTo(project.getDescription());
        assertThat(projectResponse.getSince()).isEqualTo(project.getSince());
        assertThat(projectResponse.getUntil()).isEqualTo(project.getUntil());
        assertThat(projectResponse.getIsExternal()).isEqualTo(project.getIsExternal());
        assertThat(projectResponse.getSites()).isEqualTo(project.getSites());
    }
}

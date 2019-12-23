package de.adesso.squadmap.utilityTest;

import de.adesso.squadmap.domain.Employee;
import de.adesso.squadmap.domain.Project;
import de.adesso.squadmap.domain.WorkingOn;
import de.adesso.squadmap.port.driver.employee.get.GetEmployeeResponse;
import de.adesso.squadmap.port.driver.project.get.GetProjectResponse;
import de.adesso.squadmap.port.driver.workingOn.get.GetWorkingOnResponse;
import de.adesso.squadmap.utility.EmployeeToResponseMapper;
import de.adesso.squadmap.utility.ProjectToResponseMapper;
import de.adesso.squadmap.utility.WorkingOnToResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class WorkingOnToResponseMapperTest {

    @Autowired
    private WorkingOnToResponseMapper mapper;
    @MockBean
    private EmployeeToResponseMapper employeeMapper;
    @MockBean
    private ProjectToResponseMapper projectMapper;

    @Test
    void checkIfMapMapsToValidWorkingOnResponse() {
        //given

        Project project = new Project("t", "d", LocalDate.now(), LocalDate.now(), true);
        Employee employee = new Employee("f", "l", LocalDate.now(), "e", "0", true);
        WorkingOn workingOn = new WorkingOn(employee, project, LocalDate.now(), LocalDate.now());
        workingOn.setWorkingOnId(1L);

        GetEmployeeResponse employeeResponse = new GetEmployeeResponse();
        GetProjectResponse projectResponse = new GetProjectResponse();
        when(employeeMapper.map(employee)).thenReturn(employeeResponse);
        when(projectMapper.map(project)).thenReturn(projectResponse);

        //when
        GetWorkingOnResponse workingOnResponse = mapper.map(workingOn);

        //then
        assertThat(workingOnResponse.getWorkingOnId()).isEqualTo(workingOn.getWorkingOnId());
        assertThat(workingOnResponse.getSince()).isEqualTo(workingOn.getSince());
        assertThat(workingOnResponse.getUntil()).isEqualTo(workingOn.getUntil());

        assertThat(workingOnResponse.getEmployee()).isEqualTo(employeeResponse);
        assertThat(workingOnResponse.getProject()).isEqualTo(projectResponse);

        verify(projectMapper, times(1)).map(project);
        verify(employeeMapper, times(1)).map(employee);

    }
}
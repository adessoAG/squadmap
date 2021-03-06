package de.adesso.squadmap.application.service.workingon;

import de.adesso.squadmap.application.port.driven.workingon.ListWorkingOnPort;
import de.adesso.squadmap.application.port.driver.workingon.get.GetWorkingOnResponse;
import de.adesso.squadmap.application.port.driver.workingon.get.GetWorkingOnResponseMother;
import de.adesso.squadmap.domain.EmployeeMother;
import de.adesso.squadmap.domain.ProjectMother;
import de.adesso.squadmap.domain.WorkingOn;
import de.adesso.squadmap.domain.WorkingOnMother;
import de.adesso.squadmap.domain.mapper.WorkingOnResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ListWorkingOnServiceTest {

    @Mock
    private ListWorkingOnPort listWorkingOnPort;
    @Mock
    private WorkingOnResponseMapper workingOnResponseMapper;
    @InjectMocks
    private ListWorkingOnService listWorkingOnService;

    @Test
    void checkIfListWorkingOnReturnsAllRelations() {
        //given
        WorkingOn workingOn1 = WorkingOnMother.complete()
                .employee(EmployeeMother.complete().employeeId(1L).build())
                .project(ProjectMother.complete().projectId(1L).build())
                .build();
        List<WorkingOn> filteredWorkingOn1 = Collections.singletonList(workingOn1);
        WorkingOn workingOn2 = WorkingOnMother.complete()
                .employee(EmployeeMother.complete().employeeId(2L).build())
                .project(ProjectMother.complete().projectId(2L).build())
                .build();
        List<WorkingOn> filteredWorkingOn2 = Collections.singletonList(workingOn2);
        GetWorkingOnResponse getWorkingOnResponse = GetWorkingOnResponseMother.complete().build();
        when(listWorkingOnPort.listWorkingOn()).thenReturn(Arrays.asList(workingOn1, workingOn2));
        when(workingOnResponseMapper.mapToResponseEntity(workingOn1, filteredWorkingOn1, filteredWorkingOn1))
                .thenReturn(getWorkingOnResponse);
        when(workingOnResponseMapper.mapToResponseEntity(workingOn2, filteredWorkingOn2, filteredWorkingOn2))
                .thenReturn(getWorkingOnResponse);

        //when
        List<GetWorkingOnResponse> responses = listWorkingOnService.listWorkingOn();

        //then
        assertThat(responses).isEqualTo(Arrays.asList(getWorkingOnResponse, getWorkingOnResponse));
        verify(listWorkingOnPort, times(1)).listWorkingOn();
        verify(workingOnResponseMapper, times(1))
                .mapToResponseEntity(workingOn1, filteredWorkingOn1, filteredWorkingOn1);
        verify(workingOnResponseMapper, times(1))
                .mapToResponseEntity(workingOn2, filteredWorkingOn2, filteredWorkingOn2);
        verifyNoMoreInteractions(listWorkingOnPort, workingOnResponseMapper);
    }
}

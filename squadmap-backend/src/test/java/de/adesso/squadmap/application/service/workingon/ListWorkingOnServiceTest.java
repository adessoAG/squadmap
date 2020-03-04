package de.adesso.squadmap.application.service.workingon;

import de.adesso.squadmap.application.domain.ResponseMapper;
import de.adesso.squadmap.application.domain.WorkingOn;
import de.adesso.squadmap.application.domain.WorkingOnMother;
import de.adesso.squadmap.application.port.driven.workingon.ListWorkingOnPort;
import de.adesso.squadmap.application.port.driver.workingon.get.GetWorkingOnResponse;
import de.adesso.squadmap.application.port.driver.workingon.get.GetWorkingOnResponseMother;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ListWorkingOnService.class)
@ActiveProfiles("test")
class ListWorkingOnServiceTest {

    @MockBean
    private ListWorkingOnPort listWorkingOnPort;
    @MockBean
    private ResponseMapper<WorkingOn, GetWorkingOnResponse> responseMapper;
    @Autowired
    private ListWorkingOnService service;

    @Test
    void checkIfListWorkingOnReturnsAllRelations() {
        //given
        WorkingOn workingOn1 = WorkingOnMother.complete()
                .workingOnId(1L)
                .build();
        WorkingOn workingOn2 = WorkingOnMother.complete()
                .workingOnId(2L)
                .build();
        List<WorkingOn> allRelations = Arrays.asList(workingOn1, workingOn2);
        GetWorkingOnResponse getWorkingOnResponse = GetWorkingOnResponseMother.complete().build();
        when(listWorkingOnPort.listWorkingOn()).thenReturn(allRelations);
        when(responseMapper.toResponse(workingOn1, allRelations)).thenReturn(getWorkingOnResponse);
        when(responseMapper.toResponse(workingOn2, allRelations)).thenReturn(getWorkingOnResponse);

        //when
        List<GetWorkingOnResponse> responses = service.listWorkingOn();

        //then
        responses.forEach(response -> assertThat(response).isEqualTo(getWorkingOnResponse));
        verify(listWorkingOnPort, times(2)).listWorkingOn();
        verify(responseMapper, times(1)).toResponse(workingOn1, allRelations);
        verify(responseMapper, times(1)).toResponse(workingOn2, allRelations);
        verifyNoMoreInteractions(listWorkingOnPort);
        verifyNoMoreInteractions(responseMapper);
    }
}

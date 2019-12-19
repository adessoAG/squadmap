package de.adesso.squadmap.serviceTest.workingOn;

import de.adesso.squadmap.domain.WorkingOn;
import de.adesso.squadmap.exceptions.workingOn.WorkingOnNotFoundException;
import de.adesso.squadmap.port.driver.workingOn.get.GetWorkingOnResponse;
import de.adesso.squadmap.repository.WorkingOnRepository;
import de.adesso.squadmap.service.workingOn.GetWorkingOnService;
import de.adesso.squadmap.utility.WorkingOnToResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class GetWorkingOnServiceTest {

    @Autowired
    private GetWorkingOnService service;
    @MockBean
    private WorkingOnRepository workingOnRepository;
    @MockBean
    private WorkingOnToResponseMapper workingOnMapper;

    @Test
    void checkIfGetWorkingOnReturnsTheRelation() {
        //given
        long workingOnId = 1;
        WorkingOn workingOn = new WorkingOn();
        GetWorkingOnResponse response = new GetWorkingOnResponse();
        when(workingOnRepository.existsById(workingOnId)).thenReturn(true);
        when(workingOnRepository.findById(workingOnId)).thenReturn(Optional.of(workingOn));
        when(workingOnMapper.map(workingOn)).thenReturn(response);

        //when
        GetWorkingOnResponse found = service.getWorkingOn(workingOnId);

        //then
        assertThat(found).isEqualTo(response);
        verify(workingOnRepository, times(1)).existsById(workingOnId);
        verify(workingOnRepository, times(1)).findById(workingOnId);
        verify(workingOnMapper, times(1)).map(workingOn);
        verifyNoMoreInteractions(workingOnRepository);
        verifyNoMoreInteractions(workingOnMapper);
    }

    @Test
    void checkIfGetWorkingOnThrowsExceptionWhenNotFound() {
        //given
        long workingOnId = 1;
        when(workingOnRepository.existsById(workingOnId)).thenReturn(false);

        //then
        assertThrows(WorkingOnNotFoundException.class, () ->
                service.getWorkingOn(workingOnId));
    }
}

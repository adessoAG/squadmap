package de.adesso.squadmap.application.service.workingon;

import de.adesso.squadmap.application.port.driven.workingon.DeleteWorkingOnPort;
import de.adesso.squadmap.application.port.driver.workingon.delete.DeleteWorkingOnUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteWorkingOnService implements DeleteWorkingOnUseCase {

    private final DeleteWorkingOnPort deleteWorkingOnPort;

    @Override
    public void deleteWorkingOn(Long workingOnId) {
        deleteWorkingOnPort.deleteWorkingOn(workingOnId);
    }
}

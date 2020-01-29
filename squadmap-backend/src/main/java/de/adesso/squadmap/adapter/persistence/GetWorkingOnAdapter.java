package de.adesso.squadmap.adapter.persistence;

import de.adesso.squadmap.adapter.persistence.exceptions.WorkingOnNotFoundException;
import de.adesso.squadmap.application.domain.WorkingOn;
import de.adesso.squadmap.application.port.driven.workingon.GetWorkingOnPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class GetWorkingOnAdapter implements GetWorkingOnPort {

    private final WorkingOnRepository workingOnRepository;
    private final WorkingOnPersistenceMapper mapper;

    @Override
    public WorkingOn getWorkingOn(Long workingOnId) {
        return mapper.mapToDomainEntity(workingOnRepository.findById(workingOnId, 0)
                .orElseThrow(WorkingOnNotFoundException::new));
    }
}
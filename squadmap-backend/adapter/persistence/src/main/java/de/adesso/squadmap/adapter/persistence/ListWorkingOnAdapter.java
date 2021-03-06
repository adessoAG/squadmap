package de.adesso.squadmap.adapter.persistence;

import de.adesso.squadmap.application.port.driven.workingon.ListWorkingOnPort;
import de.adesso.squadmap.common.PersistenceAdapter;
import de.adesso.squadmap.domain.WorkingOn;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@PersistenceAdapter
@RequiredArgsConstructor
class ListWorkingOnAdapter implements ListWorkingOnPort {

    private final WorkingOnRepository workingOnRepository;
    private final PersistenceMapper<WorkingOn, WorkingOnNeo4JEntity> workingOnPersistenceMapper;

    @Override
    public List<WorkingOn> listWorkingOn() {
        return StreamSupport.stream(workingOnRepository.findAll().spliterator(), false)
                .map(workingOnPersistenceMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkingOn> listWorkingOnByEmployeeId(long employeeId) {
        return StreamSupport.stream(workingOnRepository.findAllByEmployeeId(employeeId).spliterator(), false)
                .map(workingOnPersistenceMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkingOn> listWorkingOnByProjectId(long projectId) {
        return StreamSupport.stream(workingOnRepository.findAllByProjectId(projectId).spliterator(), false)
                .map(workingOnPersistenceMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
}

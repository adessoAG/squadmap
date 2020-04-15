package de.adesso.squadmap.application.domain.mapper;

import de.adesso.squadmap.application.domain.WorkingOn;

import java.util.List;

public interface ResponseMapper<I, O> {

    O mapToResponseEntity(I i, List<WorkingOn> workingOns);
}
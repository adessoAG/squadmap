package de.adesso.squadmap.application.port.driven.workingon;

import de.adesso.squadmap.domain.WorkingOn;

public interface CreateWorkingOnPort {

    long createWorkingOn(WorkingOn workingOn);
}

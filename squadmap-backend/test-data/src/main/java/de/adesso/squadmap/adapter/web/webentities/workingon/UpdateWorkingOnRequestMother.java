package de.adesso.squadmap.adapter.web.webentities.workingon;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateWorkingOnRequestMother {

    public static UpdateWorkingOnRequest.UpdateWorkingOnRequestBuilder complete() {
        return UpdateWorkingOnRequest.builder()
                .employeeId(1)
                .projectId(2)
                .since(LocalDate.now())
                .until(LocalDate.now())
                .workload(0);
    }

    public static UpdateWorkingOnRequest.UpdateWorkingOnRequestBuilder invalid() {
        return UpdateWorkingOnRequest.builder()
                .employeeId(0)
                .projectId(0)
                .since(null)
                .until(null)
                .workload(null);
    }
}

package de.adesso.squadmap.application.port.driver.workingon;

import de.adesso.squadmap.common.SelfValidating;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class WorkingOnCommand extends SelfValidating<WorkingOnCommand> {

    private final long employeeId;
    private final long projectId;
    @NotNull(message = "should not be null")
    private final LocalDate since;
    @NotNull(message = "should not be null")
    private final LocalDate until;
    @NotNull(message = "should not be null")
    @Range(min = 0, max = 100, message = "should be a number between {min} and {max}")
    private final Integer workload;


    public WorkingOnCommand(long employeeId,
                            long projectId,
                            LocalDate since,
                            LocalDate until,
                            Integer workload) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.since = since;
        this.until = until;
        this.workload = workload;
        this.validateSelf();
    }
}

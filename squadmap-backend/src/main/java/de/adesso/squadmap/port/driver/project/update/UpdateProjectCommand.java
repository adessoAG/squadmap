package de.adesso.squadmap.port.driver.project.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectCommand {

    private String title;
    private String description;
    private LocalDate since;
    private LocalDate until;
    private Boolean isExternal;
}
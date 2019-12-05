package de.adesso.squadmap.port.driver.employee.get;

import de.adesso.squadmap.port.driver.project.get.GetProjectResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeResponse {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String email;
    private String phone;
    private Boolean isExternal;
    private List<GetProjectResponse> projects;
}

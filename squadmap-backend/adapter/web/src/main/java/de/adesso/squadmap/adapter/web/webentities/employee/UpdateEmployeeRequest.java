package de.adesso.squadmap.adapter.web.webentities.employee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import de.adesso.squadmap.application.port.driver.employee.update.UpdateEmployeeCommand;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonDeserialize(builder = UpdateEmployeeRequest.UpdateEmployeeRequestBuilder.class)
public class UpdateEmployeeRequest {

    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final String email;
    private final String phone;
    private final Boolean isExternal;
    private final String image;

    public UpdateEmployeeCommand asCommand() {
        return UpdateEmployeeCommand.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .email(email)
                .phone(phone)
                .isExternal(isExternal)
                .image(image)
                .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class UpdateEmployeeRequestBuilder{}
}

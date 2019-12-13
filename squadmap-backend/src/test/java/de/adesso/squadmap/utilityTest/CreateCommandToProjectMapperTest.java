package de.adesso.squadmap.utilityTest;

import de.adesso.squadmap.domain.Project;
import de.adesso.squadmap.port.driver.project.create.CreateProjectCommand;
import de.adesso.squadmap.utility.CreateCommandToProjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CreateCommandToProjectMapperTest {

    @Autowired
    private CreateCommandToProjectMapper mapper;

    @Test
    void checkIfMapMapsToValidProject() {
        //given
        CreateProjectCommand command = new CreateProjectCommand("t", "d", LocalDate.now(), LocalDate.now(), true);

        //when
        Project project = mapper.map(command);

        //then
        assertThat(project.getTitle()).isEqualTo(command.getTitle());
        assertThat(project.getDescription()).isEqualTo(command.getDescription());
        assertThat(project.getSince()).isEqualTo(command.getSince());
        assertThat(project.getUntil()).isEqualTo(command.getUntil());
        assertThat(project.getIsExternal()).isEqualTo(command.getIsExternal());
        assertThat(project.getEmployees()).isEmpty();
    }
}

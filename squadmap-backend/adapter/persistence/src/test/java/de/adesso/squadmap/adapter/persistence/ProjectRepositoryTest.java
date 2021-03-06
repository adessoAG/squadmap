package de.adesso.squadmap.adapter.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void checkIfExistsByTitleReturnsTrueWhenExistent() {
        //given
        ProjectNeo4JEntity project = ProjectNeo4JEntityMother.complete().projectId(null).build();
        long id = projectRepository.save(project).getProjectId();
        ProjectNeo4JEntity found = projectRepository.findById(id).orElse(null);
        assertThat(found).isNotNull();

        //when
        boolean answer = projectRepository.existsByTitle(project.getTitle());

        //then
        assertThat(answer).isTrue();
    }

    @Test
    void checkIfExistsByTitleReturnsFalseWhenNotExistent() {
        //given

        //when
        boolean answer = projectRepository.existsByTitle("t");

        //then
        assertThat(answer).isFalse();
    }
}

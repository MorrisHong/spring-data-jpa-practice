package study.datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Rollback(false)
@Transactional
class TeamJpaRepositoryTest {

    @Autowired
    private TeamJpaRepository teamJpaRepository;

    @BeforeEach
    void setUp() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);
    }

    @Test
    public void basicCRUD() throws Exception {
        final long count = teamJpaRepository.count();
        assertThat(count).isEqualTo(2);
    }
}
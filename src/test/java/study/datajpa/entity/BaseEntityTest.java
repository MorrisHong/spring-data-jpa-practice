package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.TeamRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BaseEntityTest {

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void test() throws Exception {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);
        //when

        //then
     }
}
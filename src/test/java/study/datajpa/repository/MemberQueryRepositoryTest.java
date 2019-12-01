package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberQueryRepositoryTest {

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Test
    public void testQUery() throws Exception {
        //given
        final List<Member> allMembers = memberQueryRepository.findAllMembers();
        for (Member allMember : allMembers) {
            System.out.println("allMember = " + allMember);
        }
        //when

        //then
     }

}
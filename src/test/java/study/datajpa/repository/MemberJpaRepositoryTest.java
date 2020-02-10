package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @Transactional
    void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        memberJpaRepository.save(member);

        //when
        Member findMember = memberJpaRepository.find(member.getId());

        //then
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
     }

     @Test
     @Transactional
     void basic_crud() throws Exception {
         //given
         Member member1 = new Member("member1");
         Member member2 = new Member("member2");
         memberJpaRepository.save(member1);
         memberJpaRepository.save(member2);


         //when
         Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
         Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

         List<Member> findMembers = memberJpaRepository.findAll();
         //then
         assertEquals(findMember1.getId(), member1.getId());
         assertEquals(findMember2.getId(), member2.getId());

         assertEquals(2, findMembers.size());
         assertEquals(2, memberJpaRepository.count());

         memberJpaRepository.delete(member1);
         memberJpaRepository.delete(member2);

         assertEquals(0, memberJpaRepository.count());
      }

      @Test
      @Transactional
      void findByUsernameAndAgeGreaterThen() throws Exception {
          Member member1 = new Member("AAA", 10);
          Member member2 = new Member("AAA", 20);
          memberJpaRepository.save(member1);
          memberJpaRepository.save(member2);

          List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 11);
          assertEquals(1, members.size());

      }

}
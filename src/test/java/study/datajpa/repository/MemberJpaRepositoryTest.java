package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        final Member savedMember = memberJpaRepository.save(member);

        final Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        final List<Member> findMembers = memberJpaRepository.findByUsernameAndAgeGreaterThen("BBB", 15);
        assertThat(findMembers.size()).isEqualTo(1);

    }

    @Test
    void basicCRUD() {
        final Member member1 = new Member("member1");
        final Member member2 = new Member("member2");

        //create
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //read
        final Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        final Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1.getUsername()).isEqualTo(member1.getUsername());
        assertThat(findMember2.getUsername()).isEqualTo(member2.getUsername());

        assertThat(memberJpaRepository.count()).isEqualTo(2);

        //update (dirty check)
        final String updateFindMember1Username = "updateFindMember1Username";
        final String updateFindMember2Username = "updateFindMember2Username";
        findMember1.setUsername(updateFindMember1Username);
        findMember2.setUsername(updateFindMember2Username);
        assertThat(findMember1.getUsername()).isEqualTo(updateFindMember1Username);
        assertThat(findMember2.getUsername()).isEqualTo(updateFindMember2Username);

        //delete
        memberJpaRepository.delete(findMember1);
        assertThat(memberJpaRepository.count()).isEqualTo(1);
    }

    @Test
    void paging() {
        //given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);

    }

}
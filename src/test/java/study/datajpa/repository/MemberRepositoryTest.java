package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testMember() {
        Member member = new Member("memberB");
        final Member savedMember = memberRepository.save(member);

        final Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCRUD() {
        final Member member1 = new Member("member1");
        final Member member2 = new Member("member2");

        //create
        memberRepository.save(member1);
        memberRepository.save(member2);

        //read
        final Member findMember1 = memberRepository.findById(member1.getId()).get();
        final Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1.getUsername()).isEqualTo(member1.getUsername());
        assertThat(findMember2.getUsername()).isEqualTo(member2.getUsername());

        assertThat(memberRepository.count()).isEqualTo(2);

        //update (dirty check)
        final String updateFindMember1Username = "updateFindMember1Username";
        final String updateFindMember2Username = "updateFindMember2Username";
        findMember1.setUsername(updateFindMember1Username);
        findMember2.setUsername(updateFindMember2Username);
        assertThat(findMember1.getUsername()).isEqualTo(updateFindMember1Username);
        assertThat(findMember2.getUsername()).isEqualTo(updateFindMember2Username);

        //delete
        memberRepository.delete(findMember1);
        assertThat(memberRepository.count()).isEqualTo(1);
    }

}
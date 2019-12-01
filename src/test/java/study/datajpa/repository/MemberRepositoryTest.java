package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

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

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        final List<Member> findMembers = memberRepository.findByUsernameAndAgeGreaterThan("BBB", 15);
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    void testQuery() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.flush();

        List<Member> aaa = memberRepository.findUser("AAA", 10);
        assertThat(aaa.get(0)).isEqualTo(member1);
    }

    @Test
    void findUsernameList() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.flush();

        final List<String> username = memberRepository.findUsername();
        for (String s : username) {
            System.out.println("s = " + s);
        }

    }

    @Test
    void findMemberDto() {
        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        member1.setTeam(team);
        memberRepository.save(member1);

        final List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        Member member3 = new Member("CCC", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        final List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : byNames) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        final PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        final Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        final List<Member> content = page.getContent();
        final long totalElements = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void slice() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        final PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        final Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        //then
        final List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member1", 19));
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member1", 21));
        memberRepository.save(new Member("member1", 40));

        //when
        int i = memberRepository.bulkAgePlus(20);


        //then
        assertThat(i).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB

        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        final List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam().getName());
        }

        //then
    }

    @Test
    public void findMemberFetchJoin() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB

        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        final List<Member> members = memberRepository.findMemberFetchJoin();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam().getName());
        }

        //then
    }

    @Test
    public void queryHint() throws Exception {
        //given
        final Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        final Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        //then
    }

    @Test
    public void lock() throws Exception {
        //given
        final Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        final List<Member> findMembers = memberRepository.findLockByUsername("member1");
        Member findMember = findMembers.get(0);
        findMember.setUsername("member2");

        //then
    }

    @Test
    public void callCustom() throws Exception {
        //given
        final List<Member> result = memberRepository.findMemberCustom();
        //when

        //then
     }


}
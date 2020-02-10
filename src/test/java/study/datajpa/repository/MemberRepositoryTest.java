package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    @Transactional
    void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);
     }

    @Test
    @Transactional
    void basic_crud() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);


        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        List<Member> findMembers = memberRepository.findAll();
        //then
        assertEquals(findMember1.getId(), member1.getId());
        assertEquals(findMember2.getId(), member2.getId());

        assertEquals(2, findMembers.size());
        assertEquals(2, memberRepository.count());

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        assertEquals(0, memberRepository.count());
    }

    @Test
    @Transactional
    void findByUsernameAndAgeGreaterThen() throws Exception {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 11);
        assertEquals(1, members.size());
    }

    @Test
    void testQuery() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertEquals(result.get(0).getUsername(), member1.getUsername());
     }

    @Test
    void test_findUsernameList() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertEquals(usernameList.get(0), member1.getUsername());
        assertEquals(usernameList.get(1), member2.getUsername());
    }

    @Test
    void test_findMemberDto() throws Exception {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        member1.setTeam(teamA);
        member2.setTeam(teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        assertEquals(teamA.getName(), memberDtos.get(0).getTeamName());
        assertEquals(teamB.getName(), memberDtos.get(1).getTeamName());
     }

    @Test
    void test_findByNames() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> usernameList = memberRepository.findByNames(List.of(member1.getUsername(), member2.getUsername()));
        assertEquals(2, usernameList.size());
        assertEquals(member1.getUsername(), usernameList.get(0).getUsername());
        assertEquals(member2.getUsername(), usernameList.get(1).getUsername());
    }

    @Test
    void test_variety_returnType() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findListByUsername = memberRepository.findListByUsername("AAA");
        Optional<Member> findOptionalMemberByUsername = memberRepository.findOptionalMemberByUsername("AAA");
        Member findMemberByUsername = memberRepository.findMemberByUsername("AAA");
    }

    @Transactional
    @Test
    void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "username"));

        Page<Member> members = memberRepository.findByAge(age, pageRequest); //member1, member2, member3
        Page<MemberDto> map = members.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        for (MemberDto memberDto : map) {
            System.out.println("memberDto = " + memberDto);
        }

        List<Member> content = members.getContent();
        long totalElements = members.getTotalElements();

        assertEquals(6, totalElements);
        assertEquals("member1", content.get(0).getUsername());
    }

    @Transactional
    @Test
    void slice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "username"));

        Slice<Member> members = memberRepository.findSliceByAge(age, pageRequest); //member1, member2, member3

        List<Member> content = members.getContent();

        assertEquals("member1", content.get(0).getUsername());
    }
}
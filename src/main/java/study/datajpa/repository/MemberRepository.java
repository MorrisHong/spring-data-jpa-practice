package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member as m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username,
                          @Param("age") int age);

    @Query("select m.username from Member as m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member as m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member as m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> namenamename);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username);     // 단건
    Optional<Member> findOptionalMemberByUsername(String username); // 옵셔널

    @Query(value = "select m from Member m left join fetch m.team as t",
            countQuery = "select count(m.username) from Member as m")
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, PageRequest pageRequest);

    @Modifying(clearAutomatically = true) //excuteUpdate
    @Query("update Member as m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member as m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member as m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);
}

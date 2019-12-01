package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teanName;

    public MemberDto(Long id, String username, String teanName) {
        this.id = id;
        this.username = username;
        this.teanName = teanName;
    }
}

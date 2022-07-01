package oauth2web.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member_entity")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Builder
public class MemberEntity {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto key
    private int mno;            // 회원번호
    private String mid;         // 회원아이디
    private String mpassword;   // 회원비밀번호
    private Role role;          // 회원권한
}
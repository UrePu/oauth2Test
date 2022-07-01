package oauth2web.Dto;

import lombok.*;
import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class MemberDto {
    private int mno;
    private String mid;
    private String mpwd;

    // dto -> entity 변환메소드(이유 : dto는 db로 들어갈수 없다)
    public MemberEntity toentity(){
        // 패스워드 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 빌더패턴
        return MemberEntity.builder()
                .mid(this.mid)
                .mpwd(encoder.encode(this.mpwd) )
                .role(Role.MEMBER)
                .build();
    }
}

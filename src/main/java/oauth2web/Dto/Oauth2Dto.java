package oauth2web.Dto;

import lombok.*;
import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.Role;

import java.util.Map;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Builder
public class Oauth2Dto { // Oauth2Dto 생성이유 : DB 생성하기 위해

    private String mid;
    private String registrationId; //  클라이언트id
    private Map<String, Object> attributes; // 인증정보(json)
    private String nameAttributeKey; // JSON 키

    // 1. 클라언트 구분 메소드 [ 네이버 or 카카오 ]
    public static Oauth2Dto sns확인(String registrationId, Map<String, Object> attributes, String nameAttributeKey ){
        if(registrationId.equals("naver")){ // 클라이언트id가 네이버일 경우
            return 네이버변환(registrationId, attributes, nameAttributeKey);
        }else if(registrationId.equals("kakao")){ // 클라이언트id가 카카오일 경우
            return 카카오변환(registrationId, attributes, nameAttributeKey);
        }else{
            return null; // 만약에 없으면
        } // else end
    }

    // registrationId 가 네이버 이면
    public static Oauth2Dto 네이버변환(String registrationId, Map<String, Object> attributes, String nameAttributeKey ){
        // 네이버 인증정보 불러오기(nameAttributeKey = response)
        Map<String, Object> response = (Map<String, Object>) attributes.get(nameAttributeKey);
        // 빌더패턴
        return Oauth2Dto.builder()
                .mid((String)response.get("email"))
                .build();
    }
    // registrationId 가 카카오 이면
    public static Oauth2Dto 카카오변환(String registrationId, Map<String, Object> attributes, String nameAttributeKey ){
        // 카카오 인증정보 불러오기(nameAttributeKey = kakao_account)
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get(nameAttributeKey);
        // 빌더패턴
        return Oauth2Dto.builder()
                .mid((String)kakao_account.get("email"))
                .build();
    }

    // oauth 정보 : dto -> entity 변경
    public MemberEntity toentity(){
        return MemberEntity.builder()
                .mid(this.mid)
                .role(Role.MEMBER)
                .build();
    }


}

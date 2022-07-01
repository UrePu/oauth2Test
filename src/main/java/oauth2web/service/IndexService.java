package oauth2web.service;

import oauth2web.Dto.LoginDto;
import oauth2web.Dto.MemberDto;
import oauth2web.Dto.Oauth2Dto;
import oauth2web.Entity.MemberEntity;
import oauth2web.Entity.MemberRepository;
import oauth2web.Entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // UserDetailsService : 커스텀 로그인
    @Autowired // 자동(빈) 메모리 생성
    private MemberRepository memberRepository;

    // 1. 회원가입 메소드
    public void signup(MemberDto memberDto){
        // memberRepository에 entity 저장
        memberRepository.save(memberDto.toentity() );
    } // 회원가입 처리 end

    // 2. 일반회원 로그인(아이디만 검증처리 -> 패스워드 검증X)
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        // UserDetails : 인증되면 세션 부여

        // 1. memberRepository 입력된 mid가 있는지 찾아서 가져오기
        MemberEntity memberEntity = memberRepository.findBymid(mid).get();
        // 2. 찾은 회원엔티티의 권한[키]을 리스트에 담기
        List<GrantedAuthority> autorities = new ArrayList<>();
        autorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getKey() ));
        // 리스트에 인증된 엔티티의 키를 보관

        // 3. memberEntity에 해당 mid, mpassword, autorities(리스트)를 인증세션 부여
        LoginDto loginDto = new LoginDto(memberEntity.getMid(),memberEntity.getMpassword(), autorities);

        // 4. 반환
        return loginDto;
    } // 일반회원(Security 사용) 로그인 end

    // 3. OAuth 회원 로그인
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 인증[로그인] 결과 정보 요청
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 아이디[네이버 vs 카카오 vs 구글] : oauth 구분용으로 사용할 변수
        String registrationid = userRequest.getClientRegistration().getRegistrationId();

        // 인증정보 -> 결과(반환타입 JSON)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 회원정보 요청시 사용되는 JSON 키 이름 호출 : 회원정보 호출시 사용되는 키 이름
        String nameAttribyteKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 테스트
        System.out.println("클라이언트 등록 이름 : "+registrationid);
        System.out.println("회원 정보(json) 호출시 사용되는 키 : " +nameAttribyteKey);
        System.out.println("인증 정보(로그인) 결과 내용" +oAuth2User.getAttributes() );

        // oauth2 정보 -> dto
        Oauth2Dto oauth2Dto = Oauth2Dto.sns확인(registrationid, attributes, nameAttribyteKey);

        // DB에 해당ID 중복 확인
        Optional<MemberEntity> optional =  memberRepository.findBymid(oauth2Dto.getMid() );
        if(optional.isPresent()){ // id가 없으면
            // entity -> db 저장
            memberRepository.save(oauth2Dto.toentity() );
        }else{
            // 로그인 한적이 있으면 DB 업데이트 처리
        } // else end

        // 반환타입 DefaultOAuth2User(권한, 회원인증정보, 회원정보 호출키)
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("SNS사용자")),
                attributes,
                nameAttribyteKey);
    } // OAuth 회원 로그인처리 end

    // 인증된 아이디 호출 메소드
    public String 현재인증된회원아이디호출(){
        // 세션에 인증된 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 세션에 인증된 정보(principal+authorities) 중 principal 불러오기
        Object principal = authentication.getPrincipal();

        String mid = null;
        if(principal != null){ // 로그인이 되어 있으면(principal = 인증정보가 null이 아니면)
            // instanceof = 객체 타입을 확인하는 연산자(true/false 반환)
            // 사용방법 : 객체 instanceof 클래스
            if(principal instanceof UserDetails){ // 일반회원 이면
                mid = ((UserDetails)principal).getUsername();
            }else if(principal instanceof OAuth2User){ // OAuth2회원 이면
                Map<String, Object> attributes = ((OAuth2User)principal).getAttributes();
                if(attributes.get("response") != null){ // 네이버일 경우(attributes = response)
                    Map<String, Object> map = (Map<String, Object>) attributes.get("response");
                    mid = map.get("email").toString();
                }else if(attributes.get("kakao_account") != null){ // 카카오일 경우(attributes = kakao_account)
                    Map<String, Object> map = (Map<String, Object>) attributes.get("kakao_account");
                    mid = map.get("email").toString();
                } // else if end
            } // else if end
        }else{ // 로그인이 안되어 있으면(null 이면)
            return null;
        } // else end
        return mid;
    } // 현재인증된회원아이디호출 end


} // class end
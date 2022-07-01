package oauth2web.Dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LoginDto implements UserDetails { // 로그인 세션에 넣을 Dto 생성
    // UserDetails 클래스에 -> authorities 필수로 필드 선언해야함

    private String mid; // 회원아이디
    private String mpwd; // 회원아이디
    private Set<GrantedAuthority> autorities; // 부여된 인증들

    // ?????
    public LoginDto(String mid, String mpassword, Collection<? extends GrantedAuthority> authorities) {
        this.mid = mid;
        this.mpwd = mpassword;
        this.autorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
    }
    // 인증검색
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.autorities;
    }
    // 패스워드 반환
    @Override
    public String getPassword() {
        return this.mpwd;
    }
    // 아이디 반환
    @Override
    public String getUsername() {
        return this.mid;
    }
    // 계정 인증 만료 여부 확인[true : 만료되지 않음]
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 계정 잠겨 있는지 확인[true : 잠겨있지 않음]
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 계정 패스워드가 만료 여부 확인[true : 만료되지 않음]
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 계정 사용 가능한 여부 확인[true : 사용가능]
    @Override
    public boolean isEnabled() {
        return true;
    }

}
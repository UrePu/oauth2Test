package oauth2web.config;

import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 해당 클래스 설정
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 웹 시큐리티 설정 관련 상속클래스

    @Override // 재정의(WebSecurityConfigurerAdapter)
    protected void configure(HttpSecurity http) throws Exception {
        // Http = 링크 관련 시큐리티 보안
        http.authorizeHttpRequests().antMatchers("/").permitAll()
                // http.authorizeHttpRequests() : http에서 인증된 요청 / antMatchers("url") : 인증할 URL / permitAll() : 인증이 없어도 요청 가능 = 모든 접근 허용
                .and()
                .formLogin() // 로그인페이지 보안설정
                .loginPage("/member/login") // 아이디/비밀번호를 입력받을 페이지 URL(login html)
                .loginProcessingUrl("/member/logincontroller")// 로그인을 처리할 URL -> loadUserByUsername
                .failureUrl("/member/login/error")
                .usernameParameter("mid") // 로그인시 아이디로 입력받을 변수명[기본값 : user -> mid]
                .passwordParameter("mpassword") // 로그인시 비밀번호로 입력받을 변수명[기본값 : password ->mpassword]
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))  // 로그아웃 처리할 URL 정의
                .logoutSuccessUrl("/")  // 로그아웃 성공시 이동할 URL
                .invalidateHttpSession(true) // 세션 초기화
                .and()
                .csrf() // csrf : 사이트 간 요청 위조[해킹 공격 방법중 하나] = 서버에게 요청할 수 있는 페이지 제한
                .ignoringAntMatchers("/member/logincontroller")
                .ignoringAntMatchers("/member/signupcontroller")
                .and()
                .oauth2Login() // oauth2 관련 설정
                .userInfoEndpoint()// 유저 정보가 들어오는 위치
                .userService(indexService); // 해당 서비스 클래스로 유저 정보를 담는다
    }

    @Autowired // 자동(빈) 메모리 생성
    private IndexService indexService; // 인덱스 서비스 호출

    @Override // 인증(로그인)관리 메소드 재정의
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(indexService).passwordEncoder(new BCryptPasswordEncoder() );
        // 인증할 서비스객체 -> 패스워드 인코딩(BCrypt 객체로)
    }
}
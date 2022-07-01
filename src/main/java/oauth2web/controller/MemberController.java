package oauth2web.controller;
import oauth2web.Dto.MemberDto;
import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @Autowired
    private IndexService indexService;

    // 메인페이지 이동 매핑
    @GetMapping("/")
    public String main(){
        return "main";
    }
    // 로그인페이지 이동 매핑
    @GetMapping("/member/login")
    public String login(){
        return "login";
    }
    // 회원가입페이지 이동 매핑
    @GetMapping("/member/signup")
    public String signup(){return "signup";}

    // 회원가입 처리
    @PostMapping("/member/signupcontroller")
    public String signupcontroller(MemberDto memberDto){
        indexService.signup(memberDto);
        return "redirect:/"; // 컨트롤은 템플릿영역이기때문에
    }



}
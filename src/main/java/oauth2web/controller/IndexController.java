package oauth2web.controller;

import oauth2web.Dto.MemberDto;
import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @Responsebody 안써줘도됨
public class IndexController {

    @Autowired
    private IndexService indexService;

    // 인증된회원아이디호출 처리
    @GetMapping("/member/info")
    public String memberinfo(){return indexService.현재인증된회원아이디호출();}
}
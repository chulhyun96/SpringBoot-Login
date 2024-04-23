package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository repository;

    /*@GetMapping("/")*/
    public String home() {
        return "home";
    }
    
    @GetMapping("/")
    // 로그인 안한 사용자도 들어와야 하기 때문에 required=false
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        //로그인을 안한 상태 일 경우

        if(memberId == null) {
            return "home";
        }

        //로그인
        Member loginMember = repository.findById(memberId);
        //DB에 없을 수도 있음. 쿠키가 너무 옛날에 만들어졌거나해서
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response,"memberId");
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
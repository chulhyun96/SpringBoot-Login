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
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    
    /*@GetMapping("/")*/
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

    /*@GetMapping("/")*/
    public String homeLoginV2(HttpServletRequest request, Model model) {
        // 아직 로그인을 안한 사용자는 세션이 부여되면 안되기 때문에 기본값을 false로 처리한다.
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        // 세션에 회원 데이터가 없으면 home
        if (member == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }
    @GetMapping("/")
    public String homeLoginV3(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {
        // 세션 만료 및 회원 데이터가 없다면 home
        if (member == null) {
            return "home";
        }
        //세션이 유지되면 로그인 성공
        model.addAttribute("member", member);
        return "loginHome";
    }
}
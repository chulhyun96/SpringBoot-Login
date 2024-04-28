package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login/loginForm";
    }

    /*@PostMapping("/login")*/
    public String loginV1(@Validated LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            log.info("LogIn form error: {}", bindingResult);
        }
        Member member = loginService.login(form.getLoginId(), form.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "Invalid loginId or password");
            return "login/loginForm";
        }
        // 로그인 성공 처리
        // 쿠키에 시간 정보를 주지 않으면 세션 쿠키 이다(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(member.getId()));
        response.addCookie(idCookie);
        log.info("Login Success");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV2(@Validated LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {
        //에러가 있을 경우 필드 에러
        if (bindingResult.hasErrors()) {
            log.info("LogIn form error: {}", bindingResult);
            return "login/loginForm";
        }
        // 글로벌 에러
        Member member = loginService.login(form.getLoginId(), form.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "Invalid loginId or password");
            return "login/loginForm";
        }
        // 성공 로직
        HttpSession session = request.getSession(); // Default = true
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        log.info("Login Success");
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}

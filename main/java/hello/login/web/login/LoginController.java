package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated LoginForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("LogIn form error: {}", bindingResult);
        }
        Member member = loginService.login(form.getLoginId(), form.getPassword());
        if (member == null) {
            bindingResult.reject("loginFail", "Invalid loginId or password");
            return "login/loginForm";
        }
        return "redirect:/";
    }
}

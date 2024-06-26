package hello.login.web.member;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository repository;

    @GetMapping("/add")
    public String addForm(Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Validated Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("LogIn Errors : {}", bindingResult);
            return "members/addMemberForm";
        }
        repository.save(member);
        return "redirect:/";
    }
}

package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {
    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return "There is no session";
        }
        //보관된 세션의 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name: {} \n, value : {}", name, session.getAttribute(name)));
        //세션의 ID정보 출력
        log.info("sessionId={}", session.getId());
        //세션을 비활성화 시키는 시간, 1800초 -> 30분 분동안 어떠한 활동도 하지 않을 경우 사용자의 세션이 만료되는 시간
        log.info("session.getMaxInactiveInterval = {}", session.getMaxInactiveInterval());
        log.info("getCreationTime={}", new Date(session.getCreationTime()));
        log.info("getLastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        return "세션 출력";
    }
}

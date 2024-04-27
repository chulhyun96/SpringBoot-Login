package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 접근 제한이 필요치 않은 URL 경로. 회원이 아닌 사람들도 들어올 수 있는 경로
    // 예를 들어 로그인 페이지 or 회원가입 페이지는 로그인 하지 않는 사용자도 접근해야 하므로, 화이트리스트에 추가.
    // 화이트 리스트의 경로로 들어오는 사람은 보안 로직을 생략
    private static final String[] whiteList = {"/","/members/add","/login","/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            log.info("인증 체크 필터 시작: {}", requestURI);
            //반환값이 true일 경우 인증체크 필요
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 시작: {}", requestURI);
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 체크: {}", requestURI);
                    response.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw  e; // 예외 로깅 가능하지만, 톰캣까지 예외를 보내주어야함
        } finally {
            log.info("인증 체크 종료: {}" , requestURI);
        }
    }
    private boolean isLoginCheckPath(String requestURL) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURL);
        // whiteList의 경로와 실제 요청 URL이 하나라도 맞다면(true를 반환하지만, ! -> false를 반환), 인증 체크 불필요
        // 근데 맞는게 하나도 없다면(false를 반환하지만 ! -> true를 반환 ) 인증체크가 필요
    }
}

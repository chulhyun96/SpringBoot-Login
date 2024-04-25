package hello.login.web.filter;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 이미 로그인한 사용자의 요청 정보를 가져올것이기 때문에
        HttpSession session = request.getSession(false);

        if(session != null) {
            Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            // 로그인한 사용자의 세션이 유효하다면
            if (member != null) {
                MDC.put("USERID", member.getLoginId());
            }
        }

        String requestURI = request.getRequestURI();
        try {
            log.info("REQUEST: [{}], USERID [{}]", requestURI, MDC.get("USERID"));
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            throw e;
        } finally {
            // 로그인이 성공했지만, 맨 위의 if문에서 null로 통과 되었기 때문에 null이다.
            log.info("RESPONSE: [{}], USERID [{}]", requestURI, MDC.get("USERID"));
            MDC.remove("REQUEST USERID");
        }
    }

    /*@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // HTTP 요청이 오면 doFilter 메서드가 호출된다.
        // ServletRequest는 HTTP 요청이 아닌 경우까지 고려했기 때문에 HttpServlet이 아닌 Servlet이다.
        // HTTP를 사용한다면 아래와 같이 다운캐스팅해주면 된다.
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        // 이는 HTTP 요청을 구분하기 위해 요청당 임의의 `uuid`를 생성해준다.
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST: [{}], uuid: [{}]", requestURI, uuid);
            // 이 부분이 가장 중요하다. 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출한다. 만약 이 로직을 호출하지 않으면 다음 단계로 진행되지 않는다.
            filterChain.doFilter(servletRequest, servletResponse); // 여기서 서블릿을 호출한 뒤 컨트롤러 메서드를 실행 후 Response logging 실행
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            log.info("RESPONSE: [{}], uuid: [{}]", requestURI, uuid);
        }
    }*/

    @Override
    public void destroy() {
        log.info("LogFilter destroy");
    }


}

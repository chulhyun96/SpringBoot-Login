package hello.login.web.intercepter;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Interceptor;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LogIntercepter implements HandlerInterceptor {
    public static final String USERID = "USERID";
    public static final String HANDLER = "handler";
    public static final String REQUEST_URI = "requestURI";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Interceptor Pre-handle 호출");
        String requestURI = request.getRequestURI();
        MDC.put(REQUEST_URI, requestURI);

        HttpSession session = request.getSession(false);
        if (session != null) {
            Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (member != null) {
                MDC.put(USERID, member.getLoginId());
            }
        }
        if (handler instanceof HandlerMethod) {
            MDC.put(HANDLER, String.valueOf(handler));
        }
        log.info("REQUEST : [{}] [{}] [{}] ", MDC.get(REQUEST_URI), MDC.get(USERID), MDC.get(HANDLER));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("PostHandle: [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Interceptor afterCompletion 호출");
        log.info("RESPONSE : [{}] [{}] [{}] ", MDC.get(REQUEST_URI), MDC.get(USERID), MDC.get(HANDLER));
        if (ex != null) {
            log.error("afterCompletion error!!!", ex);
        }
        MDC.remove(USERID);
        MDC.remove(HANDLER);
        MDC.remove(REQUEST_URI);
    }

}

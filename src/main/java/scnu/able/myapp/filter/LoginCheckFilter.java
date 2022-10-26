package scnu.able.myapp.filter;

import static org.springframework.util.ObjectUtils.isEmpty;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/memLogin.do", "/memLoginForm.do", "/memJoin.do", "/memJoinForm.do", "/css/*", "/memIdCheck.do", "/memNameCheck.do", "/images/*"};


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {


            if (isLoginCheckPath(requestURI)) {

                HttpSession session = httpRequest.getSession(false);
                if (isEmpty(session) || isEmpty(session.getAttribute("mvo"))) {

                    httpResponse.sendRedirect("/memLogin.do");
                    return; // 여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝!
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; // 예외 로깅 가능하지만, 톰캣까지 예외를 보내야 함
        }

    }

    /**
     * 화이트 리스트는 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}

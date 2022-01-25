package com.hmh.memberboard.interceptor;

import com.hmh.memberboard.common.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // proHandle을 쓰는데 있어서 3개의 매개변수는 무조건 선언이 되어있어야함 -> 사용여부 관계 없음.
        // Client -> Server : Request
        // Server -> Client : Response

        // 사용자가 요청한 주소
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        HttpSession session = request.getSession();
        // 세선에 로그인 정보가 있는지 확인
        if (session.getAttribute(SessionConst.LOGIN_ID) == null) {
            session.setAttribute("redirectURL", requestURI);
            response.sendRedirect("/member/login");

            return false;
        } else {

            return true;
        }
    }
}

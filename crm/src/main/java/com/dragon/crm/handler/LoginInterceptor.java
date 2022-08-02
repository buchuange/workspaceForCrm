package com.dragon.crm.handler;

import com.dragon.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof DefaultServletHttpRequestHandler) {
            return true;
        } else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // 如果user不为null, 说明登录过
            if (user != null) {
                return true;
            } else {  // 没有登录过
                String contextPath = request.getContextPath(); // /项目名
                response.sendRedirect(contextPath + "/login.jsp");
                return false;
            }
        }
    }
}

package imnu.jumpserver;

import cn.dev33.satoken.stp.StpUtil;
import imnu.jumpserver.model.User;
import imnu.jumpserver.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Resource
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StpUtil.isLogin()) {
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userRepository.findById(userId).get();
            request.setAttribute("loginUser", user);
        } else {
            // 如果用户未登录或token过期，则重定向到login页面
            response.sendRedirect("/auth/login");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

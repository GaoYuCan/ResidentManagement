package top.sinkdev.rm.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import top.sinkdev.rm.common.entity.User;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        HttpSession session = request.getSession();
        var curUser = (User) session.getAttribute("cur_user");
        if (curUser == null || curUser.getUid() == null || curUser.getUid().length() != 32) {
            // 重定向到登录
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}

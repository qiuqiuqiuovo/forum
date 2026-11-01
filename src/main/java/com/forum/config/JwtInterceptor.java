package com.forum.config;

import com.forum.common.BusinessException;
import com.forum.common.UserContext;
import com.forum.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");

        // 有令牌就解析，把用户信息存进口袋
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);
                Long userId = Long.valueOf(claims.getSubject());
                String username = claims.get("username", String.class);
                UserContext.set(userId, username);
                return true;
            } catch (Exception e) {
                // GET 请求令牌无效也放行（当游客），其他方法抛异常
                if (!"GET".equalsIgnoreCase(request.getMethod())) {
                    throw new BusinessException("令牌无效或已过期");
                }
            }
        }

        // 没带令牌的情况
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;   // GET 游客可看
        }
        throw new BusinessException("未登录或令牌缺失");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.clear();   // 清空口袋，防内存泄漏
    }
}
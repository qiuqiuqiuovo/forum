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
        // GET 请求游客可看，直接放行（不需要令牌）
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 以下非 GET 请求（POST/PUT/DELETE）需要验证令牌
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException("未登录或令牌缺失");
        }
        token = token.substring(7);   // 去掉 "Bearer " 前缀（7 个字符）

        try {
            Claims claims = JwtUtil.parseToken(token);
            Long userId = Long.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            UserContext.set(userId, username);   // 存进口袋
            return true;   // 放行
        } catch (Exception e) {
            throw new BusinessException("令牌无效或已过期");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.clear();   // 清空口袋，防内存泄漏
    }
}
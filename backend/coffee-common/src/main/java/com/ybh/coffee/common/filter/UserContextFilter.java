package com.ybh.coffee.common.filter;

import com.ybh.coffee.common.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("X-User-Id");
            if (header != null && !header.isBlank()) {
                UserContext.setUserId(Long.parseLong(header));
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}

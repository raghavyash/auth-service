package com.rsnvtech.erp.edu.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisRateLimitFilter extends OncePerRequestFilter {
    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS = 5;

    private static final int WINDOW_SECONDS = 60;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        String key = "rate_limit:" + ip;

        String current = redisTemplate.opsForValue().get(key);

        int requests = current == null ? 0 : Integer.parseInt(current);

        if (requests >= MAX_REQUESTS) {

            response.setStatus(429);

            response.getWriter().write("Too many requests");

            return;
        }

        redisTemplate.opsForValue().increment(key);

        redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));

        filterChain.doFilter(request, response);
    }

}

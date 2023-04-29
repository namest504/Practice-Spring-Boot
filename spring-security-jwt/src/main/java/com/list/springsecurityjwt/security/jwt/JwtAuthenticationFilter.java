package com.list.springsecurityjwt.security.jwt;

import com.list.springsecurityjwt.service.AuthService;
import com.list.springsecurityjwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Request Header 에서 토큰을 꺼냄
        LOGGER.info("[JwtAuthenticationFilter] doFilterInternal 시작");
        String jwt = resolveToken(request);
        LOGGER.info("[JwtAuthenticationFilter] request : {}", request);
        LOGGER.info("[JwtAuthenticationFilter] resolveToken(request) : {}", jwt);
        LOGGER.info("[JwtAuthenticationFilter] validateAccessToken 으로 토큰 유효성 검사 시작");
        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt) && jwtService.validateAccessToken(jwt)) {
            Authentication authentication = jwtService.getAuthentication(jwt);
            LOGGER.info("[JwtAuthenticationFilter] Authentication 을 가져와서 SecurityContext 에 저장");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            LOGGER.info("[JwtAuthenticationFilter] 토큰이 비어있는 상태로 진행");
        }

        filterChain.doFilter(request, response);

    }
    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        LOGGER.info("[JwtAuthenticationFilter] resolveToken 시작");
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        LOGGER.info("[JwtAuthenticationFilter] request.getHeader(AUTHORIZATION_HEADER) : {}", bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            LOGGER.info("[JwtAuthenticationFilter] resolveToken 종료 bearerToken.substring(7) : {}", bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        return null;
    }
}

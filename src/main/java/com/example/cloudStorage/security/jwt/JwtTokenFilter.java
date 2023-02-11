package com.example.cloudStorage.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

import com.example.cloudStorage.security.UserDetailsServiceImpl;
import com.example.cloudStorage.service.impl.JwtBlackListServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Фильтрует запрос на наличие токена

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    JwtUtils jwtUtils;
    UserDetailsServiceImpl userDetailsServiceImpl;
    String tokenPrefix = "Bearer ";
    String headerString = "auth-token";
    JwtBlackListServiceImpl jwtBlackListService;


    public JwtTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsServiceImpl,
                          JwtBlackListServiceImpl jwtBlackListService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtBlackListService = jwtBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(headerString);
            if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith(tokenPrefix)) {
                String jwt = authHeader.substring(7);
                //проверяем не пустой ли токен
                if(jwt.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token in Bearer Header");
                    log.error("JwtTokenFilter.class - Invalid JWT Token in Bearer Header");
                    //проверяем есть ли токен в блэклисте(совершен логаут)
                } else if(jwtBlackListService.isBlacklisted(jwt)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please, login");
                    log.error("JwtTokenFilter.class - there is JWT Token in blacklist");
                } else {

                    try {
                        //проверяем получилось ли получить имя пользователя - не было выброшено исключение
                        String username = jwtUtils.validateTokenRetrieveClaim(jwt);
                        //проверяем есть ли такой пользователь в базе данных
                        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                        //получаем пароль и роль из базы данных - авторизация
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                                userDetails.getAuthorities());
                        if(SecurityContextHolder.getContext().getAuthentication() == null) {
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    } catch (JWTVerificationException e) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "Invalid JWT Token");
                            log.error("JwtTokenFilter.class - Invalid JWT Token", e);
                    }
                }
            }
        filterChain.doFilter(request, response);
    }
}

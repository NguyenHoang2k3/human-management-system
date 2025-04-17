package com.lab.server.configs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lab.server.caches.ICacheData;
import com.lab.server.configs.language.MessageSourceHelper;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final MessageSourceHelper messageSourceHelper;
    private final ICacheData<String> caches;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtProvider.getJwtFromRequest(request);

        String[] publicUrls = {"/users/login", "/v3/api-docs/.*", "/v3/api-docs","/swagger-ui/.*", "/swagger-ui.html"};

        for (String publicUrl : publicUrls) {
            if (request.getRequestURI().matches(publicUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        try {
        	 String username = jwtProvider.getUsernameFromToken(jwt);
        	 
        	 String existedUsernameToken = caches.find(jwt);
        	 if(existedUsernameToken != null) {
        		 response.sendError(HttpServletResponse.SC_FORBIDDEN, messageSourceHelper.getMessage("warning.tokenBanned"));
        		 return;
        	 } 

             UserDetails userDetails = userDetailsService.loadUserByUsername(username);

             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                 userDetails, jwt, userDetails.getAuthorities());

             SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error(messageSourceHelper.getMessage("error.notFound"));
            response.sendError(HttpServletResponse.SC_FORBIDDEN, messageSourceHelper.getMessage("warning.accessDenied"));
            return;
        }

        filterChain.doFilter(request, response);
    }
}

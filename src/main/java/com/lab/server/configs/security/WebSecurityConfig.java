package com.lab.server.configs.security;


import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lab.server.caches.ICacheData;
import com.lab.server.configs.language.DetectLanguageInterceptor;
import com.lab.server.configs.language.MessageSourceHelper;

import io.jsonwebtoken.lang.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final DetectLanguageInterceptor languageInterceptor;
    private final MessageSourceHelper messageSourceHelper;
    private final ICacheData<String> caches;
    private final CustomAccessDecisionVoter customAccessDecisionVoter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())  
            	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            	.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); 

        return http.build();
    }
    @Bean
    public AccessDecisionManager accessDecisionManager() {
    	List<AccessDecisionVoter<?>> voters = new ArrayList<>();
    	voters.add(customAccessDecisionVoter);
    	voters.add(new AuthenticatedVoter());
        return new AffirmativeBased(voters); 
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtProvider, userDetailsService, messageSourceHelper, caches);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(languageInterceptor);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(false);
        cors.addAllowedOriginPattern(CorsConfiguration.ALL);
        cors.addAllowedMethod(CorsConfiguration.ALL);
        cors.addAllowedHeader(CorsConfiguration.ALL);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
//    @Bean
//    public DispatcherServletPath dispatcherServletPath() {
//        return () -> "/";
//    }
//    @Bean
//    public ServletRegistrationBean<DispatcherServlet> dispatcherServlet(DispatcherServlet dispatcherServlet) {
//        ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(dispatcherServlet);
//        registration.addUrlMappings("/");
//        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true); // Bật ném lỗi nếu không tìm thấy API
//        return registration;
//    }
}

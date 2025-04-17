package com.lab.server.configs.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.lab.lib.exceptions.UnAuthorizationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class SecurityHelper {

	public String getCurrentUserLogin() throws UnAuthorizationException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnAuthorizationException("User not found");
        } else {
            return ((UserDetails)authentication.getPrincipal()).getUsername();
        }
    }
}

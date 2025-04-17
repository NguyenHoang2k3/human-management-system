package com.lab.server.configs.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@SuppressWarnings("deprecation")
@Component
public class CustomAccessDecisionVoter implements AccessDecisionVoter<Object> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute() != null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ACCESS_DENIED;
        }

        for (ConfigAttribute attribute : attributes) {
            String requiredPermission = attribute.getAttribute(); // Lấy quyền cần kiểm tra từ @PreAuthorize
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals(requiredPermission)) {
                    return ACCESS_GRANTED;
                }
            }
        }

        return ACCESS_DENIED;
    }
}

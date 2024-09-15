package com.am.crm.customer.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityHandler {
    // Extract the current username from the SecurityContext
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("username");
        }

        return authentication.getPrincipal().toString();
    }
}

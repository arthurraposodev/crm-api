package com.am.crm.customer.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityHandlerTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUsername_WithNoAuthentication_ReturnsNull() {
        assertNull(SecurityHandler.getCurrentUsername());
    }

    @Test
    void getCurrentUsername_WithNonJwtAuthentication_ReturnsPrincipalToString() {
        Authentication auth = new TestingAuthenticationToken("testUser", "password");
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals("testUser", SecurityHandler.getCurrentUsername());
    }

    @Test
    void getCurrentUsername_WithJwtAuthentication_ReturnsUsernameFromJwt() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("username")).thenReturn("jwtUser");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals("jwtUser", SecurityHandler.getCurrentUsername());
    }

    @Test
    void getCurrentUsername_WithUnauthenticatedPrincipal_ReturnsNull() {
        Authentication auth = new TestingAuthenticationToken("testUser", "password");
        auth.setAuthenticated(false);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertNull(SecurityHandler.getCurrentUsername());
    }
}
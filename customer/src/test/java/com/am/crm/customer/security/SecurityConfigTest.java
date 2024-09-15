package com.am.crm.customer.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private HttpSecurity httpSecurity;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        httpSecurity = mock(HttpSecurity.class, RETURNS_SELF);
    }

    @Test
    void jwtAuthenticationConverter_ShouldConfigureCorrectly() throws Exception {
        // Arrange
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_SELF);

        // Act
        securityConfig.securityFilterChain(http);

        // Assert
        verify(http).oauth2ResourceServer(any());
    }
}
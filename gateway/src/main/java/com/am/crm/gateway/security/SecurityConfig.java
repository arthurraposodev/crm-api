package com.am.crm.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/health").permitAll()  // Allow unauthenticated access to /actuator/health

                        // Authenticate other routes
                        .anyExchange().permitAll())

                // Enable OAuth2 login
                .oauth2Login(withDefaults())
                .oidcLogout(logout -> logout
                        .backChannel(Customizer.withDefaults())
                )

                // Disable CSRF for WebFlux
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }
}

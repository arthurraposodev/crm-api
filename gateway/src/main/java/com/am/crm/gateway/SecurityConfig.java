package com.am.crm.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        // Permit all access to /customers/hello
                        .pathMatchers("/actuator/health").permitAll()  // Allow unauthenticated access to /actuator/health
                        .pathMatchers("/customers/hello").authenticated()

                        // Authenticate other routes
                        .anyExchange().permitAll())

                // Enable OAuth2 login
                .oauth2Login(withDefaults())

                // Disable CSRF for WebFlux
                .csrf(withDefaults());
        return http.build();
    }
}

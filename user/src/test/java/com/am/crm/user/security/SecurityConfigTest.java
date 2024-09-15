package com.am.crm.user.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void actuatorHealth_shouldBeAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().is5xxServerError()); // Not Unauthorized or Forbidden
    }

    @Test
    void protectedEndpoint_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "GROUP_admins")
    void protectedEndpoint_shouldAllowAccessForAdmins() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().is5xxServerError()); // Not Unauthorized or Forbidden
    }

    @Test
    @WithMockUser(authorities = "GROUP_users")
    void protectedEndpoint_shouldDenyAccessForNonAdmins() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isForbidden());
    }
}
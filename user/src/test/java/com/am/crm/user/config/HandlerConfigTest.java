package com.am.crm.user.config;

import com.am.crm.user.features.admin.AdminUserHandler;
import com.am.crm.user.features.create.CreateUserHandler;
import com.am.crm.user.features.delete.DeleteUserHandler;
import com.am.crm.user.features.list.ListUsersHandler;
import com.am.crm.user.features.update.UpdateUserHandler;
import com.am.crm.user.infrastructure.OAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {HandlerConfig.class})
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
class HandlerConfigTest {

    @MockBean
    private OAuthService oAuthService;

    @Autowired
    private CreateUserHandler createUserHandler;

    @Autowired
    private DeleteUserHandler deleteUserHandler;

    @Autowired
    private ListUsersHandler listUsersHandler;

    @Autowired
    private AdminUserHandler adminUserHandler;

    @Autowired
    private UpdateUserHandler updateUserHandler;

    @Test
    void createUserHandler_shouldBeConfigured() {
        assertNotNull(createUserHandler);
    }

    @Test
    void deleteUserHandler_shouldBeConfigured() {
        assertNotNull(deleteUserHandler);
    }

    @Test
    void listUsersHandler_shouldBeConfigured() {
        assertNotNull(listUsersHandler);
    }

    @Test
    void adminUserHandler_shouldBeConfigured() {
        assertNotNull(adminUserHandler);
    }

    @Test
    void updateUserHandler_shouldBeConfigured() {
        assertNotNull(updateUserHandler);
    }
}

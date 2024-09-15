package com.am.crm.user.features.create;

import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.web.CreateUserCommand;
import com.am.crm.user.web.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CreateUserHandlerTest {

    @Mock
    private OAuthService oAuthService;

    private CreateUserHandler createUserHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserHandler = new CreateUserHandler(oAuthService);
    }

    @Test
    void handle_shouldCreateUser() {
        // Arrange
        CreateUserCommand command = new CreateUserCommand();
        command.setEmail("test@example.com");
        command.setPhoneNumber("+1234567890");
        command.setUsername("testuser");
        command.setTemporaryPassword("tempPass123");
        UserQuery expectedUser = new UserQuery();
        expectedUser.setUsername("testuser");
        expectedUser.setEmail("test@example.com");
        expectedUser.setPhoneNumber("+1234567890");
        when(oAuthService.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(expectedUser);

        // Act
        UserQuery result = createUserHandler.handle(command);

        // Assert
        assertEquals(expectedUser, result);
        verify(oAuthService).createUser("testuser", "test@example.com", "+1234567890", "tempPass123");
    }
}

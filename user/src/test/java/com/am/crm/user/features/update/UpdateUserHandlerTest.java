package com.am.crm.user.features.update;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.web.UpdateUserCommand;
import com.am.crm.user.web.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateUserHandlerTest {

    @Mock
    private OAuthService oAuthService;

    private UpdateUserHandler updateUserHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateUserHandler = new UpdateUserHandler(oAuthService);
    }

    @Test
    void handle_shouldUpdateUser() {
        // Arrange
        String username = "testuser";
        UpdateUserCommand command = new UpdateUserCommand();
        command.setEmail("newemail@example.com");
        command.setPhoneNumber("+9876543210");

        Map<String, String> expectedAttributes = new HashMap<>();
        expectedAttributes.put("email", "newemail@example.com");
        expectedAttributes.put("phone_number", "+9876543210");

        UserQuery expectedUser = new UserQuery();
        expectedUser.setUsername(username);
        expectedUser.setEmail("newemail@example.com");
        expectedUser.setPhoneNumber("+9876543210");
        when(oAuthService.updateUserAttributes(eq(username), anyMap())).thenReturn(expectedUser);

        // Act
        UserQuery result = updateUserHandler.handle(username, command);

        // Assert
        assertEquals(expectedUser, result);
        verify(oAuthService).updateUserAttributes(eq(username), eq(expectedAttributes));
    }

    @Test
    void handle_shouldThrowUserNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        UpdateUserCommand command = new UpdateUserCommand();
        when(oAuthService.updateUserAttributes(eq(username), anyMap())).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> updateUserHandler.handle(username, command));
        verify(oAuthService).updateUserAttributes(eq(username), anyMap());
    }
}

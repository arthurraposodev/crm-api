package com.am.crm.user.features.delete;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteUserHandlerTest {

    @Mock
    private OAuthService oAuthService;

    private DeleteUserHandler deleteUserHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteUserHandler = new DeleteUserHandler(oAuthService);
    }

    @Test
    void handle_shouldDeleteUser() {
        // Arrange
        String username = "testuser";
        doNothing().when(oAuthService).deleteUser(username);

        // Act
        deleteUserHandler.handle(username);

        // Assert
        verify(oAuthService).deleteUser(username);
    }

    @Test
    void handle_shouldThrowUserNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        doThrow(new RuntimeException("User not found")).when(oAuthService).deleteUser(username);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> deleteUserHandler.handle(username));
        verify(oAuthService).deleteUser(username);
    }
}

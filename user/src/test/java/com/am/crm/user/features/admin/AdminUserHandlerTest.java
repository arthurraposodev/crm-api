package com.am.crm.user.features.admin;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdminUserHandlerTest {

    @Mock
    private OAuthService oAuthService;

    private AdminUserHandler adminUserHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminUserHandler = new AdminUserHandler(oAuthService);
    }

    @Test
    void handle_shouldMakeUserAdmin() {
        // Arrange
        String username = "testuser";
        doNothing().when(oAuthService).makeUserAdmin(username);

        // Act
        adminUserHandler.handle(username);

        // Assert
        verify(oAuthService).makeUserAdmin(username);
    }

    @Test
    void handle_shouldThrowUserNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        doThrow(new RuntimeException("User not found")).when(oAuthService).makeUserAdmin(username);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> adminUserHandler.handle(username));
        verify(oAuthService).makeUserAdmin(username);
    }
}

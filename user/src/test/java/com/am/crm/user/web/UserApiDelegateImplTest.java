package com.am.crm.user.web;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.features.admin.AdminUserHandler;
import com.am.crm.user.features.create.CreateUserHandler;
import com.am.crm.user.features.delete.DeleteUserHandler;
import com.am.crm.user.features.list.ListUsersHandler;
import com.am.crm.user.features.update.UpdateUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserApiDelegateImplTest {

    @Mock
    private CreateUserHandler createUserHandler;
    @Mock
    private DeleteUserHandler deleteUserHandler;
    @Mock
    private ListUsersHandler listUsersHandler;
    @Mock
    private UpdateUserHandler updateUserHandler;
    @Mock
    private AdminUserHandler adminUserHandler;

    private UserApiDelegateImpl userApiDelegate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userApiDelegate = new UserApiDelegateImpl(createUserHandler, deleteUserHandler, listUsersHandler, updateUserHandler, adminUserHandler);
    }

    @Test
    void listUsers_shouldReturnListOfUsers() {
        // Arrange
        UserQuery userQuery1 = new UserQuery();
        userQuery1.setUsername("user1");
        userQuery1.setEmail("user1@example.com");
        userQuery1.setPhoneNumber("+1234567890");
        UserQuery userQuery2 = new UserQuery();
        userQuery2.setUsername("user2");
        userQuery2.setEmail("user2@example.com");
        userQuery2.setPhoneNumber("+0987654321");
        List<UserQuery> expectedUsers = Arrays.asList(
                userQuery1,
                userQuery2
        );
        when(listUsersHandler.handle()).thenReturn(expectedUsers);

        // Act
        ResponseEntity<List<UserQuery>> response = userApiDelegate.listUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
        verify(listUsersHandler).handle();
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        // Arrange
        CreateUserCommand command = new CreateUserCommand();
        command.setUsername("newuser");
        command.setEmail("newuser@example.com");
        command.setPhoneNumber("+1234567890");
        command.setTemporaryPassword("tempPass123");
        UserQuery createdUser = new UserQuery();
        createdUser.setUsername("newuser");
        createdUser.setEmail("newuser@example.com");
        createdUser.setPhoneNumber("+1234567890");
        when(createUserHandler.handle(command)).thenReturn(createdUser);

        // Act
        ResponseEntity<UserQuery> response = userApiDelegate.createUser(command);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
        verify(createUserHandler).handle(command);
    }

    @Test
    void makeUserAdmin_shouldReturnOkWhenSuccessful() {
        // Arrange
        String username = "testuser";
        doNothing().when(adminUserHandler).handle(username);

        // Act
        ResponseEntity<Void> response = userApiDelegate.makeUserAdmin(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminUserHandler).handle(username);
    }

    @Test
    void makeUserAdmin_shouldReturnNotFoundWhenUserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        doThrow(new UserNotFoundException("User not found")).when(adminUserHandler).handle(username);

        // Act
        ResponseEntity<Void> response = userApiDelegate.makeUserAdmin(username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(adminUserHandler).handle(username);
    }

    @Test
    void deleteUser_shouldReturnNoContent() {
        // Arrange
        String username = "testuser";
        doNothing().when(deleteUserHandler).handle(username);

        // Act
        ResponseEntity<Void> response = userApiDelegate.deleteUser(username);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteUserHandler).handle(username);
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() {
        // Arrange
        String username = "testuser";
        UpdateUserCommand command = new UpdateUserCommand();
        command.setEmail("newemail@example.com");
        command.setPhoneNumber("+9876543210");
        UserQuery updatedUser = new UserQuery();
        updatedUser.setUsername(username);
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setPhoneNumber("+9876543210");
        when(updateUserHandler.handle(username, command)).thenReturn(updatedUser);

        // Act
        ResponseEntity<UserQuery> response = userApiDelegate.updateUser(username, command);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(updateUserHandler).handle(username, command);
    }}

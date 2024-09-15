package com.am.crm.user.features.list;

import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.web.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListUsersHandlerTest {

    @Mock
    private OAuthService oAuthService;

    private ListUsersHandler listUsersHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listUsersHandler = new ListUsersHandler(oAuthService);
    }

    @Test
    void handle_shouldReturnListOfUsers() {
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
        when(oAuthService.listUsers()).thenReturn(expectedUsers);

        // Act
        List<UserQuery> result = listUsersHandler.handle();

        // Assert
        assertEquals(expectedUsers, result);
        verify(oAuthService).listUsers();
    }
}
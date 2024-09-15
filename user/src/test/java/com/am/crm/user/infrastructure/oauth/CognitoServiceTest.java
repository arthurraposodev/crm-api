package com.am.crm.user.infrastructure.oauth;

import com.am.crm.user.infrastructure.oauth.mapper.UserMapper;
import com.am.crm.user.web.UserQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CognitoServiceTest {

    @Mock
    private CognitoIdentityProviderClient cognitoClient;

    @Mock
    private UserMapper userMapper;

    private CognitoService cognitoService;

    private static final String TEST_USER_POOL_ID = "test-pool-id";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        cognitoService = new CognitoService(cognitoClient, userMapper);
        // Use reflection to set the userPoolId
        Field userPoolIdField = CognitoService.class.getDeclaredField("userPoolId");
        userPoolIdField.setAccessible(true);
        userPoolIdField.set(cognitoService, TEST_USER_POOL_ID);
    }

    @Test
    void listUsers_shouldReturnListOfUsers() {
        // Arrange
        UserType user1 = UserType.builder().username("user1").build();
        UserType user2 = UserType.builder().username("user2").build();
        ListUsersResponse listUsersResponse = ListUsersResponse.builder()
                .users(Arrays.asList(user1, user2))
                .build();
        when(cognitoClient.listUsers(any(ListUsersRequest.class))).thenReturn(listUsersResponse);

        UserQuery userQuery1 = new UserQuery();
        userQuery1.setUsername("user1");
        userQuery1.setEmail("user1@example.com");
        userQuery1.setPhoneNumber("+1234567890");
        UserQuery userQuery2 = new UserQuery();
        userQuery2.setUsername("user2");
        userQuery2.setEmail("user2@example.com");
        userQuery2.setPhoneNumber("+0987654321");
        when(userMapper.toDto(user1)).thenReturn(userQuery1);
        when(userMapper.toDto(user2)).thenReturn(userQuery2);

        // Act
        List<UserQuery> result = cognitoService.listUsers();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(userQuery1));
        assertTrue(result.contains(userQuery2));
        verify(cognitoClient).listUsers(any(ListUsersRequest.class));
        verify(userMapper, times(2)).toDto(any(UserType.class));
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String phone = "+1234567890";
        String temporaryPassword = "tempPass123";

        UserType createdUser = UserType.builder().username(username).build();
        AdminCreateUserResponse createUserResponse = AdminCreateUserResponse.builder()
                .user(createdUser)
                .build();
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class))).thenReturn(createUserResponse);

        UserQuery expectedUserQuery = new UserQuery();
        expectedUserQuery.setUsername(username);
        expectedUserQuery.setPhoneNumber(phone);
        expectedUserQuery.setEmail(email);
        when(userMapper.toDto(createdUser)).thenReturn(expectedUserQuery);

        // Act
        UserQuery result = cognitoService.createUser(username, email, phone, temporaryPassword);

        // Assert
        assertEquals(expectedUserQuery, result);
        verify(cognitoClient).adminCreateUser(any(AdminCreateUserRequest.class));
        verify(userMapper).toDto(createdUser);
    }

    @Test
    void makeUserAdmin_shouldAddUserToAdminGroup() {
        // Arrange
        String username = "testuser";
        AdminAddUserToGroupRequest expectedRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(TEST_USER_POOL_ID)
                .username(username)
                .groupName("admins")
                .build();

        // Act
        cognitoService.makeUserAdmin(username);

        // Assert
        verify(cognitoClient).adminAddUserToGroup(expectedRequest);
    }

    @Test
    void deleteUser_shouldDeleteUserFromCognito() {
        // Arrange
        String username = "testuser";
        AdminDeleteUserRequest expectedRequest = AdminDeleteUserRequest.builder()
                .userPoolId(TEST_USER_POOL_ID)
                .username(username)
                .build();

        // Act
        cognitoService.deleteUser(username);

        // Assert
        verify(cognitoClient).adminDeleteUser(expectedRequest);
    }

    @Test
    void updateUserAttributes_shouldUpdateAndReturnUser() {
        // Arrange
        String username = "testuser";
        Map<String, String> attributes = new HashMap<>();
        attributes.put("email", "newemail@example.com");
        attributes.put("phone_number", "+1234567890");

        AdminGetUserResponse getUserResponse = AdminGetUserResponse.builder()
                .username(username)
                .userAttributes(Arrays.asList(
                        AttributeType.builder().name("email").value("newemail@example.com").build(),
                        AttributeType.builder().name("phone_number").value("+1234567890").build()
                ))
                .build();

        UserQuery expectedUserQuery = new UserQuery();
        expectedUserQuery.setEmail("newemail@example.com");
        expectedUserQuery.setUsername(username);
        expectedUserQuery.setPhoneNumber("+1234567890");

        when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class))).thenReturn(getUserResponse);
        when(userMapper.toDto(getUserResponse)).thenReturn(expectedUserQuery);

        // Act
        UserQuery result = cognitoService.updateUserAttributes(username, attributes);

        // Assert
        ArgumentCaptor<AdminUpdateUserAttributesRequest> requestCaptor = ArgumentCaptor.forClass(AdminUpdateUserAttributesRequest.class);
        verify(cognitoClient).adminUpdateUserAttributes(requestCaptor.capture());

        AdminUpdateUserAttributesRequest capturedRequest = requestCaptor.getValue();
        assertEquals(TEST_USER_POOL_ID, capturedRequest.userPoolId());
        assertEquals(username, capturedRequest.username());

        List<AttributeType> capturedAttributes = capturedRequest.userAttributes();
        assertEquals(2, capturedAttributes.size());
        assertTrue(capturedAttributes.contains(AttributeType.builder().name("email").value("newemail@example.com").build()));
        assertTrue(capturedAttributes.contains(AttributeType.builder().name("phone_number").value("+1234567890").build()));

        verify(cognitoClient).adminGetUser(any(AdminGetUserRequest.class));
        verify(userMapper).toDto(getUserResponse);
        assertEquals(expectedUserQuery, result);
    }

    @Test
    void updateUserAttributes_shouldThrowUserNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        Map<String, String> attributes = new HashMap<>();
        attributes.put("email", "newemail@example.com");

        when(cognitoClient.adminUpdateUserAttributes(any(AdminUpdateUserAttributesRequest.class)))
                .thenThrow(UserNotFoundException.class);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> cognitoService.updateUserAttributes(username, attributes));
    }

    @Test
    void findByUsername_shouldReturnUserWhenFound() {
        // Arrange
        String username = "testuser";
        AdminGetUserResponse getUserResponse = AdminGetUserResponse.builder()
                .username(username)
                .userAttributes(Arrays.asList(
                        AttributeType.builder().name("email").value("test@example.com").build(),
                        AttributeType.builder().name("phone_number").value("+1234567890").build()
                ))
                .build();

        UserQuery expectedUserQuery = new UserQuery();
        expectedUserQuery.setUsername(username);
        expectedUserQuery.setEmail("test@example.com");
        expectedUserQuery.setPhoneNumber("+1234567890");

        when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class))).thenReturn(getUserResponse);
        when(userMapper.toDto(getUserResponse)).thenReturn(expectedUserQuery);

        // Act
        Optional<UserQuery> result = cognitoService.findByUsername(username);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUserQuery, result.get());
        verify(cognitoClient).adminGetUser(any(AdminGetUserRequest.class));
        verify(userMapper).toDto(getUserResponse);
    }

    @Test
    void findByUsername_shouldReturnEmptyOptionalWhenUserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(cognitoClient.adminGetUser(any(AdminGetUserRequest.class)))
                .thenThrow(software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException.class);

        // Act
        Optional<UserQuery> result = cognitoService.findByUsername(username);

        // Assert
        assertTrue(result.isEmpty());
        verify(cognitoClient).adminGetUser(any(AdminGetUserRequest.class));
    }

    // Add more tests for other methods (makeUserAdmin, deleteUser, updateUserAttributes, findByUsername)
}

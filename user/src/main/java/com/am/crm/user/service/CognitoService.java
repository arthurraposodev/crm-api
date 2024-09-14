package com.am.crm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoService {
    private final CognitoIdentityProviderClient cognitoClient;
    @Value("${USER_POOL_ID}")
    private String userPoolId;

    // 1. List all users
    public ListUsersResponse listUsers() {
        ListUsersRequest request = ListUsersRequest.builder()
                .userPoolId(userPoolId)
                .build();

        return cognitoClient.listUsers(request);
    }

    // 2. Create a new user
    public AdminCreateUserResponse createUser(String username, String email, String temporaryPassword) {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .temporaryPassword(temporaryPassword)
                .userAttributes(AttributeType.builder().name("email").value(email).build())
                .build();

        return cognitoClient.adminCreateUser(request);
    }

    // 3. Make a user an admin (add to admin group)
    public AdminAddUserToGroupResponse makeUserAdmin(String username) {
        AdminAddUserToGroupRequest request = AdminAddUserToGroupRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .groupName("admins")
                .build();

        return cognitoClient.adminAddUserToGroup(request);
    }

    // 4. Delete a user
    public AdminDeleteUserResponse deleteUser(String username) {
        AdminDeleteUserRequest request = AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .build();

        return cognitoClient.adminDeleteUser(request);
    }

    // 5. Update user attributes
    public AdminUpdateUserAttributesResponse updateUserAttributes(String username, Map<String, String> attributes) {
        List<AttributeType> updatedAttributes = attributes.entrySet().stream()
                .map(entry -> AttributeType.builder().name(entry.getKey()).value(entry.getValue()).build())
                .toList();

        AdminUpdateUserAttributesRequest request = AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .userAttributes(updatedAttributes)
                .build();

        return cognitoClient.adminUpdateUserAttributes(request);
    }
}

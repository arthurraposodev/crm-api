package com.am.crm.user.infrastructure.oauth;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.infrastructure.oauth.mapper.UserMapper;
import com.am.crm.user.web.UserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CognitoService implements OAuthService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final UserMapper userMapper;

    @Value("${USER_POOL_ID}")
    private String userPoolId;

    @Override
    public List<UserQuery> listUsers() {
        ListUsersRequest request = ListUsersRequest.builder()
                .userPoolId(userPoolId)
                .build();

        ListUsersResponse response = cognitoClient.listUsers(request);
        return response.users().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserQuery createUser(String username, String email, String phone, String temporaryPassword) {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .temporaryPassword(temporaryPassword)
                .userAttributes(AttributeType.builder().name("phone_number").value(phone).build(),
                        AttributeType.builder().name("email").value(email).build())
                .build();

        AdminCreateUserResponse response = cognitoClient.adminCreateUser(request);
        return userMapper.toDto(response.user());
    }

    @Override
    public void makeUserAdmin(String username) {
        AdminAddUserToGroupRequest request = AdminAddUserToGroupRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .groupName("admins")
                .build();

        cognitoClient.adminAddUserToGroup(request);
    }

    @Override
    public void deleteUser(String username) {
        AdminDeleteUserRequest request = AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .build();

        cognitoClient.adminDeleteUser(request);
    }

    @Override
    public UserQuery updateUserAttributes(String username, Map<String, String> attributes) {
        List<AttributeType> updatedAttributes = attributes.entrySet().stream()
                .map(entry -> AttributeType.builder().name(entry.getKey()).value(entry.getValue()).build())
                .toList();

        AdminUpdateUserAttributesRequest request = AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .userAttributes(updatedAttributes)
                .build();

        cognitoClient.adminUpdateUserAttributes(request);
        return findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found after update: " + username));
    }

    @Override
    public Optional<UserQuery> findByUsername(String username) {
        try {
            AdminGetUserRequest request = AdminGetUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();

            AdminGetUserResponse response = cognitoClient.adminGetUser(request);
            return Optional.of(userMapper.toDto(response));
        } catch (software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException e) {
            return Optional.empty();
        }
    }
}
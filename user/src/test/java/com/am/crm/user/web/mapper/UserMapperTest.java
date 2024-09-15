package com.am.crm.user.web.mapper;

import com.am.crm.user.infrastructure.oauth.mapper.UserMapper;
import com.am.crm.user.web.UserQuery;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDto_fromUserType_shouldMapCorrectly() {
        UserType user = UserType.builder()
                .username("testuser")
                .attributes(Arrays.asList(
                        AttributeType.builder().name("email").value("test@example.com").build(),
                        AttributeType.builder().name("phone_number").value("+1234567890").build()
                ))
                .build();

        UserQuery result = userMapper.toDto(user);

        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("+1234567890", result.getPhoneNumber());
    }

    @Test
    void toDto_fromAdminGetUserResponse_shouldMapCorrectly() {
        AdminGetUserResponse user = AdminGetUserResponse.builder()
                .username("testuser")
                .userAttributes(Arrays.asList(
                        AttributeType.builder().name("email").value("test@example.com").build(),
                        AttributeType.builder().name("phone_number").value("+1234567890").build()
                ))
                .build();

        UserQuery result = userMapper.toDto(user);

        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("+1234567890", result.getPhoneNumber());
    }
}

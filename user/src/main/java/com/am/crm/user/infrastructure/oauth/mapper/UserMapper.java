package com.am.crm.user.infrastructure.oauth.mapper;

import com.am.crm.user.web.UserQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "phoneNumber", expression = "java(getPhoneFromAttributes(user.attributes()))")
    @Mapping(target = "username", expression = "java(user.username())")
    @Mapping(target = "email", expression = "java(getEmailFromAttributes(user.attributes()))")
    UserQuery toDto(UserType user);

    @Mapping(target = "phoneNumber", expression = "java(getPhoneFromAttributes(user.userAttributes()))")
    @Mapping(target = "username", expression = "java(user.username())")
    @Mapping(expression = "java(getEmailFromAttributes(user.userAttributes()))", target = "email")
    UserQuery toDto(AdminGetUserResponse user);

    default String getEmailFromAttributes(List<AttributeType> attributes) {
        return attributes.stream()
                .filter(attr -> "email".equals(attr.name()))
                .findFirst()
                .map(AttributeType::value)
                .orElse(null);
    }
    default String getPhoneFromAttributes(List<AttributeType> attributes) {
        return attributes.stream()
                .filter(attr -> "phone_number".equals(attr.name()))
                .findFirst()
                .map(AttributeType::value)
                .orElse(null);
    }
}


package com.am.crm.user.features.create;

import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.web.CreateUserCommand;
import com.am.crm.user.web.UserQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserHandler {
    private final OAuthService oAuthService;

    public UserQuery handle(CreateUserCommand command) {
        return oAuthService.createUser(command.getUsername(), command.getEmail(), command.getPhoneNumber(), command.getTemporaryPassword());
    }
}

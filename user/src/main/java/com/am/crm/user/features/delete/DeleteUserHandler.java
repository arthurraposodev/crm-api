package com.am.crm.user.features.delete;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserHandler {

    private final OAuthService oAuthService;

    public void handle(String username) {
        try {
            oAuthService.deleteUser(username);
        } catch (RuntimeException e) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
    }
}


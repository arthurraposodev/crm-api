package com.am.crm.user.features.admin;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminUserHandler {

    private final OAuthService oAuthService;

    public void handle(String username) {
        try {
            oAuthService.makeUserAdmin(username);
        } catch (RuntimeException e) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
    }
}
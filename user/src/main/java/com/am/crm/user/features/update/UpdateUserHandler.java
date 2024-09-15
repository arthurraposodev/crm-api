package com.am.crm.user.features.update;

import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.web.UpdateUserCommand;
import com.am.crm.user.web.UserQuery;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UpdateUserHandler {

    private final OAuthService oAuthService;

    public UserQuery handle(String username, UpdateUserCommand command) {
        try {
            Map<String, String> attributes = new HashMap<>();
            if (command.getEmail() != null) {
                attributes.put("email", command.getEmail());
            }
            if (command.getPhoneNumber() != null) {
                attributes.put("phone_number", command.getPhoneNumber());
            }

            return oAuthService.updateUserAttributes(username, attributes);
        } catch (RuntimeException e) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
    }
}


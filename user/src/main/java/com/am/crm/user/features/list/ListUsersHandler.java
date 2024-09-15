package com.am.crm.user.features.list;

import com.am.crm.user.infrastructure.OAuthService;
import com.am.crm.user.web.UserQuery;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListUsersHandler {
    private final OAuthService oAuthService;

    public List<UserQuery> handle() {
        return oAuthService.listUsers();
    }
}

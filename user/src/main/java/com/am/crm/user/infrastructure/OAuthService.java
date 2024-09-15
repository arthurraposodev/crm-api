package com.am.crm.user.infrastructure;

import com.am.crm.user.web.UserQuery;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OAuthService {
    List<UserQuery> listUsers();
    UserQuery createUser(String username, String email, String phone, String temporaryPassword);
    void makeUserAdmin(String username);
    void deleteUser(String username);
    UserQuery updateUserAttributes(String username, Map<String, String> attributes);
    Optional<UserQuery> findByUsername(String username);
}

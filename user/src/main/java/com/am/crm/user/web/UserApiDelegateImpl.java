package com.am.crm.user.web;

import com.am.crm.user.api.V1ApiDelegate;
import com.am.crm.user.exception.UserNotFoundException;
import com.am.crm.user.features.admin.AdminUserHandler;
import com.am.crm.user.features.create.CreateUserHandler;
import com.am.crm.user.features.delete.DeleteUserHandler;
import com.am.crm.user.features.list.ListUsersHandler;
import com.am.crm.user.features.update.UpdateUserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserApiDelegateImpl implements V1ApiDelegate {

    private final CreateUserHandler createUserHandler;
    private final DeleteUserHandler deleteUserHandler;
    private final ListUsersHandler listUsersHandler;
    private final UpdateUserHandler updateUserHandler;
    private final AdminUserHandler adminUserHandler;

    @Override
    public ResponseEntity<List<UserQuery>> listUsers() {
        List<UserQuery> users = listUsersHandler.handle();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserQuery> createUser(CreateUserCommand command) {
        UserQuery createdUser = createUserHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    public ResponseEntity<Void> makeUserAdmin(String username) {
        try {
            adminUserHandler.handle(username);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(String username) {
        deleteUserHandler.handle(username);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserQuery> updateUser(String username, UpdateUserCommand command) {
        UserQuery updatedUser = updateUserHandler.handle(username, command);
        return ResponseEntity.ok(updatedUser);
    }
}
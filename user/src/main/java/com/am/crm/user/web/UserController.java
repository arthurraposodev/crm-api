package com.am.crm.user.web;

import com.am.crm.user.service.CognitoService;
import com.am.crm.user.web.response.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/v1")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('GROUP_admins')")
public class UserController {
    private final CognitoService userService;

    // 1. List users
    @GetMapping
    public List<UserData> listUsers() {
        return userService.listUsers().users().stream().map(user -> new UserData(user.username(), user.attributes().stream()
                        .filter(attribute -> attribute.name().equals("phone_number"))
                        .findFirst()
                        .map(AttributeType::value)
                        .orElse("NOT_FOUND")))
                .toList();
    }

    // 2. Create a new user
    @PostMapping
    public AdminCreateUserResponse createUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        return userService.createUser(username, email, password);
    }

    // 3. Make a user an admin
    @PutMapping("/{username}/admin")
    public ResponseEntity<Object> makeUserAdmin(@PathVariable String username) {
        try {
            userService.makeUserAdmin(username).sdkHttpResponse();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 4. Delete a user
    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username).sdkHttpResponse();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 5. Update user attributes
    @PutMapping("/{username}")
    public ResponseEntity<Object> updateUser(
            @PathVariable String username,
            @RequestBody Map<String, String> attributes) {
        try {
            userService.updateUserAttributes(username, attributes);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}

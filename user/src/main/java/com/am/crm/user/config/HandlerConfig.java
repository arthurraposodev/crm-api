package com.am.crm.user.config;

import com.am.crm.user.features.admin.AdminUserHandler;
import com.am.crm.user.features.create.CreateUserHandler;
import com.am.crm.user.features.delete.DeleteUserHandler;
import com.am.crm.user.features.list.ListUsersHandler;
import com.am.crm.user.features.update.UpdateUserHandler;
import com.am.crm.user.infrastructure.OAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {
    @Bean
    public CreateUserHandler createUserHandler(OAuthService oAuthService) {
        return new CreateUserHandler(oAuthService);
    }

    @Bean
    public DeleteUserHandler deleteUserHandler(OAuthService oAuthService) {
        return new DeleteUserHandler(oAuthService);
    }

    @Bean
    public ListUsersHandler listUsersHandler(OAuthService oAuthService) {
        return new ListUsersHandler(oAuthService);
    }

    @Bean
    public AdminUserHandler adminUserHandler(OAuthService oAuthService) {
        return new AdminUserHandler(oAuthService);
    }

    @Bean
    public UpdateUserHandler updateUserHandler(OAuthService oAuthService) {
        return new UpdateUserHandler(oAuthService);
    }
}

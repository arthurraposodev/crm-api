package com.am.crm.customer;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ROLE_users')")
    public String hello() {
        return "Hello, World!";
    }
}

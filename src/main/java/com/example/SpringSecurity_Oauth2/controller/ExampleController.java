package com.example.SpringSecurity_Oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Примеры", description = "Примеры запросов с разными правами доступа")
public class ExampleController {

    @GetMapping
    @Operation(summary = "Доступен всем пользователям")
    @PreAuthorize("isAuthenticated()")
    public String example() {
        return "Hello, world!";
    }

    @GetMapping("/user")
    @Operation(summary = "Доступен только авторизованным пользователям")
    @PreAuthorize("isAuthenticated()")
    public String exampleUser() {
        return "Secure endpoint is working!";
    }

    @GetMapping("/admin")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }


}

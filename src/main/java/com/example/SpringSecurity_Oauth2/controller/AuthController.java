package com.example.SpringSecurity_Oauth2.controller;

import com.example.SpringSecurity_Oauth2.dto.SignInRequest;
import com.example.SpringSecurity_Oauth2.dto.SignUpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

//    @PostMapping("/signup")
//    public String registerUser(SignUpRequest signUpRequest) {
//        // Логика для регистрации пользователя
//        // Например, сохранение пользователя в базе данных
//        return "redirect:/login"; // Перенаправление на страницу входа после успешной регистрации
//    }
//
//    @PostMapping("/login")
//    public String authenticateUser(SignInRequest signInRequest) {
//        // Логика для аутентификации пользователя
//        // Например, проверка учетных данных и создание сессии
//        return "redirect:/home"; // Перенаправление на главную страницу после успешного входа
//    }
}

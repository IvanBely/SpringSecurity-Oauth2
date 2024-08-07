package com.example.SpringSecurity_Oauth2.service;

import com.example.SpringSecurity_Oauth2.model.Role;
import com.example.SpringSecurity_Oauth2.model.User;
import com.example.SpringSecurity_Oauth2.model.UserIdentity;
import com.example.SpringSecurity_Oauth2.repository.UserIdentityRepository;
import com.example.SpringSecurity_Oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final Logger logger = LoggerFactory.getLogger(OAuth2UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserIdentityRepository userIdentityRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // Получаем все атрибуты
        Map<String, Object> attributes = oauth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        logger.info("Provider: {}", registrationId);

        logger.info("Loading user attributes:");
        attributes.forEach((key, value) -> logger.info(key + ": " + value));

        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("login");
        String role = (String) attributes.get("type");
        Object idObject = attributes.get("id");
        String providerId = idObject != null ? idObject.toString() : null;

        Role userRole;
        try {
            userRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role received: {}", role);
            throw new OAuth2AuthenticationException("Invalid role: " + role);
        }
        logger.info("UserRole: {}", userRole);
        logger.info("Email: {}", email);
        logger.info("Username: {}", username);

        Optional<UserIdentity> optionalUserIdentity = userIdentityRepository.findByProviderId(providerId);
        User user = new User();
        if (optionalUserIdentity.isPresent()) {
            UserIdentity userIdentity = optionalUserIdentity.get();
            user = userIdentity.getUser();
            user.setAttributes(attributes);
            userRepository.save(user);
            logger.info("Existing user updated: {}", attributes);
        } else {
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword("");
            user.setRole(userRole);
            user.setAttributes(attributes);
            userRepository.save(user);

            UserIdentity userIdentity = new UserIdentity();
            userIdentity.setProvider(registrationId);
            userIdentity.setProviderId(providerId);
            userIdentity.setUser(user);
            userIdentityRepository.save(userIdentity);
            logger.info("New user created: {}", username);
        }

        return user;
    }
}

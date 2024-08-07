package com.example.SpringSecurity_Oauth2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_identity")
public class UserIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private String providerId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

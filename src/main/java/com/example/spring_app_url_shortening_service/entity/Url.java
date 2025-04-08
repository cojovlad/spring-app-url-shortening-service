package com.example.spring_app_url_shortening_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Url.java
@Entity
@Table(name = "urls")
@Data
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String alias;

    private LocalDateTime expirationDate;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean isActive = true;

    @ManyToMany(mappedBy = "urls", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users = new ArrayList<>();
}

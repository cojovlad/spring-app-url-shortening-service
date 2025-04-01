package com.example.spring_app_url_shortening_service.service;

import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * Handles user authentication by loading user details from the database.
 *
 * <p>This service bridges the application's {@link User} entity with Spring Security's
 * authentication framework.</p>
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new CustomUserDetailsService with the required repository.
     *
     * @param userRepositoryInjection the user repository for database operations
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepositoryInjection) {
        this.userRepository = userRepositoryInjection;
    }

    /**
     * Loads user details by username during authentication.
     *
     * @param username the username to search for
     * @return UserDetails object containing the user's credentials and authorities
     * @throws UsernameNotFoundException if the user is not found or account is disabled
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.getIsActive())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .roles(user.getRole().getRoleName().toUpperCase())
                .build();
    }
}
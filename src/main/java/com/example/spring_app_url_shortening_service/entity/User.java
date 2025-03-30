package com.example.spring_app_url_shortening_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Represents a user entity in the URL shortening application.
 * Implements Spring Security's {@link UserDetails} for authentication and authorization.
 *
 * <p>This entity maps to the "users" table in the database and includes
 * user credentials, personal information, and security-related fields.</p>
 */
@Entity
@Data
@Table(name="users")
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * User's email address. Must be unique and non-null.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Username for authentication. Must be unique and non-null.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Encrypted password for authentication. Must be non-null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * User's first name. Must be non-null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * User's last name. Must be non-null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Role assigned to the user. Establishes many-to-one relationship with Role entity.
     * Defaults to role with ID=1 if not specified.
     */
    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    /**
     * User's preferred language. Optional many-to-one relationship with Language entity.
     */
    @ManyToOne
    @JoinColumn(name="language_id")
    private Language language;

    /**
     * Flag indicating whether the user account is active.
     * Defaults to true (active) when created.
     */
    private Boolean isActive = true;

    /**
     * JPA lifecycle callback that sets default role (ID=1) if none is specified.
     */
    @PrePersist
    public void prePersist(){
        if(this.role==null){
            Role defaultRole = new Role();
            defaultRole.setId(1L);
            this.role = defaultRole;
        }
    }

    /**
     * {@inheritDoc}
     * @return empty list as authorities are managed through the Role entity
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * {@inheritDoc}
     * @return true by default (account never expires)
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * {@inheritDoc}
     * @return true by default (account never locked)
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * {@inheritDoc}
     * @return true by default (credentials never expire)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * {@inheritDoc}
     * @return true if account is active, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
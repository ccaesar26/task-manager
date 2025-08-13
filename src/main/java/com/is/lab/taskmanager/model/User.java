package com.is.lab.taskmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> ownedProjects = new HashSet<>();

    @ManyToMany(mappedBy = "collaborators")
    private Set<Project> collaboratedProjects = new HashSet<>();

    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> assignedTasks = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Pentru moment, nu avem un sistem de roluri/permisiuni.
        // Vom returna o listă goală. Toți utilizatorii sunt la fel.
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Contul nu expiră niciodată
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Contul nu este niciodată blocat
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credențialele nu expiră niciodată
    }

    @Override
    public boolean isEnabled() {
        return true; // Contul este mereu activ
    }
}

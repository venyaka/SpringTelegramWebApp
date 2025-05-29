package veniamin.backend.spring_telegram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    private Long id;

    @Column
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private String photoUrl;

    @Column(name = "date_auth")
    private LocalDateTime authDate;

    @Column
    private String hash;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_del")
    private Boolean isDeleted;


    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Set<Role> roles = new HashSet<>();



    @PrePersist
    public void prePersist() {
        this.isActive = Boolean.TRUE;
        this.isDeleted = Boolean.FALSE;
        this.dateCreate = LocalDateTime.now();
        if (roles == null) {
            roles = new HashSet<>();
        }
        if (roles.isEmpty()) {
            roles.add(Role.ROLE_USER);
        }
    }

    @PreUpdate
    public void setDateUpdate() {
        this.dateUpdate = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return hash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User otherUser = (User) obj;

        return this.getId().equals(otherUser.getId());

    }
}

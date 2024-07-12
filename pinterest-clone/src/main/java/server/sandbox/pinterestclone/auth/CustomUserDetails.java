package server.sandbox.pinterestclone.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.service.enums.UserRole;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Slf4j
public class CustomUserDetails implements UserDetails {

    private User user;

    private final Collection<GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
        authorities = new ArrayList<>();

        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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

    public boolean isAdmin() {
        return this.authorities.contains(new SimpleGrantedAuthority(UserRole.ADMIN.getRole()));
    }

    public boolean isDataOwner(String dataOwnerEmail) {
        log.info(dataOwnerEmail);
        log.info(getUsername());
        return getUsername().equals(dataOwnerEmail); }
}

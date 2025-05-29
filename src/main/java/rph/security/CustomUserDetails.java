package rph.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rph.entity.User;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // The actual User object of the currently logged-in user
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole())); // Returns a list based on the user's role
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Indicates whether the account has not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Indicates whether the account is not locked
        }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Indicates whether the credentials (password) are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Indicates whether the account is enabled
    }
}

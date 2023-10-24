package co.inventorsoft.academy.spring.security;

import co.inventorsoft.academy.spring.models.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ToString
public class JwtUser implements UserDetails {

    private String name;
    private final String email;
    private final UserRole userRole;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * JwtUser constructor for users that are present in database.
     *
     * @param email       User's email
     * @param userRole    User's role
     * @param authorities Authorities based on role to limit access to endpoints
     */
    public JwtUser(String email, UserRole userRole,
                   Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.userRole = userRole;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}

package itmo.taieta.gateway.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class UserDetailsEntity implements UserDetails {
    private final Owner owner;

    public Long getId() {
        return owner.getId();
    }

    @Override
    public String getUsername() {
        return owner.getUsername();
    }

    @Override
    public String getPassword() {
        return owner.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + owner.getRole().name()));
    }


    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

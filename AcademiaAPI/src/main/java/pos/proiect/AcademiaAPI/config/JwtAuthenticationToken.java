package pos.proiect.AcademiaAPI.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final String email;
    private final String role;
    private final Map<String, Set<String>> permissions;

    public JwtAuthenticationToken(String token, String email, String role) {
        this(token, email, role, null);
    }

    public JwtAuthenticationToken(String token, String email, String role, Map<String, Set<String>> permissions) {
        super(buildAuthorities(role));
        this.token = token;
        this.email = email;
        this.role = role;
        this.permissions = permissions;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Map<String, Set<String>> getPermissions() {
        return permissions;
    }

    private static Collection<? extends GrantedAuthority> buildAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }
}
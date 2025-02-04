package pos.proiect.AcademiaMongoAPI.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            Map<String, Set<String>> permissions = jwtAuth.getPermissions();

            System.out.println(permissions);

            if (permissions != null) {
                String uri = request.getRequestURI();
                String method = request.getMethod();

                System.out.println("Requested URI: " + uri);
                System.out.println("HTTP Method: " + method);
                System.out.println("Permissions: " + permissions);

                boolean isAllowed = permissions.entrySet().stream()
                        .anyMatch(entry -> {
                            String permissionUri = entry.getKey()
                                    .replace("**", ".*")
                                    .replace("*", "[^/]*");
                            Set<String> allowedMethods = entry.getValue();
                            System.out.println("Checking URI: " + uri + " against permission: " + permissionUri);
                            System.out.println("Allowed Methods: " + allowedMethods);
                            return uri.matches("^" + permissionUri + "$") && allowedMethods.contains(method);
                        });
                System.out.println("Is Allowed: " + isAllowed);

                if (!isAllowed) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"message\": \"Access Denied\"}");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api-docs") || uri.startsWith("/swagger-ui");
    }
}
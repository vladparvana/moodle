package pos.proiect.AcademiaAPI.permissions;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class AdminPermissions {

    public Map<String, Set<String>> getAllowedLinksAndOperations(String email) {
        Map<String, Set<String>> permissions = new HashMap<>();

        permissions.put("/api/academia/users/register", Set.of("POST"));
        permissions.put("/api/academia/users", Set.of("GET"));
        permissions.put("/api/academia/users/**", Set.of("GET", "PUT", "DELETE"));
        permissions.put("/api/academia/students",Set.of("GET", "POST", "PUT", "DELETE"));
        permissions.put("/api/academia/lectures",Set.of("GET", "POST", "PUT", "DELETE"));
        permissions.put("/api/academia/lectures/**/add-student",Set.of("GET", "POST", "PUT", "DELETE"));
        permissions.put("/api/academia/professors",Set.of("GET", "POST", "PUT", "DELETE"));
        permissions.put("/api/academia/professors/**", Set.of("GET"));


        return permissions;
    }
}
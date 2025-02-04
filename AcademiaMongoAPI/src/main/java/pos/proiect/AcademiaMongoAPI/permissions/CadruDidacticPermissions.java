package pos.proiect.AcademiaMongoAPI.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CadruDidacticPermissions {



    public Map<String, Set<String>> getAllowedLinksAndOperations(String email) {


        Map<String, Set<String>> permissions = new HashMap<>();

        permissions.put("/api/material-curs", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/material-curs/*", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/material-curs/**", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/material-laborator", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/material-laborator/*", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/material-laborator/**", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/probe-evaluare", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/probe-evaluare/*", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/probe-evaluare/**", Set.of("GET", "PUT", "POST", "DELETE"));
        permissions.put("/api/material-curs/**/continut", Set.of("GET"));
        permissions.put("/api/material-curs/**/capitol/**", Set.of("GET"));
        permissions.put("/api/material-laborator/**/continut", Set.of("GET"));
        permissions.put("/api/material-laborator/**/capitol/**", Set.of("GET"));



        return permissions;
    }
}
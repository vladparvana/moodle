package pos.proiect.AcademiaAPI.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.*;
import pos.proiect.AcademiaAPI.permissions.AdminPermissions;
import pos.proiect.AcademiaAPI.permissions.CadruDidacticPermissions;
import pos.proiect.AcademiaAPI.permissions.StudentPermissions;
import pos.proiect.auth.AuthServiceGrpc;
import pos.proiect.auth.TokenRequest;
import pos.proiect.auth.TokenResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${grpc.server.host}")
    private String grpcHost;

    @Value("${grpc.server.port}")
    private int grpcPort;

    @Value("${jwt.secret}")
    private String secret;
    private final List<String> skipUrls = List.of("/api/academia/auth/register", "/api/academia/auth/login","/swagger-ui","/v3/api-docs");

    @Autowired
    private CadruDidacticPermissions cadruDidacticPermissions;

    @Autowired
    private StudentPermissions studentPermissions;

    @Autowired
    private AdminPermissions adminPermissions;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println(request.getHeader(HttpHeaders.AUTHORIZATION));
        String uri = request.getRequestURI();

        if (skipUrls.stream().anyMatch(uri::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(),Map.of("message","Token not found!"));
            return;
        }
        String origin = request.getHeader("Origin");
        String jwt;
        String token = authorizationHeader.substring(7);
        if(origin != null && origin.equals("http://localhost:3000")){
            jwt=token;
        }
        else
        {
            jwt = JWT.decode(token).getClaim("token").asString();
        }




        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            String emailFromToken = decodedJWT.getClaim("email").asString();
            String roleFromToken = decodedJWT.getClaim("role").asString();
            ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
            AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);
            TokenRequest tokenRequest = TokenRequest.newBuilder().setToken(token).build();

            try {
                TokenResponse tokenResponse = stub.validateToken(TokenRequest.newBuilder().setToken(jwt).build());
                if(!tokenResponse.getIsValid()) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getWriter(),Map.of("message",tokenResponse.getMessage()));
                    return;
                }
            } catch (Exception e) {
                System.err.println("GRPC Error: " + e.getMessage());
            }
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<String, Set<String>> permissions ;

            if("profesor".equals(roleFromToken)) {
                permissions=cadruDidacticPermissions.getAllowedLinksAndOperations(emailFromToken);
            }
            else
                if("student".equals(roleFromToken)) {
                    permissions=studentPermissions.getAllowedLinksAndOperations(emailFromToken);
                }
                else if ("admin".equals(roleFromToken)) {
                    permissions = adminPermissions.getAllowedLinksAndOperations(emailFromToken);
                }
                else
                    {
                        permissions=null;
                    }
            System.out.println(permissions);
            Authentication authentication = new JwtAuthenticationToken(token, emailFromToken, roleFromToken,permissions);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            System.err.println("GRPC Error: " + e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(),Map.of("message","Invalid token signature"));

        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api-docs") || uri.startsWith("/swagger-ui");
    }
}
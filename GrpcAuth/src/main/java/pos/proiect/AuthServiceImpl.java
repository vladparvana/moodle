package pos.proiect;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pos.proiect.auth.*;
import pos.proiect.entity.User;
import pos.proiect.repository.UserRepository;
import pos.proiect.enums.Rol;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase{
    @Autowired
    private UserRepository userRepository;



    private final Map<String, Boolean> blacklist = new HashMap<>();
    private String secret = "secret";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String email = request.getEmail();
        String password = request.getPassword();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty())
        {
            LoginResponse response = LoginResponse.newBuilder().setToken("").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        User user = userOptional.get();

        if(!passwordEncoder.matches(password,user.getPassword())){
            LoginResponse response = LoginResponse.newBuilder().setToken("").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        String token = generateToken(user);
        LoginResponse response = LoginResponse.newBuilder().setToken(token).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void registerUser(RegisterUserRequest request, StreamObserver<RegisterUserResponse> responseObserver) {
        String email = request.getEmail();
        String password = request.getPassword();
        String role = request.getRole();
        if(userRepository.findByEmail(email).isPresent())
        {
            RegisterUserResponse response = RegisterUserResponse.newBuilder().setSuccess(false).setMessage("Username already exists").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        User user = new User(email,passwordEncoder.encode(password), Rol.valueOf(role));
        userRepository.save(user);
        RegisterUserResponse response = RegisterUserResponse.newBuilder().setSuccess(true).setMessage("User succesfully created").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void validateToken(TokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        String token = request.getToken();

        if (blacklist.containsKey(token) && blacklist.get(token)) {
            TokenResponse response = TokenResponse.newBuilder().setMessage("Token is invalid").setIsValid(false).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            TokenResponse response = TokenResponse.newBuilder().setMessage("Token is valid").setIsValid(true).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch(Exception e)
        {
            TokenResponse response = TokenResponse.newBuilder().setMessage("Token is invalid").setIsValid(false).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void invalidateToken(TokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        String token = request.getToken();
        blacklist.put(token,true);
        TokenResponse response = TokenResponse.newBuilder().setMessage("Token invalidated").setIsValid(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("http://localhost:8080")
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("role", user.getRol().toString())
                .withClaim("email",user.getEmail())
                .sign(algorithm);
    }


    @Override
    public void deleteUser(DeleteUserRequest request, StreamObserver<DeleteUserResponse> responseObserver) {
        String email = request.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            DeleteUserResponse response = DeleteUserResponse.newBuilder()
                    .setMessage("User not found")
                    .setSuccess(false)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        userRepository.delete(userOptional.get());
        DeleteUserResponse response = DeleteUserResponse.newBuilder()
                .setMessage("User successfully deleted")
                .setSuccess(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) {
        String email = request.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            GetUserResponse response = GetUserResponse.newBuilder()
                    .setFound(false)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        User user = userOptional.get();
        GetUserResponse response = GetUserResponse.newBuilder()
                .setEmail(user.getEmail())
                .setRole(user.getRol().toString())
                .setFound(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UpdateUserResponse> responseObserver) {
        String email = request.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            UpdateUserResponse response = UpdateUserResponse.newBuilder()
                    .setMessage("User not found")
                    .setSuccess(false)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(Rol.valueOf(request.getRole()));
        userRepository.save(user);

        UpdateUserResponse response = UpdateUserResponse.newBuilder()
                .setMessage("User successfully updated")
                .setSuccess(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(EmptyRequest request, StreamObserver<GetAllUsersResponse> responseObserver) {
        List<User> users = userRepository.findAll();

        List<pos.proiect.auth.User> grpcUsers = users.stream()
                .map(user -> pos.proiect.auth.User.newBuilder()
                        .setEmail(user.getEmail())
                        .setRole(user.getRol().toString())
                        .build())
                .toList();

        GetAllUsersResponse response = GetAllUsersResponse.newBuilder()
                .addAllUsers(grpcUsers)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}

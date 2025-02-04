package pos.proiect.AcademiaAPI.controller;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.proiect.auth.*;
import pos.proiect.AcademiaAPI.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/academia/users")
public class UserController {
    @Value("${grpc.server.host}")
    private String grpcHost;

    @Value("${grpc.server.port}")
    private int grpcPort;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        try {
            RegisterUserRequest request = RegisterUserRequest.newBuilder()
                    .setEmail(userDTO.getEmail())
                    .setPassword(userDTO.getPassword())
                    .setRole(userDTO.getRole())
                    .build();

            RegisterUserResponse response = stub.registerUser(request);

            if (response.getSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
            }
        } finally {
            channel.shutdown();
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        try {
            GetUserRequest request = GetUserRequest.newBuilder()
                    .setEmail(email)
                    .build();

            GetUserResponse response = stub.getUser(request);

            if (response.getFound()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(response.getEmail());
                userDTO.setRole(response.getRole());
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } finally {
            channel.shutdown();
        }
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody UserDTO userDTO) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        try {
            UpdateUserRequest request = UpdateUserRequest.newBuilder()
                    .setEmail(email)
                    .setPassword(userDTO.getPassword())
                    .setRole(userDTO.getRole())
                    .build();

            UpdateUserResponse response = stub.updateUser(request);

            if (response.getSuccess()) {
                return ResponseEntity.ok(response.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
            }
        } finally {
            channel.shutdown();
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        try {
            DeleteUserRequest request = DeleteUserRequest.newBuilder()
                    .setEmail(email)
                    .build();

            DeleteUserResponse response = stub.deleteUser(request);

            if (response.getSuccess()) {
                return ResponseEntity.ok(response.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
            }
        } finally {
            channel.shutdown();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        System.out.println("Received request for all users");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(channel);

        try {
            EmptyRequest request = EmptyRequest.newBuilder().build();

            GetAllUsersResponse response = stub.getAllUsers(request);

            List<UserDTO> users = response.getUsersList().stream()
                    .map(user -> {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setEmail(user.getEmail());
                        userDTO.setRole(user.getRole());
                        return userDTO;
                    })
                    .toList();

            return ResponseEntity.ok(users);
        } finally {
            channel.shutdown();
        }
    }

}

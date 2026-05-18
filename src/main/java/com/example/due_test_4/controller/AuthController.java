package com.example.due_test_4.controller;

import com.example.due_test_4.dto.KeycloakAuthResponse;
import com.example.due_test_4.dto.LoginUserDto;
import com.example.due_test_4.dto.RegisterUserDto;
import com.example.due_test_4.dto.UserResource;
import com.example.due_test_4.service.KeycloakAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;

    /**
     * Registers a new user in Keycloak, stores the Keycloak user ID in the local
     * database, and returns an access token for immediate use.
     *
     * POST /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<KeycloakAuthResponse> register(@RequestBody RegisterUserDto registerUserDto) {
        KeycloakAuthResponse response = keycloakAuthService.register(registerUserDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Dynamically adds a new resource entity to the database and creates a corresponding
     * user in Keycloak with the entity ID as a custom attribute.
     *
     * POST /auth/addUser
     */
    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserResource userResource) {
        String result = keycloakAuthService.addUser(userResource);
        if ("User already exists".equals(result)) {
            return ResponseEntity.status(409).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Authenticates the user against Keycloak via the ROPC grant and returns
     * an access token + refresh token.
     *
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<KeycloakAuthResponse> login(@RequestBody LoginUserDto loginUserDto) {
        KeycloakAuthResponse response = keycloakAuthService.login(loginUserDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Revokes the current session/token in Keycloak.
     *
     * POST /auth/logout
     * Header: Authorization: Bearer <token>
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        keycloakAuthService.logout(authHeader);
        return ResponseEntity.ok("Logged out successfully.");
    }

    /**
     * Assigns a Keycloak realm role to the user identified by email.
     *
     * POST /auth/assign-role
     * Body: { "email": "user@example.com", "roleName": "ADMIN" }
     */
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String roleName = body.get("roleName");
        keycloakAuthService.assignRole(email, roleName);
        return ResponseEntity.ok("Role '" + roleName + "' assigned to user '" + email + "' successfully.");
    }
}

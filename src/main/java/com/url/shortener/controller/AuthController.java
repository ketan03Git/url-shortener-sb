//Handles user registration
// 1️⃣ Accepting JSON input (via RegisterRequest).
// 2️⃣ Creating a new User object.
// 3️⃣ Assigning default role "ROLE_USER".
// 4️⃣ Calling userservice.registerUser(user) to store the user in the database.
// 5️⃣ Returning "User registered successfully".

package com.url.shortener.controller;

import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.dtos.RegisterRequest;
import com.url.shortener.models.User;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //  Indicates that this class is a Spring Boot REST controller (returns JSON responses).
@RequestMapping("/api/auth")    //Defines the base URL for all endpoints in this controller.
@AllArgsConstructor
public class AuthController {

    private UserService userservice;

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userservice.authenticateUser(loginRequest));
    }

    @PostMapping("/public/register")    //Maps HTTP POST requests to /api/auth/public/register.
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){    //@RequestBody RegisterRequest registerRequest → Takes user input (JSON) and maps it to a RegisterRequest object.
        User user = new User();
        user.setUsername(registerRequest.getUsername());    //Sets username, password, and email from the RegisterRequest DTO object
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole("ROLE_USER");
        userservice.registerUser(user); //Calls userservice.registerUser(user) to hash the password and save the user in the database.

        return ResponseEntity.ok("User registered successfully");

    }
}

package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ========== GET /api/auth/login ==========
    // Kama mtu (au browser) akifungua /api/auth/login moja kwa moja (GET)
    // badala ya kupata 405, tunamrudisha kwenye ukurasa halisi wa login.
    @GetMapping("/login")
    public ResponseEntity<Void> loginPage() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/login.html"))
                .build();
    }

    // ========== GET /api/auth/register ==========
    // Vivyo hivyo kwa register: GET inaelekeza kwenye ukurasa wa register.html.
    @GetMapping("/register")
    public ResponseEntity<Void> registerPage() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/register.html"))
                .build();
    }

    // ========== REGISTER ==========
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // Angalia kama email tayari ipo
        if (userRepository.existsByEmail(user.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        User savedUser = userRepository.save(user);

        // Usirudishe password
        savedUser.setPassword(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // ========== LOGIN ==========
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                // Login successful
                user.setPassword(null); // usirudishe password
                return ResponseEntity.ok(user);
            }
        }

        // Login failed
        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // ========== GET ALL USERS (Optional) ==========
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
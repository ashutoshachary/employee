package com.ashutosh.employeetesting.controller;


import com.ashutosh.employeetesting.model.Employee;
import com.ashutosh.employeetesting.model.GoogleTokenRequest;
import com.ashutosh.employeetesting.repository.EmployeeRepository;
import com.ashutosh.employeetesting.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {
    
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;

    public GoogleAuthController(EmployeeRepository employeeRepository, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleSignIn(@RequestBody GoogleTokenRequest tokenRequest) {
        try {
            // Add debug logging
            System.out.println("Received token: " + tokenRequest.getToken());

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), 
                new GsonFactory())
                .setAudience(Collections.singletonList(
                    "608175201687-466vui4m94sd72tt2dhph1disi16i21j.apps.googleusercontent.com"))
                .build();

            GoogleIdToken idToken = verifier.verify(tokenRequest.getToken());
            
            if (idToken == null) {
                System.out.println("Invalid token");
                return ResponseEntity.status(401).body("Invalid Google token");
            }

            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            
            // Add more debug logging
            System.out.println("Email from token: " + email);

            Optional<Employee> existingEmployee = employeeRepository.findByEmail(email);
            Employee employee;

            if (existingEmployee.isEmpty()) {
                employee = new Employee();
                employee.setEmail(email);
                employee.setEmployeeName((String) payload.get("name"));
                employee.setPassword(""); // Empty password for Google users
                employeeRepository.save(employee);
            } else {
                employee = existingEmployee.get();
            }

            String token = jwtService.generateToken(email);
            
            return ResponseEntity.ok().body(Map.of(
                "id", employee.getId(),
                "token", token,
                "email", email,
                "name", employee.getEmployeeName()
            ));
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body("Authentication failed: " + e.getMessage());
        }
    }
    
    
}
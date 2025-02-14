//package com.ashutosh.employeetesting.controller;
//
//import com.ashutosh.employeetesting.model.Employee;
//import com.ashutosh.employeetesting.repository.EmployeeRepository;
//
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/employees")
//public class EmployeeController {
//    
//    private final EmployeeRepository employeeRepository;
//
//    public EmployeeController(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//    
//    @GetMapping
//    public List<Employee> getAllEmployees() {
//        return employeeRepository.findAll();
//    }
//    
//    @PostMapping
//    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
//        return ResponseEntity.ok(employeeRepository.save(employee));
//    }
//    
//    @GetMapping("/{id}")
//    public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
//        return employeeRepository.findById(id)
//            .map(ResponseEntity::ok)
//            .orElse(ResponseEntity.notFound().build());
//    }
//    
//    @PutMapping("/{id}")
//    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {
//        return employeeRepository.findById(id)
//            .map(existingEmployee -> {
//                employee.setId(id);
//                return ResponseEntity.ok(employeeRepository.save(employee));
//            })
//            .orElse(ResponseEntity.notFound().build());
//    }
//    @GetMapping("/signin")
//    public ResponseEntity<?> signIn(@RequestParam String email, @RequestParam String password) {
//        Optional<Employee> employee = employeeRepository.findByEmailAndPassword(email, password);
//
//        if (employee.isPresent()) {
//            return ResponseEntity.ok().body("{\"id\": \"" + employee.get().getId() + "\"}");
//        } else {
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }
//    }
//
//}

package com.ashutosh.employeetesting.controller;

import com.ashutosh.employeetesting.model.Employee;
import com.ashutosh.employeetesting.repository.EmployeeRepository;
import com.ashutosh.employeetesting.security.JwtService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public EmployeeController(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        // Encrypt the password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    if (employee.getPassword() != null && !employee.getPassword().isEmpty() && !employee.getPassword().equals(existingEmployee.getPassword())) {
                        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
                    } else {
                        employee.setPassword(existingEmployee.getPassword());
                    }
                    employee.setId(id);
                    return ResponseEntity.ok(employeeRepository.save(employee));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String email, @RequestParam String password) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        
        if (employee.isPresent() && passwordEncoder.matches(password, employee.get().getPassword())) {
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok()
                    .body(Map.of(
                            "id", employee.get().getId(),
                            "token", token
                    ));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    
    @PutMapping("/{id}/updatePassword")
    public ResponseEntity<?> updatePassword(
            @PathVariable String id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        
        return employeeRepository.findById(id)
                .map(employee -> {
                    // Verify old password
                    if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
                        return ResponseEntity.status(401).body("Current password is incorrect");
                    }

                    // Encode and set new password
                    employee.setPassword(passwordEncoder.encode(newPassword));
                    employeeRepository.save(employee);
                    
                    return ResponseEntity.ok().body("Password updated successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

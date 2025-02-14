package com.ashutosh.employeetesting.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ashutosh.employeetesting.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmailAndPassword(String email, String password);
}

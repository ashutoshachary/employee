package com.ashutosh.employeetesting.repository;

import com.ashutosh.employeetesting.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TodoRepository extends MongoRepository<Todo, String> {
    List<Todo> findByEmployeeId(String employeeId);
}

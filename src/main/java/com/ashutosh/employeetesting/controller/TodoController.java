package com.ashutosh.employeetesting.controller;

import com.ashutosh.employeetesting.model.Todo;
import com.ashutosh.employeetesting.repository.TodoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees/{employeeId}/todos")
@RequiredArgsConstructor
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getEmployeeTodos(@PathVariable String employeeId) {
        return todoRepository.findByEmployeeId(employeeId);
    }
    
    @PostMapping
    public ResponseEntity<Todo> createTodo(@PathVariable String employeeId, @Valid @RequestBody Todo todo) {
        todo.setEmployeeId(employeeId);
        return ResponseEntity.ok(todoRepository.save(todo));
    }
    
    @GetMapping("/{todoId}")
    public ResponseEntity<Todo> getTodo(@PathVariable String employeeId, @PathVariable String todoId) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isPresent() && optionalTodo.get().getEmployeeId().equals(employeeId)) {
            return ResponseEntity.ok(optionalTodo.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{todoId}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable String employeeId,
            @PathVariable String todoId,
            @Valid @RequestBody Todo todo) {
        return todoRepository.findById(todoId)
            .filter(existingTodo -> existingTodo.getEmployeeId().equals(employeeId))
            .map(existingTodo -> {
                todo.setId(todoId);
                todo.setEmployeeId(employeeId);
                return ResponseEntity.ok(todoRepository.save(todo));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Object> deleteTodo(@PathVariable String employeeId, @PathVariable String todoId) {
        return todoRepository.findById(todoId)
            .filter(todo -> todo.getEmployeeId().equals(employeeId))
            .map(todo -> {
                todoRepository.delete(todo);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}

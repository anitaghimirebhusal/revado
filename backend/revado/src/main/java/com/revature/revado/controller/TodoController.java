package com.revature.revado.controller;

import com.revature.revado.dto.TodoCreateRequest;
import com.revature.revado.dto.TodoUpdateRequest;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoItem> createTodo(@RequestBody TodoCreateRequest request) {
        TodoItem created = todoService.createTodo(request.userId(), request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TodoItem>> getTodos(@RequestParam(name = "userId", required = false) Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(todoService.getTodosForUser(userId));
        }
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItem> updateTodo(@PathVariable Long id, @RequestBody TodoUpdateRequest request) {
        TodoItem updated = todoService.updateTodo(id, request.title(), request.description(), request.completed());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoItem> markTodoComplete(@PathVariable Long id) {
        TodoItem updated = todoService.setTodoCompletion(id, true);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<TodoItem> markTodoIncomplete(@PathVariable Long id) {
        TodoItem updated = todoService.setTodoCompletion(id, false);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}


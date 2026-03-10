package com.revature.revado.controller;

import com.revature.revado.dto.request.TodoRequest;
import com.revature.revado.dto.response.ApiResponse;
import com.revature.revado.dto.response.TodoResponse;
import com.revature.revado.security.UserPrincipal;
import com.revature.revado.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoManagementService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoResponse>>> getAllTodos(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) Boolean completed) {
        List<TodoResponse> todoResponses;
        if (completed != null) {
            todoResponses = todoManagementService.getTodosByCompleted(currentUser.getId(), completed);
        } else {
            todoResponses = todoManagementService.getAllTodos(currentUser.getId());
        }
        return ResponseEntity.ok(ApiResponse.success(todoResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        TodoResponse todoDetails = todoManagementService.getTodoById(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(todoDetails));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(
            @Valid @RequestBody TodoRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        TodoResponse createdTodo = todoManagementService.createTodo(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Todo created successfully", createdTodo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        TodoResponse updatedTodo = todoManagementService.updateTodo(id, request, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Todo updated successfully", updatedTodo));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<TodoResponse>> toggleTodoComplete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        TodoResponse toggledTodo = todoManagementService.toggleTodoComplete(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Todo status toggled", toggledTodo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        todoManagementService.deleteTodo(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Todo deleted successfully", null));
    }
}

package com.revature.revado.controller;

import com.revature.revado.dto.request.SubtaskRequest;
import com.revature.revado.dto.response.ApiResponse;
import com.revature.revado.dto.response.SubtaskResponse;
import com.revature.revado.security.UserPrincipal;
import com.revature.revado.service.SubtaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos/{todoId}/subtasks")
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskManagementService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubtaskResponse>>> getSubtasks(
            @PathVariable Long todoId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<SubtaskResponse> subtaskResponses = subtaskManagementService.getSubtasksByTodoId(todoId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(subtaskResponses));
    }

    @GetMapping("/{subtaskId}")
    public ResponseEntity<ApiResponse<SubtaskResponse>> getSubtask(
            @PathVariable Long todoId,
            @PathVariable Long subtaskId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        SubtaskResponse subtaskDetails = subtaskManagementService.getSubtaskById(todoId, subtaskId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(subtaskDetails));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubtaskResponse>> createSubtask(
            @PathVariable Long todoId,
            @Valid @RequestBody SubtaskRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        SubtaskResponse createdSubtask = subtaskManagementService.createSubtask(todoId, request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subtask created successfully", createdSubtask));
    }

    @PutMapping("/{subtaskId}")
    public ResponseEntity<ApiResponse<SubtaskResponse>> updateSubtask(
            @PathVariable Long todoId,
            @PathVariable Long subtaskId,
            @Valid @RequestBody SubtaskRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        SubtaskResponse updatedSubtask = subtaskManagementService.updateSubtask(todoId, subtaskId, request, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Subtask updated successfully", updatedSubtask));
    }

    @PatchMapping("/{subtaskId}/toggle")
    public ResponseEntity<ApiResponse<SubtaskResponse>> toggleSubtaskComplete(
            @PathVariable Long todoId,
            @PathVariable Long subtaskId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        SubtaskResponse toggledSubtask = subtaskManagementService.toggleSubtaskComplete(todoId, subtaskId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Subtask status toggled", toggledSubtask));
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubtask(
            @PathVariable Long todoId,
            @PathVariable Long subtaskId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        subtaskManagementService.deleteSubtask(todoId, subtaskId, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Subtask deleted successfully", null));
    }
}

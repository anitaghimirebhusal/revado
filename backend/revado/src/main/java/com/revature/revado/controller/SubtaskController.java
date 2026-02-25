package com.revature.revado.controller;

import com.revature.revado.dto.SubtaskCreateRequest;
import com.revature.revado.dto.SubtaskUpdateRequest;
import com.revature.revado.entity.SubtaskItem;
import com.revature.revado.service.SubtaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @PostMapping("/todos/{todoId}/subtasks")
    public ResponseEntity<SubtaskItem> createSubtask(@PathVariable Long todoId,
                                                     @RequestBody SubtaskCreateRequest request) {
        SubtaskItem created = subtaskService.createSubtask(todoId, request.title());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/todos/{todoId}/subtasks")
    public ResponseEntity<List<SubtaskItem>> getSubtasksForTodo(@PathVariable Long todoId) {
        return ResponseEntity.ok(subtaskService.getSubtasksForTodo(todoId));
    }

    @GetMapping("/subtasks/{id}")
    public ResponseEntity<SubtaskItem> getSubtaskById(@PathVariable Long id) {
        return ResponseEntity.ok(subtaskService.getSubtaskById(id));
    }

    @PutMapping("/subtasks/{id}")
    public ResponseEntity<SubtaskItem> updateSubtask(@PathVariable Long id,
                                                     @RequestBody SubtaskUpdateRequest request) {
        SubtaskItem updated = subtaskService.updateSubtask(id, request.title(), request.completed());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/subtasks/{id}/complete")
    public ResponseEntity<SubtaskItem> markSubtaskComplete(@PathVariable Long id) {
        SubtaskItem updated = subtaskService.setSubtaskCompletion(id, true);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/subtasks/{id}/incomplete")
    public ResponseEntity<SubtaskItem> markSubtaskIncomplete(@PathVariable Long id) {
        SubtaskItem updated = subtaskService.setSubtaskCompletion(id, false);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/subtasks/{id}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long id) {
        subtaskService.deleteSubtask(id);
        return ResponseEntity.noContent().build();
    }
}


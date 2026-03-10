package com.revature.revado.service;

import com.revature.revado.dto.request.SubtaskRequest;
import com.revature.revado.dto.response.SubtaskResponse;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.exception.UnauthorizedException;
import com.revature.revado.model.Subtask;
import com.revature.revado.model.Todo;
import com.revature.revado.repository.SubtaskRepository;
import com.revature.revado.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TodoRepository todoRepository;

    public List<SubtaskResponse> getSubtasksByTodoId(Long todoId, Long userId) {
        Todo todoEntity = getTodoAndVerifyOwnership(todoId, userId);
        return subtaskRepository.findByTodoIdOrderByCreatedAtAsc(todoEntity.getId())
                .stream()
                .map(this::mapToSubtaskResponse)
                .toList();
    }

    public SubtaskResponse getSubtaskById(Long todoId, Long subtaskId, Long userId) {
        getTodoAndVerifyOwnership(todoId, userId);
        Subtask subtaskEntity = subtaskRepository.findByIdAndTodoId(subtaskId, todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask", "id", subtaskId));
        return mapToSubtaskResponse(subtaskEntity);
    }

    @Transactional
    public SubtaskResponse createSubtask(Long todoId, SubtaskRequest request, Long userId) {
        Todo todoEntity = getTodoAndVerifyOwnership(todoId, userId);

        Subtask subtaskEntity = Subtask.builder()
                .title(request.getTitle())
                .todo(todoEntity)
                .build();

        subtaskEntity = subtaskRepository.save(subtaskEntity);
        return mapToSubtaskResponse(subtaskEntity);
    }

    @Transactional
    public SubtaskResponse updateSubtask(Long todoId, Long subtaskId, SubtaskRequest request, Long userId) {
        getTodoAndVerifyOwnership(todoId, userId);
        Subtask subtaskEntity = subtaskRepository.findByIdAndTodoId(subtaskId, todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask", "id", subtaskId));

        subtaskEntity.setTitle(request.getTitle());
        subtaskEntity = subtaskRepository.save(subtaskEntity);
        return mapToSubtaskResponse(subtaskEntity);
    }

    @Transactional
    public SubtaskResponse toggleSubtaskComplete(Long todoId, Long subtaskId, Long userId) {
        getTodoAndVerifyOwnership(todoId, userId);
        Subtask subtaskEntity = subtaskRepository.findByIdAndTodoId(subtaskId, todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask", "id", subtaskId));

        subtaskEntity.setCompleted(!subtaskEntity.isCompleted());
        subtaskEntity = subtaskRepository.save(subtaskEntity);
        return mapToSubtaskResponse(subtaskEntity);
    }

    @Transactional
    public void deleteSubtask(Long todoId, Long subtaskId, Long userId) {
        getTodoAndVerifyOwnership(todoId, userId);
        Subtask subtaskEntity = subtaskRepository.findByIdAndTodoId(subtaskId, todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask", "id", subtaskId));
        subtaskRepository.delete(subtaskEntity);
    }

    private Todo getTodoAndVerifyOwnership(Long todoId, Long userId) {
        Todo todoEntity = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));
        if (!todoEntity.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to access this todo's subtasks");
        }
        return todoEntity;
    }

    private SubtaskResponse mapToSubtaskResponse(Subtask subtaskEntity) {
        return SubtaskResponse.builder()
                .id(subtaskEntity.getId())
                .title(subtaskEntity.getTitle())
                .completed(subtaskEntity.isCompleted())
                .todoId(subtaskEntity.getTodo().getId())
                .createdAt(subtaskEntity.getCreatedAt())
                .updatedAt(subtaskEntity.getUpdatedAt())
                .build();
    }
}

package com.revature.revado.service;

import com.revature.revado.dto.request.TodoRequest;
import com.revature.revado.dto.response.SubtaskResponse;
import com.revature.revado.dto.response.TodoResponse;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.model.Subtask;
import com.revature.revado.model.Todo;
import com.revature.revado.model.User;
import com.revature.revado.repository.TodoRepository;
import com.revature.revado.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public List<TodoResponse> getAllTodos(Long userId) {
        return todoRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToTodoResponse)
                .toList();
    }

    public List<TodoResponse> getTodosByCompleted(Long userId, boolean completed) {
        return todoRepository.findByUserIdAndCompletedOrderByCreatedAtDesc(userId, completed)
                .stream()
                .map(this::mapToTodoResponse)
                .toList();
    }

    public TodoResponse getTodoById(Long todoId, Long userId) {
        Todo todoEntity = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));
        return mapToTodoResponse(todoEntity);
    }

    @Transactional
    public TodoResponse createTodo(TodoRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Todo todoEntity = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .build();

        todoEntity = todoRepository.save(todoEntity);
        return mapToTodoResponse(todoEntity);
    }

    @Transactional
    public TodoResponse updateTodo(Long todoId, TodoRequest request, Long userId) {
        Todo todoEntity = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));

        todoEntity.setTitle(request.getTitle());
        todoEntity.setDescription(request.getDescription());

        todoEntity = todoRepository.save(todoEntity);
        return mapToTodoResponse(todoEntity);
    }

    @Transactional
    public TodoResponse toggleTodoComplete(Long todoId, Long userId) {
        Todo todoEntity = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));

        todoEntity.setCompleted(!todoEntity.isCompleted());
        todoEntity = todoRepository.save(todoEntity);
        return mapToTodoResponse(todoEntity);
    }

    @Transactional
    public void deleteTodo(Long todoId, Long userId) {
        Todo todoEntity = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", todoId));
        todoRepository.delete(todoEntity);
    }

    private TodoResponse mapToTodoResponse(Todo todoEntity) {
        List<SubtaskResponse> subtaskResponses = todoEntity.getSubtasks() != null
                ? todoEntity.getSubtasks().stream().map(this::mapToSubtaskResponse).toList()
                : List.of();

        return TodoResponse.builder()
                .id(todoEntity.getId())
                .title(todoEntity.getTitle())
                .description(todoEntity.getDescription())
                .completed(todoEntity.isCompleted())
                .subtasks(subtaskResponses)
                .createdAt(todoEntity.getCreatedAt())
                .updatedAt(todoEntity.getUpdatedAt())
                .build();
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

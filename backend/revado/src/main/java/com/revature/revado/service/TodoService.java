package com.revature.revado.service;

import com.revature.revado.entity.TodoItem;
import com.revature.revado.entity.User;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.TodoItemRepository;
import com.revature.revado.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoService {

    private final TodoItemRepository todoItemRepository;
    private final UserRepository userRepository;

    public TodoService(TodoItemRepository todoItemRepository, UserRepository userRepository) {
        this.todoItemRepository = todoItemRepository;
        this.userRepository = userRepository;
    }

    public TodoItem createTodo(Long userId, String title, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        TodoItem todoItem = TodoItem.builder()
                .title(title)
                .description(description)
                .completed(false)
                .user(user)
                .build();

        return todoItemRepository.save(todoItem);
    }

    @Transactional(readOnly = true)
    public List<TodoItem> getTodosForUser(Long userId) {
        return todoItemRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<TodoItem> getAllTodos() {
        return todoItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TodoItem getTodoById(Long id) {
        return todoItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id " + id));
    }

    public TodoItem updateTodo(Long id, String title, String description, Boolean completed) {
        TodoItem existing = getTodoById(id);
        if (title != null) {
            existing.setTitle(title);
        }
        if (description != null) {
            existing.setDescription(description);
        }
        if (completed != null) {
            existing.setCompleted(completed);
        }
        return todoItemRepository.save(existing);
    }

    public TodoItem setTodoCompletion(Long id, boolean completed) {
        TodoItem existing = getTodoById(id);
        existing.setCompleted(completed);
        return todoItemRepository.save(existing);
    }

    public void deleteTodo(Long id) {
        if (!todoItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id " + id);
        }
        todoItemRepository.deleteById(id);
    }
}


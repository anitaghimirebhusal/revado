package com.revature.revado.service;

import com.revature.revado.entity.SubtaskItem;
import com.revature.revado.entity.TodoItem;
import com.revature.revado.exception.ResourceNotFoundException;
import com.revature.revado.repository.SubtaskItemRepository;
import com.revature.revado.repository.TodoItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubtaskService {

    private final SubtaskItemRepository subtaskItemRepository;
    private final TodoItemRepository todoItemRepository;

    public SubtaskService(SubtaskItemRepository subtaskItemRepository, TodoItemRepository todoItemRepository) {
        this.subtaskItemRepository = subtaskItemRepository;
        this.todoItemRepository = todoItemRepository;
    }

    public SubtaskItem createSubtask(Long todoId, String title) {
        TodoItem todo = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id " + todoId));

        SubtaskItem subtask = SubtaskItem.builder()
                .title(title)
                .completed(false)
                .todo(todo)
                .build();

        return subtaskItemRepository.save(subtask);
    }

    @Transactional(readOnly = true)
    public List<SubtaskItem> getSubtasksForTodo(Long todoId) {
        return subtaskItemRepository.findByTodoId(todoId);
    }

    @Transactional(readOnly = true)
    public SubtaskItem getSubtaskById(Long id) {
        return subtaskItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with id " + id));
    }

    public SubtaskItem updateSubtask(Long id, String title, Boolean completed) {
        SubtaskItem existing = getSubtaskById(id);
        if (title != null) {
            existing.setTitle(title);
        }
        if (completed != null) {
            existing.setCompleted(completed);
        }
        return subtaskItemRepository.save(existing);
    }

    public SubtaskItem setSubtaskCompletion(Long id, boolean completed) {
        SubtaskItem existing = getSubtaskById(id);
        existing.setCompleted(completed);
        return subtaskItemRepository.save(existing);
    }

    public void deleteSubtask(Long id) {
        if (!subtaskItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subtask not found with id " + id);
        }
        subtaskItemRepository.deleteById(id);
    }
}


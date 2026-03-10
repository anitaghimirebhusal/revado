package com.revature.revado.repository;

import com.revature.revado.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

    List<Subtask> findByTodoIdOrderByCreatedAtAsc(Long todoId);

    Optional<Subtask> findByIdAndTodoId(Long id, Long todoId);
}

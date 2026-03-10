package com.revature.revado.repository;

import com.revature.revado.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

    List<Todo> findByUserIdAndCompletedOrderByCreatedAtDesc(Long userId, boolean completed);
}

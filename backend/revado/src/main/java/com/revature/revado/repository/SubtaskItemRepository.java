package com.revature.revado.repository;

import com.revature.revado.entity.SubtaskItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskItemRepository extends JpaRepository<SubtaskItem, Long> {

    List<SubtaskItem> findByTodoId(Long todoId);
}


package com.revature.revado.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtaskResponse {

    private Long id;
    private String title;
    private boolean completed;
    private Long todoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

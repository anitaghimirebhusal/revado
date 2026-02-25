package com.revature.revado.dto;

public record TodoCreateRequest(
        Long userId,
        String title,
        String description
) {
}


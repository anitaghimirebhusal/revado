package com.revature.revado.dto;

public record TodoUpdateRequest(
        String title,
        String description,
        Boolean completed
) {
}


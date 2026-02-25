package com.revature.revado.dto;

public record SubtaskUpdateRequest(
        String title,
        Boolean completed
) {
}


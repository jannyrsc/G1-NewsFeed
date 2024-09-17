package com.example.demo.dto;

// Encapsular a resposta de login com token e userId.
public record LoginResponseDTO(String token, Long userId) {
}

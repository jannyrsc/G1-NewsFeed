package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

//encapsular os dados de registro de um novo usuário.
public record RegisterDTO(String login, String password, String nome, String fone, String email, LocalDate dataNascimento, List<Long> categoriasPreferidas) {
}

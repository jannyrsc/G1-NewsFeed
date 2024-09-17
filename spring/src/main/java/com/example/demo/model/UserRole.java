package com.example.demo.model;

//Enumeração que representa os papéis (roles) dos usuários no sistema.
public enum UserRole {
    ADMIN(1),
    USER(2);


    private int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    //Retorna o UserRole correspondente ao código fornecido.
    public static UserRole valueOf(int code) {
        for (UserRole value : UserRole.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("role não encontrado");
    }
}

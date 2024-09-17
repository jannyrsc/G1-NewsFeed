package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String fone;
    private String login;
    private String email;
    private LocalDate dataNascimento;
    private StatusUsuario status; // Ativo ou Inativo
    private String password;
    private int role;

    public Usuario(String login,String password,int role, String nome, String fone, String email, LocalDate dataNascimento){
        this.login = login;
        this.password = password;
        this.role = role;
        this.nome = nome;
        this.fone = fone;
        this.email = email;
        this.dataNascimento = dataNascimento;

    }
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "usuario_categoria",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categoriasPreferidas=new ArrayList<>();

    // Retorna o papel (role) do usuário como um enum UserRole
    public UserRole getRole() {
        return UserRole.valueOf(role);
    }

    //Define o papel (role) do usuário a partir de um enum UserRole.
    public void setRole(UserRole role) {
        if(role != null){
            this.role = role.getCode();
        }
    }
    // Retorna as autorizações do usuário com base no papel
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.getRole()==UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    // Conta não expira
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    // Conta não está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    // Credenciais não expiram
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    // Conta está habilitada
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

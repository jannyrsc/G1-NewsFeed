package com.example.demo.repository;


import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;



@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByLogin(String login);

    UserDetails findByLogin(String login);

}

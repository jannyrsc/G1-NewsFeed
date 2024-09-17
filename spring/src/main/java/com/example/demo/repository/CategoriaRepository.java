package com.example.demo.repository;

import com.example.demo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findByTipo(Categoria.Tipo tipo);

    // Atualize a assinatura do método para usar Iterable
    List<Categoria> findAllById(Iterable<Long> ids);

    Categoria findByNome(String nome);
}

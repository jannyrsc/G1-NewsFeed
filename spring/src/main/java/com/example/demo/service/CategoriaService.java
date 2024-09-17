package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria findById(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + categoriaId));
    }

    public Categoria findByTipo(Categoria.Tipo tipoCategoria) {
        return categoriaRepository.findByTipo(tipoCategoria);
    }

    // Método para retornar todas as categorias
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    // Método para buscar categorias por uma lista de IDs
    public List<Categoria> findAllByIds(Set<Long> ids) {
        return categoriaRepository.findAllById(ids);
    }
}


package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.model.Noticia;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.NoticiaRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserService userService;

    // Método para buscar todas as notícias
    public List<Noticia> getAllNoticias() {
        return noticiaRepository.findAll();
    }

    // Busca notícias filtradas pelas categorias preferidas do usuário
    public List<Noticia> getNoticias(Long userId) {
        // Consulta as categorias preferidas do usuário
        List<Categoria> categorias = userService.consultarCategoriasPreferidas(userId);

        // Verifica se o usuário tem categorias preferidas
        if (categorias.isEmpty()) {
            throw new IllegalArgumentException("Usuário não possui categorias preferidas.");
        }

        // Retorna as notícias que pertencem a essas categorias
        return noticiaRepository.findByCategoriaIn(categorias);
    }

    @Transactional
    public void associarCategorias(Long noticiaId, Long categoriaId) {
        Noticia noticia = noticiaRepository.findById(noticiaId)
                .orElseThrow(() -> new IllegalArgumentException("Notícia não encontrada: " + noticiaId));

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada: " + categoriaId));

        noticia.setCategoria(categoria);

        noticiaRepository.save(noticia);
    }

    public Noticia getNoticiaById(Long id) {
        return noticiaRepository.findById(id).orElse(null);
    }

    public List<Noticia> getNoticiasPorCategoriaPreferida(Long userId, Long categoriaId) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario != null) {
            List<Categoria> categorias = usuario.getCategoriasPreferidas();
            if (categorias.stream().anyMatch(c -> c.getId().equals(categoriaId))) {
                return noticiaRepository.findByCategoriaId(categoriaId);
            }
        }
        return List.of(); // Retorna uma lista vazia se o usuário não for encontrado ou a categoria não for preferida
    }
}

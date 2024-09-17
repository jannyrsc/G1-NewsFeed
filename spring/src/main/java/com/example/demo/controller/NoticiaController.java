package com.example.demo.controller;

import com.example.demo.model.Categoria;
import com.example.demo.model.Noticia;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.NoticiaService;
import com.example.demo.repository.NoticiaRepository;
import com.example.demo.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//gerenciamento de notícias
@RestController
@RequestMapping("/noticias")

public class NoticiaController {
    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    //  Endpoint para retornar todas as notícias.
    @GetMapping("/all")
    public ResponseEntity<List<Noticia>> getAllNoticias() {
        List<Noticia> noticias = noticiaService.getAllNoticias();
        return ResponseEntity.ok(noticias);
    }

    //Endpoint para obter uma lista de notícias associadas a um usuário específico.
    @GetMapping
    public ResponseEntity<List<Noticia>> getNoticias(@RequestParam Long userId) {
        List<Noticia> noticias = noticiaService.getNoticias(userId);
        return ResponseEntity.ok(noticias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Noticia> getNoticiaById(@PathVariable Long id) {
        Noticia noticia = noticiaService.getNoticiaById(id);
        if (noticia != null) {
            return ResponseEntity.ok(noticia);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{userId}/categoria/{categoriaId}")
    public ResponseEntity<List<Noticia>> getNoticiasPorCategoriaPreferida(
            @PathVariable Long userId,
            @PathVariable Long categoriaId) {
        List<Noticia> noticias = noticiaService.getNoticiasPorCategoriaPreferida(userId, categoriaId);
        return ResponseEntity.ok(noticias);
    }
}

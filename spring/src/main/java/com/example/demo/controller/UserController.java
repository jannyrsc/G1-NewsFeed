package com.example.demo.controller;

import com.example.demo.dto.CategoriaNomeDTO;
import com.example.demo.model.Categoria;
import com.example.demo.model.Usuario;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//gerenciamento do usuário
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoriaService categoriaService;

    // Endpoint para adicionar categorias preferidas a um usuário existente.
    @PostMapping("/{userId}/categorias")
    public ResponseEntity<Usuario> adicionarCategoriasPreferidas(
            @PathVariable Long userId,
            @RequestBody Set<Long> categoriaIds) {

        // Busca o usuário pelo ID
        Usuario usuario = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + userId));

        // Busca as categorias pelo IDs fornecidos
        List<Categoria> categorias = new ArrayList<>();
        for (Long categoriaId : categoriaIds) {
            Categoria categoria = categoriaService.findById(categoriaId);
            if (categoria != null) {
                categorias.add(categoria);
            }
        }

        // Adiciona as categorias preferidas ao usuário
        usuario.getCategoriasPreferidas().addAll(categorias);
        // usuario.setCategoriasPreferidas(categorias); estava substituindo a lista de categorias do usuário ao invés de adicionar às já existentes

        // Salva o usuário atualizado no banco de dados
        Usuario usuarioAtualizado = userService.save(usuario);

        return ResponseEntity.ok(usuarioAtualizado);
    }

    // Endpoint para buscar as categorias preferidas e notícias de um usuário.
    @GetMapping(value = "/{id}")
    public ResponseEntity<List<CategoriaNomeDTO>> findCategoriaAndNoticiesUsuario(@PathVariable Long id) {
        Usuario usuario = userService.findById(id).orElse(null);
        if (usuario != null) {
            List<Categoria> categorias = usuario.getCategoriasPreferidas();
            // Converte as categorias em DTOs para serem retornadas na resposta
            List<CategoriaNomeDTO> categoriaNomeDTOs = categorias.stream()
                    .map(c -> new CategoriaNomeDTO(c.getId(), c.getNome()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(categoriaNomeDTOs);
        }
        return ResponseEntity.notFound().build();
    }
}


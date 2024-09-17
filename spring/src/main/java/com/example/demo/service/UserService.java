package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.model.UserRole;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UsuarioRepository usuarioRepository) {
        this.userRepository = usuarioRepository;
    }

    public Optional<Usuario> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new RuntimeException("Login já cadastrado");
        }

        // Criptografar a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Definir papel padrão, se necessário
        user.setRole(UserRole.USER); // Ou qualquer lógica que você preferir

        return userRepository.save(user);
    }

    public Usuario save(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public List<Categoria> consultarCategoriasPreferidas(Long userId) {
        Usuario usuario = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return usuario.getCategoriasPreferidas();
    }

}

package com.example.demo.controller;

import com.example.demo.dto.AuthenticationDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.Categoria;
import com.example.demo.model.UserRole;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

//Controlador REST responsável pelo gerenciamento de autenticação e registro de usuários.
@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    //O método recebe dados de autenticação (login e senha), realiza a autenticação e,
    //se bem-sucedida, gera um token JWT que é retornado na resposta.
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var usuario = (Usuario) auth.getPrincipal();
        var token = tokenService.generateToken(usuario);

        // Cria a resposta com o token e o userId
        var response = new LoginResponseDTO(token, usuario.getId());

        return ResponseEntity.ok(response);
    }

    //O método recebe dados de registro (login, senha, papel, nome, telefone, email, data de nascimento),
    //verifica se o login já está em uso e, caso contrário, salva o novo usuário no repositório.

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (usuarioRepository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUser = new Usuario(data.login(), encryptedPassword, 2, data.nome(), data.fone(), data.email(), data.dataNascimento());

        // Recupera as categorias preferidas
        List<Categoria> categorias = categoriaRepository.findAllById(data.categoriasPreferidas());
        newUser.setCategoriasPreferidas(categorias);

        usuarioRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}


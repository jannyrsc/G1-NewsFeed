package com.example.demo.test;

import com.example.demo.model.Usuario;
import com.example.demo.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Commit
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCadastrarUsuario() {
        // Criar um novo usuário para teste
        Usuario user = new Usuario();
        user.setNome("Teste User");
        user.setLogin("teste");
        user.setEmail("teste@example.com");
        user.setPassword("senha123"); // Lembrando que a senha deve ser criptografada no serviço real

        // Chamar o método de cadastro do serviço
        Usuario novoUsuario = userService.cadastrarUsuario(user);
        // Verificar se o usuário foi salvo corretamente
        assertNotNull(novoUsuario.getId()); // Verifica se o ID foi gerado
        assertEquals("Teste User", novoUsuario.getNome());
        assertEquals("teste", novoUsuario.getLogin());
        assertEquals("teste@example.com", novoUsuario.getEmail());


    }
}

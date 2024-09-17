package com.example.demo.security;

import com.example.demo.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Filtro de segurança para validar tokens de autenticação JWT.
@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService; // Serviço para manipulação de tokens JWT
    @Autowired
    UsuarioRepository userRepository;
    //Método para processar a requisição e validar o token JWT.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token!=null){
            var login = tokenService.validateToken(token);// Valida o token JWT para obter o login do usuário
            UserDetails user = userRepository.findByLogin(login); // Busca os detalhes do usuário pelo login

            // Cria uma instância de autenticação com base nos detalhes do usuário
            var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            //permite que a aplicação saiba que o usuário está autenticado e tem as permisssões associadas
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //continua a cadeia de filtros, passando os parametros para o proximo
        filterChain.doFilter(request,response);
    }
    //Recupera o token JWT do cabeçalho Authorization da requisição.
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader==null) return null;
        return authHeader.replace("Bearer ","");// Remove o prefixo "Bearer " do token JWT
    }
}

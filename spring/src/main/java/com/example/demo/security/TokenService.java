package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//Serviço para geração, validação e manipulação de tokens JWT.
@Service
public class TokenService {
    //chave secreta para assinatura
    @Value("${api.security.token.secret}")
    private String secret;

    //Método para gerar um token JWT para o usuário especificado.
    public String generateToken(Usuario user){
        try{
            //algoritmo de assinatura
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token",exception);
        }
    }
    // Método para validar e extrair o login do usuário a partir de um token JWT.
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //inicia a construção de um verificador
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Invalid token",exception) ;
        }
    }
    //Método para gerar a data de expiração do token (2 horas a partir do momento atual).
    public Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

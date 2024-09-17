package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    @Column(length = 65535, columnDefinition = "text")
    private String imagem;
    @Column(length = 65535, columnDefinition = "text")
    private String descricao;
    @Column(length = 65535, columnDefinition = "text")
    private String link;
    private Date dataPublicacao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)  // Noticia tem uma única Categoria
    private Categoria categoria;


    //Sobrescreve o método equals para comparar objetos Noticia com base no ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Noticia)) return false;
        Noticia noticia = (Noticia) o;
        return Objects.equals(getId(), noticia.getId());
    }
    // Sobrescreve o método hashCode para gerar um hash baseado no ID
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
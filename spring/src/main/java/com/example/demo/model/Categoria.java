package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Enum para representar o tipo de categoria, armazenado como uma string no banco de dados
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    // Conjunto de notícias associadas a esta categoria, ignorado na serialização JSON para evitar loops infinitos
    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private Set<Noticia> noticias;

    @JsonIgnore
    @ManyToMany(mappedBy = "categoriasPreferidas")
    private Set<Usuario> usuarios;
    private String nome;


    public enum Tipo {
        CARROS("https://g1.globo.com/dynamo/carros/rss2.xml"),
        CIENCIA_E_SAUDE("https://g1.globo.com/dynamo/ciencia-e-saude/rss2.xml"),
        CONCURSOS_E_EMPREGO("https://g1.globo.com/dynamo/concursos-e-emprego/rss2.xml"),
        ECONOMIA("https://g1.globo.com/dynamo/economia/rss2.xml"),
        EDUCACAO("https://g1.globo.com/dynamo/educacao/rss2.xml"),
        LOTERIAS("https://g1.globo.com/dynamo/loterias/rss2.xml"),
        MUNDO("https://g1.globo.com/dynamo/mundo/rss2.xml"),
        MUSICA("https://g1.globo.com/dynamo/musica/rss2.xml"),
        NATUREZA("https://g1.globo.com/dynamo/natureza/rss2.xml"),
        PLANETA_BIZARRO("https://g1.globo.com/dynamo/planeta-bizarro/rss2.xml"),
        POLITICA("https://g1.globo.com/dynamo/politica/rss2.xml"),
        POP_ARTE("https://g1.globo.com/dynamo/pop-arte/rss2.xml"),
        TECNOLOGIAS_E_GAMES("https://g1.globo.com/dynamo/tecnologia/rss2.xml"),
        TURISMO_E_VIAGEM("https://g1.globo.com/dynamo/turismo-e-viagem/rss2.xml");

        private final String url;

        Tipo(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}

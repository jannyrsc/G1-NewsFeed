package com.example.demo.service;
import com.example.demo.model.Categoria;
import com.example.demo.model.Noticia;
import com.example.demo.repository.NoticiaRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;

//Serviço para consumir e salvar notícias de um feed RSS.

@Service
public class RssFeedService {
    private static final Logger logger = LoggerFactory.getLogger(RssFeedService.class);
    @Autowired
    private NoticiaRepository noticiaRepository;
    @Autowired
    private CategoriaService categoriaService;

    //Método agendado para consumir e salvar notícias de um feed RSS.
    @Scheduled(fixedRate = 3600000)
    public void consumirFeedRss() {
        for (Categoria.Tipo categoria : Categoria.Tipo.values()) {
            consumirFeedRssPorCategoria(categoria);
        }
    }
    public void consumirFeedRssPorCategoria(Categoria.Tipo categoriaTipo) {
        try {
            Categoria categoria = categoriaService.findByTipo(categoriaTipo);
            if (categoria == null) {
                logger.error("Categoria não encontrada para o Tipo: {}", categoriaTipo);
                return; // Ou outra ação conforme sua lógica de tratamento de erro
            }

            URL feedUrl = new URL(categoriaTipo.getUrl());
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            List<SyndEntry> entradas = feed.getEntries();
            for (SyndEntry entrada : entradas) {
                String titulo = entrada.getTitle();
                String link = entrada.getLink();
                String descricao = entrada.getDescription().getValue();
                String endImg = extrairUrlImagem(descricao);
                LocalDateTime dataPublicacaoLocalDateTime = entrada.getPublishedDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                Date dataPublicacao = Date.from(dataPublicacaoLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
                String descricaoLimitada = descricao != null ? descricao : "";

                // Verifica se a notícia já existe no banco de dados
                if (!noticiaRepository.existsByTitulo(titulo)) {
                    Noticia noticia = new Noticia();
                    noticia.setTitulo(titulo);
                    noticia.setLink(link);
                    noticia.setDescricao(descricaoLimitada);
                    noticia.setImagem(endImg);
                    noticia.setCategoria(categoria);
                    noticia.setDataPublicacao(dataPublicacao);

                    // Salva a notícia no banco de dados
                    noticiaRepository.save(noticia);

                    logger.info("Notícia salva: {}", noticia.getTitulo());
                } else {
                    logger.info("Notícia já existe: {}", titulo);
                }
            }
        } catch (FeedException | IOException e) {
            e.printStackTrace();
            logger.error("Erro ao consumir feed RSS", e);
        }
    }
    private String extrairUrlImagem(String descricao) {
        // Usando regex para extrair a URL da imagem dentro da tag CDATA
        Pattern pattern = Pattern.compile("<img src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(descricao);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null; // Retorna null se não encontrar a URL da imagem
    }
}

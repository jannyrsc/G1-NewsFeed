// src/components/NewsDetail.jsx
import React, { useEffect, useState } from 'react';
import axios from '../service/api'; 
import { useParams } from 'react-router-dom'; 
import '../css/NewsDetail.css'; 

function NewsDetail() {
  const [noticia, setNoticia] = useState(null); 
  const [descricaoSemImagem, setDescricaoSemImagem] = useState(''); 
  const { id } = useParams(); 

  useEffect(() => {
    const fetchNoticia = async () => {
      try {
        // Faz a requisição da notícia pelo ID
        const response = await axios.get(`/noticias/${id}`);
        const noticiaData = response.data;
        setNoticia(noticiaData);

        const descricaoSemImagem = noticiaData.descricao.replace(/<img[^>]*>/g, '');
        setDescricaoSemImagem(descricaoSemImagem);
      } catch (error) {
        console.error('Erro ao buscar detalhes da notícia:', error);
      }
    };
    
    fetchNoticia(); // Chama a função para buscar a notícia
  }, [id]); // O hook roda novamente se o 'id' mudar

  return (
    <div>
      {noticia ? ( // Verifica se a notícia foi carregada
        <div>
          <h1 className="news-detail-title">{noticia.titulo}</h1>
          <div className="news-detail-image-container">
            <img
              src={noticia.imagem} // Exibe a imagem da notícia
              className="news-detail-image"
            />
          </div>
          <div
            className="news-detail-content"
            dangerouslySetInnerHTML={{ __html: descricaoSemImagem }} 
          ></div>
          <p className="news-detail-date">
            Data de Publicação: {new Date(noticia.dataPublicacao).toLocaleDateString()} 
          </p>
        </div>
      ) : (
        <p>Carregando...</p>
      )}
    </div>
  );
}

export default NewsDetail;

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import defaultImage from '../assets/image/default-image.jpg'; 
import '../css/HomePage.css'; 

function HomePage() {
  const [news, setNews] = useState([]); 
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(''); 
  const navigate = useNavigate(); 

  // buscar as notícias quando o componente monta
  useEffect(() => {
    const fetchNews = async () => {
      setLoading(true); // Ativa o estado de carregamento
      try {
        // Faz a requisição para obter as notícias
        const response = await axios.get('http://localhost:8080/noticias/all');
        setNews(response.data); // Atualiza o estado com as notícias recebidas
      } catch (err) {
        // Define a mensagem de erro se a requisição falhar
        setError('Erro ao buscar notícias.');
      } finally {
        setLoading(false); // Desativa o estado de carregamento
      }
    };

    fetchNews();
  }, []); // O efeito roda apenas uma vez, na montagem do componente

  if (loading) return <p>Carregando...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="homepage">
      <header className="homepage-header">
        <h1>Portal de Notícias</h1>
        <button className="login-button" onClick={() => navigate('/login')}>
          Login
        </button>
      </header>
    
      <div className="news-grid">
        {news.length > 0 ? (
          news.map(newsItem => (
            <Link key={newsItem.id} to={`/news/${newsItem.id}`} className="news-link">
              <div className="news-card">
                <img
                  src={newsItem.imagem || defaultImage}
                  alt={newsItem.titulo} 
                  className="news-card-image"
                />
                <h2 className="news-card-title">{newsItem.titulo}</h2>
              </div>
            </Link>
          ))
        ) : (
          <p>Não há notícias disponíveis.</p>
        )}
      </div>
    </div>
  );
}

export default HomePage;

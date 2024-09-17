import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import api from '../service/api'; 
import defaultImage from '../assets/image/default-image.jpg'; 
import '../css/HomePage.css'; 
import '../css/FilteredNews.css'; 

const FilteredNews = () => {
    const [noticias, setNoticias] = useState([]); 
    const [categories, setCategories] = useState([]); 
    const [selectedCategory, setSelectedCategory] = useState(''); 
    const [loading, setLoading] = useState(true); 
    const [error, setError] = useState(null); 
    const navigate = useNavigate(); 

    //buscar as categorias quando o componente monta
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const userId = localStorage.getItem('userId');
                if (userId) {
                    const response = await api.get(`/users/${userId}`);
                    setCategories(response.data); // Define as categorias recebidas na resposta
                } else {
                    console.error('Usuário não autenticado');
                    setError('Não foi possível carregar as categorias.');
                }
            } catch (err) {
                console.error('Erro ao buscar categorias:', err.response ? err.response.data : err.message);
                setError('Não foi possível carregar as categorias.');
            }
        };

        fetchCategories();
    }, []); // roda apenas uma vez, ao montar o componente

    // buscar as notícias sempre que a categoria selecionada mudar
    useEffect(() => {
        const fetchNoticias = async () => {
            try {
                const userId = localStorage.getItem('userId');
                if (userId && selectedCategory) {
                    // Busca notícias de uma categoria específica
                    const response = await api.get(`/noticias/usuario/${userId}/categoria/${selectedCategory}`);
                    setNoticias(response.data);
                } else if (userId) {
                    // Busca todas as notícias para o usuário
                    const response = await api.get(`/noticias?userId=${userId}`);
                    setNoticias(response.data);
                } else {
                    console.error('Usuário não autenticado');
                    setError('Não foi possível carregar as notícias.');
                }
            } catch (err) {
                console.error('Erro ao buscar notícias:', err.response ? err.response.data : err.message);
                setError('Não foi possível carregar as notícias.');
            } finally {
                setLoading(false); // Desativa o loading quando a requisição terminar
            }
        };

        fetchNoticias();
    }, [selectedCategory]); // Esse efeito é disparado quando a categoria selecionada muda

    // Função para manipular a mudança da categoria selecionada
    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value); // Atualiza a categoria escolhida
        setLoading(true); // Ativa o estado de carregamento
    };

    // Função para realizar o logout
    const handleLogout = () => {
        localStorage.removeItem('token'); // Remove o token de autenticação
        navigate('/'); // Redireciona para a HomePage
    };

    // Função para redirecionar o usuário ao clicar em uma notícia
    const handleNewsClick = (noticiaId) => {
        navigate(`/news/${noticiaId}`); // Navega para a página de detalhes da notícia
    };

    if (loading) return <div>Carregando...</div>; // Exibe um indicador de carregamento
    if (error) return <div>{error}</div>; // Exibe uma mensagem de erro, se houver

    return (
        <div className="homepage">
            <header className="homepage-header">
                <h1>Portal de Notícias</h1>
                <div className="header-actions">
                    {/* Filtro de categoria */}
                    <div className="category-filter">
                        <select id="category" value={selectedCategory} onChange={handleCategoryChange}>
                            <option value="">TODAS</option>
                            {Array.isArray(categories) && categories.length > 0 ? (
                                categories.map(category => (
                                    <option key={category.id} value={category.id}>
                                        {category.nome}
                                    </option>
                                ))
                            ) : (
                                <option value="">Nenhuma categoria disponível</option>
                            )}
                        </select>
                    </div>
                    {/* Botão de logout */}
                    <button className="login-button" onClick={handleLogout}>
                        Sair
                    </button>
                </div>
            </header>

            {/* Exibição das notícias em um layout de grade */}
            <div className="news-grid">
                {noticias.length > 0 ? (
                    noticias.map(noticia => (
                        <div key={noticia.id} className="news-card" onClick={() => handleNewsClick(noticia.id)}>
                            {/* Exibe a imagem da notícia ou uma imagem padrão */}
                            <img
                                src={noticia.imagem || defaultImage}
                                alt={noticia.titulo}
                                className="news-card-image"
                            />
                            {/* Título da notícia */}
                            <h2 className="news-card-title">{noticia.titulo}</h2>
                        </div>
                    ))
                ) : (
                    <p>Nenhuma notícia encontrada para esta categoria.</p>
                )}
            </div>

        </div>
    );
};

export default FilteredNews;

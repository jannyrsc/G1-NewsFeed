import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; 
import axios from 'axios'; 
import '../css/Login.css'; 
import newsImage from '../assets/image/news-image.svg'; 

// Função para lidar com o login
const handleLogin = async (credentials) => {
  try {
    // Envia as credenciais para a API e espera a resposta
    const response = await axios.post('http://localhost:8080/auth/login', credentials);
    const { token, userId } = response.data; // Extrai o token e o userId da resposta

    // Armazena o token e o userId no localStorage
    localStorage.setItem('token', token); 
    localStorage.setItem('userId', userId);

    return true; // Indica sucesso no login
  } catch (error) {
    throw error; // Lança o erro para ser tratado no componente
  }
};

function Login() {
  const [login, setLogin] = useState(''); 
  const [senha, setSenha] = useState(''); 
  const [error, setError] = useState(''); 
  const [loading, setLoading] = useState(false); 
  const navigate = useNavigate(); 

  // Validação do formulário
  const validateForm = () => {
    if (!login) {
      setError('O login é obrigatório.');
      return false;
    } 
    if (!senha) {
      setError('A senha é obrigatória.');
      return false;
    }
    return true; 
  };

  // Função que lida com o envio do formulário de login
  const handleSubmit = async (event) => {
    event.preventDefault(); // Previne o comportamento padrão do submit
    setError(''); 

    if (!validateForm()) {
      return; 
    }

    setLoading(true); 
    try {
      // Tenta fazer login com as credenciais
      await handleLogin({ login, password: senha });
      navigate('/filtered-news'); // Redireciona para a página de notícias filtradas
    } catch (err) {
      // Se a resposta da API retornar status 401 (não autorizado), define a mensagem de erro
      if (err.response?.status === 401) {
        setError('Credenciais incorretas. Verifique seu login e senha.');
      } else {
        setError('Erro ao fazer login. Tente novamente mais tarde.'); // Tratamento para outros erros
      }
    } finally {
      setLoading(false); 
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="image-side">
          <img src={newsImage} alt="newsImage" /> 
        </div>
        <div className="form-side">
          <h1>Login</h1>
          <form onSubmit={handleSubmit}>
            <label>Login:</label>
            <input
              type="text"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              required
            />
            <label>Senha:</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
            <button type="submit">Login</button>
            {error && <p className="error-message">{error}</p>} 
          </form>
          <div className="footer">
            <Link to="/register">Cadastrar</Link> 
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import InputMask from 'react-input-mask';
import '../css/Register.css'; 

function Register() {
  // Estados para armazenar os valores dos campos de entrada
  const [nome, setNome] = useState('');
  const [fone, setFone] = useState('');
  const [login, setLogin] = useState('');
  const [email, setEmail] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [password, setPassword] = useState('');
  const [categorias, setCategorias] = useState([]); 
  const [selectedCategories, setSelectedCategories] = useState([]); // Categorias selecionadas
  const [errors, setErrors] = useState({}); // Estado para erros de validação
  const navigate = useNavigate(); // Hook para navegação

  // Carrega as categorias quando o componente é montado
  useEffect(() => {
    const fetchCategorias = async () => {
      try {
        const response = await axios.get('http://localhost:8080/categorias/all');
        setCategorias(response.data); // Define a lista de categorias
      } catch (error) {
        console.error('Erro ao carregar categorias', error);
      }
    };
    fetchCategorias();
  }, []);

  // Função para verificar se o login é único no backend
  const validateLoginUnique = async (login) => {
    try {
      const response = await axios.get(`http://localhost:8080/auth/check-login?login=${login}`);
      return !response.data.exists; // Retorna se o login já existe
    } catch (error) {
      console.error('Erro ao verificar login', error);
      return false;
    }
  };

  // Função para tratar o envio do formulário
  const handleSubmit = async (event) => {
    event.preventDefault();
    
    // Limpa mensagens de erro anteriores
    setErrors({});
    let newErrors = {};

    // Valida campos obrigatórios
    if (!nome) newErrors.nome = 'Nome é obrigatório.';
    if (!login) newErrors.login = 'Login é obrigatório.';
    if (!email) newErrors.email = 'Email é obrigatório.';
    if (!password) newErrors.password = 'Senha é obrigatória.';
    else if (password.length < 8) newErrors.password = 'Senha deve ter pelo menos 8 caracteres.';
    if (!dataNascimento) newErrors.dataNascimento = 'Data de Nascimento é obrigatória.';
    if (selectedCategories.length === 0) newErrors.categorias = 'Pelo menos uma categoria deve ser selecionada.';

    // Se houver erros, define os erros no estado e interrompe o envio
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    // Tenta registrar o usuário
    try {
      await axios.post('http://localhost:8080/auth/register', {
        login,
        password,
        nome,
        fone,
        email,
        dataNascimento,
        categoriasPreferidas: selectedCategories, // Envia as categorias selecionadas
      });
      navigate('/login'); // Redireciona para a página de login após o registro
    } catch (error) {
      console.error('Erro ao registrar usuário', error);
    }
  };

  return (
    <div className="register-page">
      <form className="register-form" onSubmit={handleSubmit}>
        <h1>Cadastre-se</h1>

        <label>
          Nome:
          <input 
            type="text" 
            value={nome} 
            onChange={(e) => setNome(e.target.value)} 
          />
          {errors.nome && <span className="error">{errors.nome}</span>}
        </label>

        <label>
          Telefone:
          <InputMask 
            mask="(99) 99999-9999" 
            value={fone} 
            onChange={(e) => setFone(e.target.value)}
          >
            {(inputProps) => <input {...inputProps} />}
          </InputMask>
        </label>

        <label>
          Login:
          <input 
            type="text" 
            value={login} 
            onChange={(e) => setLogin(e.target.value)} 
          />
          {errors.login && <span className="error">{errors.login}</span>}
        </label>

        <label>
          Email:
          <input 
            type="email" 
            value={email} 
            onChange={(e) => setEmail(e.target.value)} 
          />
          {errors.email && <span className="error">{errors.email}</span>}
        </label>

        <label>
          Data de Nascimento:
          <input 
            type="date" 
            value={dataNascimento} 
            onChange={(e) => setDataNascimento(e.target.value)} 
          />
          {errors.dataNascimento && <span className="error">{errors.dataNascimento}</span>}
        </label>

        <label>
          Senha:
          <input 
            type="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
          />
          {errors.password && <span className="error">{errors.password}</span>}
        </label>

        <fieldset>
          <legend>Categorias Preferidas:</legend>
          {categorias.map(categoria => (
            <label key={categoria.id}>
              <input 
                type="checkbox" 
                value={categoria.id} 
                checked={selectedCategories.includes(categoria.id)}
                onChange={(e) => {
                  const id = parseInt(e.target.value);
                  setSelectedCategories(prevSelected => 
                    prevSelected.includes(id) 
                      ? prevSelected.filter(catId => catId !== id) 
                      : [...prevSelected, id]
                  );
                }}
              />
              {categoria.nome}
            </label>
          ))}
          {errors.categorias && <span className="error">{errors.categorias}</span>}
        </fieldset>

        <button type="submit">Cadastrar</button>

        <div className="login-link">
          <p>Já tem uma conta? <a href="/login">Faça o login</a></p>
        </div>
      </form>
    </div>
  );
}

export default Register;

import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './components/HomePage'; // Tela 1
import NewsDetail from './components/NewsDetail'; // Tela 2
import Login from './components/Login'; // Tela 3
import Register from './components/Register'; // Tela 4
import FilteredNews from './components/FilteredNews'; // Nova Tela

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} /> {/* Tela 1 */}
        <Route path="/news/:id" element={<NewsDetail />} /> {/* Tela 2 */}
        <Route path="/login" element={<Login />} /> {/* Tela 3 */}
        <Route path="/register" element={<Register />} /> {/* Tela 4 */}
        <Route path="/filtered-news" element={<FilteredNews />} /> {/* Página de notícias filtradas */}
      </Routes>
    </Router>
  );
}

export default App;



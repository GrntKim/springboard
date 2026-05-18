import { Route, Routes } from 'react-router-dom';
import './App.css'
import MainPage from './pages/MainPage/MainPage';
import PostListPage from './pages/PostListPage/PostListPage';
import PostWritePage from './pages/PostWritePage/PostWritePage';
import Header from './layouts/Header/Header';
import Footer from './layouts/Footer/Footer';

function App() {
  return (
    <div className="app-container">
      <Header />
      <div className="content">
        <Routes>
          <Route path='/' element={<MainPage />} />
          <Route path='/posts' element={<PostListPage />} />
          <Route path='/write' element={<PostWritePage />} />
        </Routes>
      </div>
      <Footer />
    </div>
  )
}

export default App;
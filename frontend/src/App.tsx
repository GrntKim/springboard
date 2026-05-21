import { Route, Routes } from 'react-router-dom';
import MainPage from './pages/MainPage/MainPage';
import PostListPage from './pages/PostListPage/PostListPage';
import PostWritePage from './pages/PostWritePage/PostWritePage';
import Header from './layouts/Header/Header';
import Footer from './layouts/Footer/Footer';
import RegisterPage from './pages/RegisterPage/RegisterPage';
import UserListPage from './pages/UserListPage/UserListPage';
import PostDetailPage from './pages/PostDetailPage/PostDetailPage';
import UserDetailPage from './pages/UserDetailPage/UserDetailPage';
import './App.css';
import LoginPage from './pages/LoginPage/LoginPage';

function App() {
  return (
    <div className="app-container">
      <Header />
      <div className="content">
        <Routes>
          <Route path='/' element={<MainPage />} />
          <Route path='/posts' element={<PostListPage />} />
          <Route path='/posts/:postId' element={<PostDetailPage />} />
          <Route path='/users' element={<UserListPage />} />
          <Route path='/users/:userId' element={<UserDetailPage />} />
          <Route path='/write' element={<PostWritePage />} />
          <Route path='/register' element={<RegisterPage />} />
          <Route path='/login' element={<LoginPage />} />
        </Routes>
      </div>
      <Footer />
    </div>
  )
}

export default App;
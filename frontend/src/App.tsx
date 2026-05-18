import { Route, Routes } from 'react-router-dom';
import './App.css'
import MainPage from './pages/MainPage/MainPage';
import PostListPage from './pages/PostListPage/PostListPage';
import PostWritePage from './pages/PostWritePage/PostWritePage';

function App() {
  return (
    <div className="app-container">
      <Routes>
        <Route path='/' element={<MainPage />} />
        <Route path='/posts' element={<PostListPage />} />
        <Route path='/write' element={<PostWritePage />} />
      </Routes>
    </div>
  )
}

export default App;
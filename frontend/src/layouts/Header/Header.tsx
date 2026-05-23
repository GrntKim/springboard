import { useAuth } from '../../hooks/auth';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import "./Header.css";

export default function Header() {
    const { currentUser, isAuthChecked, logout } = useAuth();
    const navigate = useNavigate();
    return (
        <div className="header-container">
            <header className="logo-and-menus-bar">
                <div className="logo">
                    <Link to={"/"}>CMS</Link>
                </div>
                <div className="site-menu">
                    <ul className="site-links">
                        <li><NavLink to={"/"}>Main</NavLink></li>
                        <li><NavLink to={"/posts"}>Posts</NavLink></li>
                        <li><NavLink to={"/write"}>Write</NavLink></li>
                        <li><NavLink to={"/users"}>Users</NavLink></li>
                    </ul>
                </div>
            </header>
            <header className="login-status-bar">
                <ul className="site-links">
                    {!isAuthChecked && <li>Checking login...</li>}
                    {isAuthChecked && !currentUser && (
                        <>
                            <li><NavLink to={"/login"}>Log in</NavLink></li>
                            <li><NavLink to={"/register"}>Register</NavLink></li>
                        </>
                    )}

                    {isAuthChecked && currentUser && (
                        <>
                            <li>Welcome, {currentUser.nickname}</li>
                            <li>
                                <NavLink to={`/users/${currentUser.id}`}>My page</NavLink>
                            </li>
                            <li>
                                <button type="button" onClick={() => { 
                                    if (confirm("Are you sure you want to log out?")) {
                                        logout(); 
                                        navigate("/"); 
                                    }}}>
                                    Logout
                                </button>
                            </li>
                        </>
                    )}
                </ul>
            </header>
        </div>
    )
}
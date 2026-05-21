import "./Header.css";
import { Link, NavLink } from 'react-router-dom';

export default function Header() {
    return (
        <div className="header-container">
            <header className="login-status-bar">
                <ul className="site-links">
                    <li><NavLink to={"/login"}>Log in</NavLink></li>
                    <li><NavLink to={"/register"}>Register</NavLink></li>
                </ul>
            </header>
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
        </div>
    )
}
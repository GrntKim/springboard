import { Link, NavLink } from 'react-router-dom';

export default function Header() {
    return (
        <header>
            <div className="logo">
                <Link to={"/"}>CMS</Link>
            </div>

            <div className="site-menu">
                <ul className="site-links">
                    <li><NavLink to={"/posts"}>Posts</NavLink></li>
                    <li><NavLink to={"/write"}>Write</NavLink></li>
                </ul>
            </div>
        </header>
    )
}
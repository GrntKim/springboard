import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { type User, getUsers } from "../../api/users";
import "../pages.css";
import { getApiErrorMessage } from "../../api/error";

export default function UserListPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        getUsers()
            .then(setUsers)
            .catch((error) => {
                setMessage(getApiErrorMessage(error));
            });
    }, []);

    return (
        <div className="main">
            <h1 className="page-title">
                Users
            </h1>
            <div className="page-content">
                <ul>
                    {users.map((user) => (
                        <li key={user.id}><Link to={`/users/${user.id}`}>{user.displayName}</Link></li>
                    ))}
                </ul>
            </div>
            {message && <p>{message}</p>}
        </div>
    );
}
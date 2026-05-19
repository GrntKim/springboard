import axios from "axios";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { type User, getUsers } from "../../api/users";
import "../pages.css";

export default function UserListPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const users = await getUsers();
                setUsers(users);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    if (error.response?.status === 500) {
                        setMessage("Bad Request.");
                        return;
                    }
                    setMessage("Something went wrong.");
                }
            }
        }

        fetchUsers();
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
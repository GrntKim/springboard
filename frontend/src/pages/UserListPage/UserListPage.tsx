import axios from "axios";
import { useState, useEffect } from "react";
import "../pages.css";

type User = {
    id: number;
    username: string;
    displayName: string;
    createdAt: string;
}

export default function UserListPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const res = await axios.get<User[]>("/api/users");
                setUsers(res.data);
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
                Post list page
            </h1>
            <div className="page-content">
                <ul>
                    {users.map((user) => (
                        <li key={user.id}>UserId: {user.displayName}</li>
                    ))}
                </ul>
            </div>
            {message && <p>{message}</p>}
        </div>
    );
}
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import "../pages.css";

type Post = {
    id: number;
    title: string;
    content: string;
    authorId: number;
    authorName: string;
    createdAt: string;
};

type User = {
    id: number;
    username: string;
    displayName: string;
    createdAt: string;
}

export default function UserDetailPage() {
    const { userId } = useParams();
    const [user, setUser] = useState<User | null>(null);
    const [posts, setPosts] = useState<Post[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const getUser = async () => {
            try {
                const res = await axios.get<User>(`/api/users/${userId}`);
                setUser(res.data);
            } catch (error) {
                if (axios.isAxiosError(error)) {
                    if (error.response?.status === 500) {
                        setMessage("Bad Request.");
                        return;
                    }

                    if (error.response?.status === 404) {
                        setMessage("User not found.");
                        return;
                    }

                    setMessage("Something went wrong.");
                }
            }
        }

        const fetchPostsByUserId = async (userId: number) => {
            try {
                const res = await axios.get<Post[]>(`/api/users/${userId}/posts`);
                setPosts(res.data);
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
        getUser();
        fetchPostsByUserId(Number(userId));
    }, [userId]);

    if (message) {
        return <p>{message}</p>
    }

    if (!user) {
        return <p>Loading...</p>
    }

    return (
        <div className="main">
            <h1 className="page-title">
                {user.displayName}'s Page
            </h1>
            <div className="page-content">
                <div className="user-info">
                    <p>id: {user.id}</p>
                    <p>username: {user.username}</p>
                    <p>displayName: {user.displayName}</p>
                    <p>createdAt: {user.createdAt}</p>
                </div>
                <h1 className="page-title">
                    {user.displayName}'s Posts
                </h1>
                {posts.length === 0 ? (
                    <p>No posts yet..</p>
                ) : (
                    <ul className="user-post-list">
                        {posts.map((post) => (
                                <li key={post.id}>
                                    <Link to={`/posts/${post.id}`}>
                                        {post.title}
                                    </Link>
                                </li>
                        ))}
                    </ul>
                )}
            </div>
            {message && <p>{message}</p>}
        </div>
    );
}
import axios from "axios";
import { useState, useEffect } from "react";
import "../pages.css";

type Post = {
    id: number;
    title: string;
    content: string;
    authorName: string;
    createdAt: string;
};

export default function PostListPage() {
    const [posts, setPosts] = useState<Post[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const res = await axios.get<Post[]>("/api/posts");
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
        fetchPosts();
    }, []);
    return (
        <div className="main">
            <h1 className="page-title">
                Post list page
            </h1>
            <div className="page-content">
                <ul>
                    {posts.map((post) => (
                        <li key={post.id}>{post.title} - {post.authorName}</li>
                    ))}
                </ul>
            </div>
            {message && <p>{message}</p>}
        </div>
    );
}
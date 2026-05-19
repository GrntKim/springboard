import axios from "axios";
import { Link, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import "../pages.css";

type Post = {
    id: number;
    title: string;
    content: string;
    authorName: string;
    createdAt: string;
};

export default function PostDetailPage() {
    const { postId } = useParams();
    const [post, setPost] = useState<Post | null>(null);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const res = await axios.get<Post>(`/api/posts/${postId}`);
                setPost(res.data);
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
        fetchPost();
    }, [postId]);

    if (message) {
        return <p>{message}</p>
    }

    if (!post) {
        return <p>Loading...</p>
    }

    return (
        <div className="main">
            <h1 className="page-title">
                {post.title}
            </h1>
            <div className="page-content">
                <p>Author: {post.authorName}</p>
                <p>Created at: {post.createdAt}</p>
                <p>{post.content}</p>
                <Link to={"/posts"}>Go back</Link>
            </div>
        </div>
    );
}
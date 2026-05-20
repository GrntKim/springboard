import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import "../pages.css";
import { getPosts, type Post } from "../../api/posts";
import { getApiErrorMessage } from "../../api/error";

export default function PostListPage() {
    const [posts, setPosts] = useState<Post[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        getPosts()
            .then(setPosts)
            .catch((error) => {
                setMessage(getApiErrorMessage(error));
            });
    }, []);
    return (
        <div className="main">
            <h1 className="page-title">
                Posts
            </h1>
            <div className="page-content">
                {posts.length === 0 ? (
                        <p>No posts yet..</p>
                    ) : (
                        <ul className="post-list">
                            {posts.map((post) => (
                                <li key={post.id}>
                                    <p>
                                        <Link to={`/posts/${post.id}`}>{post.title} </Link> 
                                        by
                                        <Link to={`/users/${post.authorId}`}> {post.authorName}</Link>
                                    </p>
                                </li>
                            ))}
                        </ul>
                    )}
            </div>
            {message && <p>{message}</p>}
        </div>
    );
}
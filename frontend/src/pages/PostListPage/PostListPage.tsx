import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import "../pages.css";
import { getPosts, type Post } from "../../api/posts";

export default function PostListPage() {
    const [posts, setPosts] = useState<Post[]>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const posts = await getPosts();
                setPosts(posts);
            } catch {
                setMessage("Something went wrong");
            }
        };

        fetchPosts();
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
import { Link } from "react-router-dom";
import { usePosts } from "../../hooks/posts";
import { getApiErrorMessage } from "../../api/error";
import "../pages.css";

export default function PostListPage() {
    const {
        data: posts = [],
        isPending,
        isError,
        error,
    } = usePosts();

    if (isPending) {
        return (
            <div className="main">
                <h1 className="page-title">Posts</h1>
                <div className="page-content">
                    <p>Loading...</p>
                </div>
            </div>
        );
    }

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
            {isError && <p>{getApiErrorMessage(error)}</p>}
        </div>
    );
}
import { Link, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import { type Post, getPostById } from "../../api/posts";
import { type Comment, getCommentsByPostId } from "../../api/comments";
import { getApiErrorMessage } from "../../api/error";
import CommentList from "./Components/CommentList/CommentList";
import CommentForm from "./Components/CommentForm/CommentForm";
import "../pages.css";

export default function PostDetailPage() {
    const { postId } = useParams();
    const [post, setPost] = useState<Post | null>(null);
    const [comments, setComments] = useState<Comment[]>([]);
    const [message, setMessage] = useState<string>("");

    const reloadComments = (postId: number) => {
        getCommentsByPostId(postId)
            .then(setComments)
            .catch((error) => {
                setMessage(getApiErrorMessage(error));
            });
    }

    const numericPostId = Number(postId);

    useEffect(() => {
        if (!postId || Number.isNaN(numericPostId)) return;

        getPostById(numericPostId)
            .then((post) => {
                setPost(post);
            })
            .catch((error) => {
                setMessage(getApiErrorMessage(error));
            });
        
        getCommentsByPostId(numericPostId)
            .then((comments) => {
                setComments(comments);
            })
            .catch((error) => {
                setMessage(getApiErrorMessage(error));
            });
    }, [postId, numericPostId]);

    if (!postId || Number.isNaN(numericPostId)) {
        return <p>Invalid post id.</p>;
    }

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
                <p>Author: <Link to={`/users/${post.authorId}`}>{post.authorName}</Link></p>
                <p>Created at: {post.createdAt}</p>
                <p>{post.content}</p>
                <Link to={"/posts"}>Go back</Link>

                <div className="comment-section">
                    <CommentForm 
                        postId={numericPostId} 
                        onCreated={() => reloadComments(numericPostId)}
                    />
                    <CommentList comments={comments} />
                </div>
            </div>
        </div>
    );
}
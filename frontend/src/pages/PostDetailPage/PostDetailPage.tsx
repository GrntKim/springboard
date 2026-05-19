import axios from "axios";
import { Link, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import { type Post, getPostByPostId } from "../../api/posts";
import { type Comment, getCommentsByPostId } from "../../api/comments";
import CommentList from "./Components/CommentList/CommentList";
import CommentForm from "./Components/CommentForm/CommentForm";
import "../pages.css";

export default function PostDetailPage() {
    const { postId } = useParams();
    const [post, setPost] = useState<Post | null>(null);
    const [comments, setComments] = useState<Comment[]>([]);
    const [message, setMessage] = useState<string>("");

    const fetchComments = async (postId: number) => {
        try {
            const comments = await getCommentsByPostId(postId);
            setComments(comments);
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

    useEffect(() => {
        if (!postId) {
            setMessage("Invalid post id.");
            return;
        }

        const numericPostId = Number(postId);

        const fetchPost = async () => {
            try {
                const post = await getPostByPostId(numericPostId);
                setPost(post);
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
        fetchComments(numericPostId);
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
                <p>Author: <Link to={`/users/${post.authorId}`}>{post.authorName}</Link></p>
                <p>Created at: {post.createdAt}</p>
                <p>{post.content}</p>
                <Link to={"/posts"}>Go back</Link>

                <div className="comment-section">
                    <CommentForm 
                        postId={Number(postId)} 
                        onCreated={() => fetchComments(post.id)}
                    />
                    <CommentList comments={comments} />
                </div>
            </div>
        </div>
    );
}
import { Link, useParams } from "react-router-dom";
import { getApiErrorMessage } from "../../api/error";
import CommentList from "./Components/CommentList/CommentList";
import CommentForm from "./Components/CommentForm/CommentForm";
import "../pages.css";
import { usePost } from "../../hooks/posts";
import { useCommentsByPostId } from "../../hooks/comments";

export default function PostDetailPage() {
    const { postId } = useParams();
    const numericPostId = Number(postId);

    const {
        data: post,
        isPending,
        isError,
        error,
    } = usePost(numericPostId);

    const { 
        data: comments = [],
        isPending: isCommentsPending,
        isError: isCommentsError,
        error: commentsError,
    } = useCommentsByPostId(numericPostId);

    if (!postId || Number.isNaN(numericPostId)) {
        return <p>Invalid post id.</p>;
    }

    if (isError) {
        return <p>{getApiErrorMessage(error)}</p>
    }

    if (isPending) {
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
                    <CommentForm postId={numericPostId} />
                    {isCommentsPending ? (
                        <p>Loading comments...</p>
                    ) : isCommentsError ? (
                        <p>{getApiErrorMessage(commentsError)}</p>
                    ) : (
                        <CommentList comments={comments} />
                    )}
                </div>
            </div>
        </div>
    );
}
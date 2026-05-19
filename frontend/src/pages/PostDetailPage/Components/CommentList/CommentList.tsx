import { Link } from "react-router-dom";

type CommentListProps = {
    comments: Comment[];
};

type Comment = {
    id: number;
    postId: number;
    userId: number;
    authorName: string;
    content: string;
    createdAt: string
};

export default function CommentList({ comments }: CommentListProps) {
    return (
        <div className="main">
            <div className="page-content">
                <h1>Comments</h1>
                {comments.length === 0 ? (
                        <p>No comments yet..</p>
                    ) : (
                        <ul className="comment-list">
                            {comments.map((comment) => (
                                <li key={comment.id}>
                                    <p>
                                        by <Link to={`/users/${comment.userId}`}>{comment.authorName}</Link>
                                        {" · "}
                                        {comment.createdAt}
                                    </p>
                                    <p>
                                        {comment.content}
                                    </p>
                                </li>
                            ))}
                        </ul>
                    )}
            </div>
        </div>
    );
}
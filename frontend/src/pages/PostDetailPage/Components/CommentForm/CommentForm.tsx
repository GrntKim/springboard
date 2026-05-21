import { useState } from "react";
import { createComment } from "../../../../api/comments";
import { API_ERROR_MESSAGE, getApiErrorMessage, HTTP_STATUS } from "../../../../api/error";

type CommentFormProps = {
    postId: number;
    onCreated: () => void;
};

export default function CommentForm({ postId, onCreated }: CommentFormProps) {
    const [content, setContent] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        createComment({ postId, content, })
            .then(() => {
                setMessage("Comment created successfully.");
                setContent("");
                onCreated();
            })
            .catch((error) => {
                setMessage(getApiErrorMessage(error, {
                    [HTTP_STATUS.UNAUTHORIZED]: "Login required",
                    [HTTP_STATUS.NOT_FOUND]: "Post not found",
                    [HTTP_STATUS.BAD_REQUEST]: API_ERROR_MESSAGE.BAD_REQUEST,
                }));
            });
    }

    return (
        <div className="main">
            <h1 className="page-title">
                Write a comment
            </h1>

            <div className="page-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="content">Content</label>
                        <input
                            id="content"
                            name="content"
                            value={content}
                            onChange={(event) => setContent(event.target.value)}
                        />
                    </div>
                    <button type="submit">Create</button>
                </form>
                
                {message && <p>{message}</p>}
                </div>
            </div>
    )
}
import { useState } from "react";
import { createComment } from "../../../../api/comments";
import { API_ERROR_MESSAGE, getApiErrorMessage, HTTP_STATUS } from "../../../../api/error";

type CommentFormProps = {
    postId: number;
    onCreated: () => void;
};

export default function CommentForm({ postId, onCreated }: CommentFormProps) {
    const [userId, setUserId] = useState<string>("");
    const [content, setContent] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        createComment({ postId, userId: Number(userId), content, })
            .then(() => {
                setMessage("Comment created successfully.");
                setUserId("");
                setContent("");
                onCreated();
            })
            .catch((error) => {
                setMessage(getApiErrorMessage(error, {
                    [HTTP_STATUS.NOT_FOUND]: "User not found",
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
                        <label htmlFor="userId">user_id</label>
                        <input
                            id="userId"
                            name="userId"
                            value={userId}
                            onChange={(event) => setUserId(event.target.value)}
                        />
                    </div>
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
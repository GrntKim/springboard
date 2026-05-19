import axios from "axios";
import { useState } from "react";
import { createComment } from "../../../../api/comments";

type CommentFormProps = {
    postId: number;
    onCreated: () => void;
};

export default function CommentForm({ postId, onCreated }: CommentFormProps) {
    const [userId, setUserId] = useState<string>("");
    const [content, setContent] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    const handleSubmit = async(event: React.SubmitEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {
            await createComment({
                postId,
                userId: Number(userId),
                content,
            })

            setMessage("Comment created successfully.");
            setUserId("");
            setContent("");
            onCreated();
        } catch (error) {
            if (axios.isAxiosError(error)) {
                if (error.response?.status === 400) {
                    setMessage("Please check your input.");
                    return;
                }
                setMessage("Something went wrong.");
            }
        }
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
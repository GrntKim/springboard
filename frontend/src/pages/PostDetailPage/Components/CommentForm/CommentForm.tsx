import { useState } from "react";
import { getApiErrorMessage } from "../../../../api/error";
import { useCreateComment } from "../../../../hooks/comments";

type CommentFormProps = {
    postId: number;
};

export default function CommentForm({ postId }: CommentFormProps) {
    const [content, setContent] = useState<string>("");
    const [message, setMessage] = useState<string>("");
    const createCommentMutation = useCreateComment();

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        setMessage("");
        createCommentMutation.mutate(
            { postId, content },
            {
                onSuccess: () => {
                    setMessage("Comment created successfully.");
                    setContent("");
                },
                onError: (error) => {
                    setMessage(getApiErrorMessage(error));
                },
            },
        );
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
                        <textarea
                            id="content"
                            name="content"
                            value={content}
                            onChange={(event) => setContent(event.target.value)}
                        />
                    </div>
                    <button type="submit" disabled={createCommentMutation.isPending}>
                        {createCommentMutation.isPending ? "Creating..." : "Create"}
                    </button>
                </form>
                
                {message && <p>{message}</p>}
                </div>
            </div>
    )
}
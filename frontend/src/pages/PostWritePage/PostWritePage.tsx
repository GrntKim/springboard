import { useState } from "react";
import "../pages.css";
import { getApiErrorMessage } from "../../api/error";
import { useCreatePost } from "../../hooks/posts";
import { useNavigate } from "react-router-dom";

export default function PostWritePage() {
    const [title, setTitle] = useState<string>("");
    const [content, setContent] = useState<string>("");
    const [message, setMessage] = useState<string>("");
    const navigate = useNavigate();
    const createPostMutation = useCreatePost();

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        setMessage("");
        createPostMutation.mutate(
            { title, content },
            {
                onSuccess: () => {
                    navigate("/posts");
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
                Write your post
            </h1>
            <div className="page-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="title">Title</label>
                        <input
                            id="title"
                            name="title"
                            value={title}
                            onChange={(event) => setTitle(event.target.value)}
                        />
                    </div>
                    <div>
                        <label htmlFor="content">Content</label>
                        <textarea
                            id="content"
                            name="content"
                            value={content}
                            onChange={(event) => setContent(event.target.value)}
                        />
                    </div>
                    <button type="submit" disabled={createPostMutation.isPending}>
                        {createPostMutation.isPending ? "Creating..." : "Create"}
                    </button>
                </form>
                
                {message && <p>{message}</p>}
            </div>
        </div>
    );
}
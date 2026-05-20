import { useState } from "react";
import "../pages.css";
import { createPost } from "../../api/posts";
import { API_ERROR_MESSAGE, getApiErrorMessage, HTTP_STATUS } from "../../api/error";

export default function PostWritePage() {
    const [userId, setUserId] = useState<string>("");
    const [title, setTitle] = useState<string>("");
    const [content, setContent] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        createPost({ userId: Number(userId), title, content, })
            .then(() => {
                setMessage("Post created successfully.");
                setUserId("");
                setTitle("");
                setContent(""); 
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
                Write your post
            </h1>
            <div className="page-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="userId">user_id</label>
                        <input
                            id="userId"
                            name="userId"
                            type="number"
                            value={userId}
                            onChange={(event) => setUserId(event.target.value)}
                        />
                    </div>
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
    );
}
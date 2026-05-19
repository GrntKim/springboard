import axios from "axios";
import { useState } from "react";
import "../pages.css";

export default function PostWritePage() {
    const [userId, setUserId] = useState("");
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [message, setMessage] = useState("");

    async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();

        try {
            await axios.post("/api/posts", {
                userId: Number(userId),
                title,
                content,
            });

            setMessage("Post created successfully.");
            setUserId("");
            setTitle("");
            setContent("");
        } catch (error) {
            if (axios.isAxiosError(error)) {
                if (error.response?.status === 404) {
                    setMessage("User not found.");
                    return;
                }

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
                Post write page
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
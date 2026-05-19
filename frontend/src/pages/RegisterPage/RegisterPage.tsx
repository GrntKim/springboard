import axios from "axios";
import { useState } from "react";
import "../pages.css";

export default function RegisterPage() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [displayName, setDisplayName] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();

        try {
            await axios.post("/api/users", {
                username,
                password,
                displayName,
            });

            setMessage("User registered successfully.");
            setUsername("");
            setPassword("");
            setDisplayName("");
        } catch (error) {
            if (axios.isAxiosError(error)) {
                if (error.response?.status === 409) {
                    setMessage("Username already exists.");
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
                Register
            </h1>

            <div className="page-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            name="username"
                            value={username}
                            onChange={(event) => setUsername(event.target.value)}
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            name="password"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                        />
                    </div>
                    <div>
                        <label htmlFor="displayName">DisplayName</label>
                        <input
                            id="displayName"
                            name="displayName"
                            value={displayName}
                            onChange={(event) => setDisplayName(event.target.value)}
                        />
                    </div>
                    <button type="submit">Register</button>
                </form>
                
                {message && <p>{message}</p>}
                        </div>
            </div>
    )
}
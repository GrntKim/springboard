import { useState } from "react";
import "../pages.css";
import { createUser } from "../../api/users";
import { API_ERROR_MESSAGE, getApiErrorMessage, HTTP_STATUS } from "../../api/error";

export default function RegisterPage() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [displayName, setDisplayName] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        createUser({ username, password, displayName, })
            .then(() => {
                setMessage("User registered successfully.");
                setUsername("");
                setPassword("");
                setDisplayName("");
            })
            .catch((error) => { 
                setMessage(getApiErrorMessage(error, {
                    [HTTP_STATUS.BAD_REQUEST]: API_ERROR_MESSAGE.BAD_REQUEST,
                    [HTTP_STATUS.CONFLICT]: "Username already exists.",
                }));
            });
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
                            type="password"
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
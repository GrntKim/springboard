import { useState } from "react";
import "../pages.css";
import { createUser } from "../../api/users";
import { API_ERROR_MESSAGE, getApiErrorMessage, HTTP_STATUS } from "../../api/error";

export default function RegisterPage() {
    const [loginId, setLoginId] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [nickname, setNickname] = useState<string>("");
    const [message, setMessage] = useState<string>("");

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();
        createUser({ loginId, password, nickname, })
            .then(() => {
                setMessage("User registered successfully.");
                setLoginId("");
                setPassword("");
                setNickname("");
            })
            .catch((error) => { 
                setMessage(getApiErrorMessage(error, {
                    [HTTP_STATUS.BAD_REQUEST]: API_ERROR_MESSAGE.BAD_REQUEST,
                    [HTTP_STATUS.CONFLICT]: "LoginId already exists.",
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
                        <label htmlFor="loginId">LoginId</label>
                        <input
                            id="loginId"
                            name="loginId"
                            value={loginId}
                            onChange={(event) => setLoginId(event.target.value)}
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
                        <label htmlFor="nickname">Nickname</label>
                        <input
                            id="nickname"
                            name="nickname"
                            value={nickname}
                            onChange={(event) => setNickname(event.target.value)}
                        />
                    </div>
                    <button type="submit">Register</button>
                </form>
                
                {message && <p>{message}</p>}
            </div>
        </div>
    )
}
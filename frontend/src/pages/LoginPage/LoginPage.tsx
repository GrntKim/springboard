import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/auth";
import { getApiErrorMessage, HTTP_STATUS } from "../../api/error";
import "../pages.css";

export default function LoginPage() {
    const [loginId, setLoginId] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [message, setMessage] = useState<string>("");
    const { login, isLoginPending } = useAuth();
    const navigate = useNavigate();

    function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
        event.preventDefault();

        login({ loginId, password })
            .then(() => {
                navigate("/");
            })
            .catch((error) => {
                setMessage(getApiErrorMessage(error, {
                    [HTTP_STATUS.BAD_REQUEST]: "Please enter id and password.",
                    [HTTP_STATUS.UNAUTHORIZED]: "Invalid login id or password.",
                }));
            });
    }

    return (
        <div className="main">
            <h1 className="page-title">Login</h1>

            <div className="page-content">
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="loginId">Login ID</label>
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

                    <button type="submit" disabled={isLoginPending}>
                        {isLoginPending ? "Logging in..." : "Login"}
                    </button>
                </form>

                {message && <p>{message}</p>}
            </div>
        </div>
    );
}
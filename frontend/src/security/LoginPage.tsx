import {FormEvent, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/LoginPage.css"

type Props = {
    onLogin: () => void
}
export default function LoginPage(props: Props) {
    const [username, setUsername] = useState<string>("");
    const [rawPassword, setRawPassword] = useState<string>("");


    const login = () => {
        axios.get("/api/app-users/login", {
            auth: {
                username,
                password: rawPassword
            }
        })
            .then(props.onLogin)
            .catch(() => alert("Login failed"));
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        login()
    }

    return <>
        <div className={"login-form"}>
            <form className="form" onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="username">Username</label>
                    <input type="text" id="username" placeholder="username"
                           onChange={event => setUsername(event.target.value)}/>
                </div>
                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input type="password" id="password" placeholder="password"
                           onChange={event => setRawPassword(event.target.value)}/>
                </div>
                <button className="primary">Login</button>
            </form>
            <Link to={{pathname: "/register"}}>Register</Link>
        </div>
    </>
}
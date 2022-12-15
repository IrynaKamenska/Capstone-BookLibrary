import React, {FormEvent, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/LoginPage.css"
import "../Buttons.css"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons";

const eye = <FontAwesomeIcon icon={faEye}/>;

type Props = {
    fetchUsername: () => void
    fetchUser: () => void
}

export default function LoginPage(props: Props) {
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const [passwordShown, setPasswordShown] = useState(false);

    const togglePassword = () => {
        setPasswordShown(!passwordShown);
    };

    const login = () => {
        axios.get("/api/app-users/login", {
            auth: {
                username,
                password
            }
        })
            .then(props.fetchUsername)
            .then(props.fetchUser)
            .catch(() => alert("Login failed"));
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        login()
    }

    return <>
        <div className="header-lib">The&nbsp;&nbsp;Library</div>
        <div className={"login-form"}>
            <h2>Login</h2>
            <p>Dont have an account?&nbsp;
                <Link to={{pathname: "/register"}}>Register</Link></p>
            <form className="form" onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="username">Username</label>
                    <input name="username" type="text" id="username" placeholder="username"
                           onChange={event => setUsername(event.target.value)}/>
                </div>
                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input name="password" type={passwordShown ? "text" : "password"} id="password"
                           placeholder="password"
                           onChange={event => setPassword(event.target.value)}/>
                    <b onClick={togglePassword} id="eye">{eye}</b>
                </div>
                <button className="button button-login">Login</button>
            </form>
        </div>
    </>
}

import React, {FormEvent, useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/LoginPage.css"
import "../Buttons.css"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons";

const eye = <FontAwesomeIcon icon={faEye}/>;

function RegisterPage() {
    const [username, setUsername] = useState<string>("")
    const [rawPassword, setRawPassword] = useState<string>("")
    const [passwordShown, setPasswordShown] = useState(false);
    const togglePassword = () => {
        setPasswordShown(!passwordShown);
    };
    const register = () => {
        axios.post("/api/app-users/member", {username, rawPassword})
            .then(response => response.data)
            .then(() => alert("AppUser successfully created"))
            .catch(() => alert("Registration failed"))
            .finally(() => {
                setUsername("")
                setRawPassword("")
            });
    }


    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        register()
    }

    return <>
        <div className={"login-form"}>
            <h2>Register</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="username">Username</label>
                    <input name="username" type="text" id="username" placeholder="username"
                           onChange={event => setUsername(event.target.value)} required/>
                </div>
                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input name="rawPassword" type={passwordShown ? "text" : "password"} id="password"
                           placeholder="password"
                           onChange={event => setRawPassword(event.target.value)}/>
                    <b onClick={togglePassword} id="eye">{eye}</b>
                </div>
                <button className="button button-reg">Register</button>
            </form>
            <Link to="/">Back to login</Link>
        </div>
    </>;
}

export default RegisterPage;

import React, {FormEvent, useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/LoginPage.css"

function RegisterPage() {
    const [username, setUsername] = useState("");
    const [rawPassword, setRawPassword] = useState("");

    const register = () => {
        axios.post("/api/app-users/member", {username, rawPassword})
            .then(response => response.data)
            .then(() => alert("A library member successfully created"))
            .catch(() => alert("Registration failed"))
            .finally(() => {
                setUsername("");
                setRawPassword("");
            });
    }


    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        register()
    }


    return <>
        <div className={"register-form"}>
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
                <button className="secondary">Register</button>
            </form>
            <Link to="/">Back to login</Link>
        </div>
    </>;
}

export default RegisterPage;


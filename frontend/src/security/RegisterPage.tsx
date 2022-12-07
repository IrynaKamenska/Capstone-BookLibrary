import React, {FormEvent, useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/RegisterPage.css"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons";
import {AppUser} from "./model/AppUser";

const eye = <FontAwesomeIcon icon={faEye}/>;


function RegisterPage() {
    const initialData: AppUser = {
        "id": "",
        "username": "",
        "rawPassword": "",
        "role": ""
    }
    const [appUser, setAppUser] = useState(initialData)
    const [passwordShown, setPasswordShown] = useState(false);
    const togglePassword = () => {
        setPasswordShown(!passwordShown);
    };
    const register = () => {
        axios.post("/api/app-users/member", {
            username: appUser.username,
            rawPassword: appUser.rawPassword
        })
            .then(response => response.data)
            .then(() => alert("AppUser successfully created"))
            .catch(() => alert("Registration failed"))
            .finally(() => {
                setAppUser({
                    id: "",
                    username: "",
                    rawPassword: "",
                    role: ""
                })
            });
    }


    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        register()
    }

    function handleChange(event: any) {
        setAppUser({
            ...appUser,
            [event.target.name]: event.target.value
        })
    }

    return <>
        <div className={"register-form"}>
            <form className="form" onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="username">Username</label>
                    <input name="username" type="text" id="username" placeholder="username"
                           onChange={handleChange} required/>
                </div>
                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input name="rawPassword" type={passwordShown ? "text" : "password"} id="password"
                           placeholder="password"
                           onChange={handleChange}/>
                    <i onClick={togglePassword} id="eye">{eye}</i>
                </div>
                <button className="secondary">Register</button>
            </form>
            <Link to="/">Back to login</Link>
        </div>
    </>;
}

export default RegisterPage;


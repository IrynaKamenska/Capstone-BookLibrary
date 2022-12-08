import {ChangeEvent, FormEvent, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import "./css/LoginPage.css"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons";
import {AppUser} from "./model/AppUser";

const eye = <FontAwesomeIcon icon={faEye}/>;

type Props = {
    fetchUser: () => void
    fetchUsername: () => void
}
export default function LoginPage(props: Props) {
    const [passwordShown, setPasswordShown] = useState(false);

    const initialData: AppUser = {
        "id": "",
        "username": "",
        "rawPassword": "",
        "role": ""
    }
    const [appUser, setAppUser] = useState(initialData)

    const togglePassword = () => {
        setPasswordShown(!passwordShown);
    };

    const login = () => {
        axios.get("/api/app-users/login", {
            auth: {
                username: appUser.username,
                password: appUser.rawPassword
            }
        })
            .then(props.fetchUsername)
            .catch(() => alert("Login failed"));
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        login()

    }

    function handleChange(event: ChangeEvent<HTMLInputElement>) {
        setAppUser({
            ...appUser,
            [event.target.name]: event.target.value
        })
    }


    return <>
        <div className={"login-form"}>
            <form className="form" onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="username">Username</label>
                    <input name="username" type="text" id="username" placeholder="username"
                           onChange={handleChange}/>
                </div>
                <div className="input-group">
                    <label htmlFor="password">Password</label>
                    <input name="rawPassword" type={passwordShown ? "text" : "password"} id="password"
                           placeholder="password"
                           onChange={handleChange}/>
                    <b onClick={togglePassword} id="eye">{eye}</b>
                </div>
                <button className="primary">Login</button>
            </form>
            <Link to={{pathname: "/register"}}>Register</Link>
        </div>
    </>
}

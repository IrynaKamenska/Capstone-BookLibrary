import React, {useState} from 'react';
import axios from "axios";

function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const register = () => {
        axios.post("/api/app-users/librarian", {username, password})
            .then(() => alert("User as Librarian created"))
            .catch(() => alert("Registration failed"))
            .finally(() => {
                setUsername("");
                setPassword("");
            });
    }

    return (
        <div style={{border: '3px solid blue', padding: '10px'}}>
            <h1>Register</h1>
            <input type="text" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)}/>
            <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)}/>
            <button onClick={register}>Register</button>
        </div>
    );
}

export default RegisterPage;
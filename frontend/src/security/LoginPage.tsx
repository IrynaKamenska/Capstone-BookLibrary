import {useState} from "react";
import axios from "axios";

type Props = {
    onLogin: () => void
}
export default function LoginPage(props: Props) {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");


    const login = () => {
        axios.get("/api/app-users/login", {
            auth: {
                username,
                password
            }
        })
            .then(props.onLogin)
            .catch(() => alert("Login failed"));
    }


    return <>
        return <div style={{border: '3px solid blue', padding: '10px'}}>
        <h1>Login</h1>
        <input type="text" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)}/>
        <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)}/>
        <button onClick={login}>Login</button>
    </div>
    </>
}
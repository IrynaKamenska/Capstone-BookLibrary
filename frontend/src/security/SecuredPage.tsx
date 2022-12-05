import axios from "axios";

export default function SecuredPage(props: {
    onLogout: () => void,
    username: string,
}) {
    function logout() {
        axios.get("/api/app-users/logout")
            .then(props.onLogout)
    }

    return <div style={{border: "3px solid green"}}>
        <h1>Willkommen {props.username} (Sie sind eingeloggt)</h1>
        <button onClick={logout}>Logout</button>
    </div>
}
import axios from "axios";
import {AppUser} from "./model/AppUser";
import React, {Dispatch, SetStateAction} from "react";
import DeleteAccount from "./DeleteAccount";
import "./css/SecuredPage.css";


type SecuredProps = {
    fetchUsername: () => void,
    appUser: AppUser
    fetchUser: () => void
    setUsername: Dispatch<SetStateAction<string | undefined>>
}
export default function SecuredPage(props: SecuredProps) {

    function logout() {
        axios.get("/api/app-users/logout")
            .then(props.fetchUsername)
    }

    return <div className={"border"}>
        <div className={"logged-in"}>
            <h1>Logged in as {props.appUser.role} </h1>
        </div>
        <div className={"logout-delete-container"}>
            <button className={"button-logout"} onClick={logout}>Logout</button>
            <DeleteAccount appUser={props.appUser} setUsername={props.setUsername} fetchUser={props.fetchUser}/>
        </div>
    </div>


}

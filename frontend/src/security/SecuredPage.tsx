import axios from "axios";
import React, {Dispatch, SetStateAction} from "react";
import DeleteAccount from "./DeleteAccount";
import "./css/SecuredPage.css";
import {AppUserInfo} from "./model/AppUserInfo";

type SecuredProps = {
    fetchUsername: () => void,
    appUserInfo: AppUserInfo
    fetchUser: () => void
    setUsername: Dispatch<SetStateAction<string | undefined>>
}
export default function SecuredPage(props: SecuredProps) {
    function logout() {
        axios.get("/api/app-users/logout")
            .then(props.fetchUsername)
    }

    return <div className={"border"}>
        <div className={"logged-in"}>Logged in as {props.appUserInfo.role}</div>
        <div className={"logout-delete-container"}>
            <button className={"button-logout"} onClick={logout}>Logout</button>
            <DeleteAccount appUserInfo={props.appUserInfo} setUsername={props.setUsername} fetchUser={props.fetchUser}/>
        </div>
    </div>

}

import axios from "axios";
import React, {Dispatch, SetStateAction, useCallback, useEffect, useState} from "react";
import DeleteAccount from "./DeleteAccount";
import "./css/SecuredPage.css";
import "../Buttons.css";
import {AppUserInfo} from "./AppUserInfo";

type SecuredProps = {
    fetchUsername: () => void,
    setUsername: Dispatch<SetStateAction<string | undefined>>
}

export default function SecuredPage(props: SecuredProps) {
    const initialData: AppUserInfo = {
        "username": "",
        "role": ""
    }
    const [appUserInfo, setAppUserInfo] = useState<AppUserInfo>(initialData);

    const fetchUser = useCallback(() => {
        axios.get("/api/app-users/user")
            .then(response => response.data)
            .then(setAppUserInfo)
    }, [])
    useEffect(fetchUser, [fetchUser])

    function logout() {
        axios.get("/api/app-users/logout")
            .then(props.fetchUsername)
    }

    return <div className={"top-bar"}>
        <div className="top-bar-lib">The&nbsp;&nbsp;Library</div>
        <div className={"logged-in"}>Hello {appUserInfo.username}! (you are {appUserInfo.role})</div>
        <div className="top-bar-spacer"></div>
        <div className={"logout-delete-container"}>
            <button className={"button button-logout"} onClick={logout}>Logout</button>
            <DeleteAccount appUserInfo={appUserInfo} setUsername={props.setUsername} fetchUser={fetchUser}/>
        </div>
    </div>

}

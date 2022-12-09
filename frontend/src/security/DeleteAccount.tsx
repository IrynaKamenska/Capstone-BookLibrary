import React, {Dispatch, SetStateAction, useCallback, useState} from 'react';
import {AppUserInfo} from "./AppUserInfo";
import axios from "axios";
import Modal from "react-modal";
import "./css/DeleteAccount.css";


type DeleteAccountProps = {
    appUserInfo: AppUserInfo,
    setUsername: Dispatch<SetStateAction<string | undefined>>
    fetchUser: () => void
}

function DeleteAccount(props: DeleteAccountProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)

    const deleteUser = () => {
        axios.delete("/api/app-users/deleteMe")
            .then(() => alert("User " + props.appUserInfo.username + " will be deleted!"))
            .catch(error => console.error("DELETE Error: " + error))
            .then(() => props.setUsername("anonymousUser"))
    }

    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const closeModalCallback = useCallback(closeModal, [closeModal])

    return <>
        <button className="button-delete-account" type={"submit"} onClick={openModal}>Delete Account</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Delete account</span>
            <div className="modal-body">
                <h5>Are you sure to delete your account?</h5>
            </div>
            <button className="button-delete" id="del-alert"
                    onClick={() => deleteUser()}>Delete
            </button>
            <button className="button-cancel" onClick={() => closeModal()}>Cancel</button>
        </Modal>

    </>;
}

export default DeleteAccount;

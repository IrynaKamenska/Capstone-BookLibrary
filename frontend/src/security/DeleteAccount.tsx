import React, {Dispatch, SetStateAction, useCallback, useState} from 'react';
import {AppUserInfo} from "./AppUserInfo";
import axios from "axios";
import Modal from "react-modal";
import "../Buttons.css"
import "../Modals.css"

type DeleteAccountProps = {
    appUserInfo: AppUserInfo,
    setUsername: Dispatch<SetStateAction<string | undefined>>
    fetchUser: () => void
}

function DeleteAccount(props: DeleteAccountProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const closeModalCallback = useCallback(closeModal, [closeModal])

    const deleteUser = () => {
        axios.delete("/api/app-users/deleteMe")
            .then(() => alert("User " + props.appUserInfo.username + " will be deleted!"))
            .catch(error => console.error("DELETE Error: " + error))
            .then(() => props.setUsername("anonymousUser"))
    }

    return <>
        <button className="button button-delete-account" type={"submit"} onClick={openModal}>Delete Account</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
               overlayClassName={"modal-overlay"}
        >
            <span className="modal-heading">Delete account</span>
            <div className="modal-body">
                <h5>Are you sure to delete your account?</h5>
                <button className="button button-delete" id="del-alert"
                        onClick={() => deleteUser()}>Delete
                </button>
                <button className="button button-cancel" onClick={() => closeModal()}>Cancel</button>
            </div>
        </Modal>
    </>;
}

export default DeleteAccount;

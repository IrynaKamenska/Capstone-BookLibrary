import React, {Dispatch, SetStateAction, useState} from 'react';
import {AppUser} from "./model/AppUser";
import axios from "axios";
import Modal from "react-modal";
import "./css/DeleteAccount.css";


type DeleteAccountProps = {
    appUser: AppUser,
    setUsername: Dispatch<SetStateAction<string | undefined>>
    fetchUser: () => void
}

function DeleteAccount(props: DeleteAccountProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)


    const deleteUser = (id: string) => {
        console.log("USER ID" + id)
        axios.delete("/api/app-users/" + id)
            .then(props.fetchUser)
            .then(() => alert("User with role " + props.appUser.role + "were deleted!"))
            .catch(error => console.error("DELETE Error: " + error))
            .then(() => props.setUsername("anonymousUser"))
    }

    const openModal = () => {
        setModalIsOpen(true)
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }

    return <>
        <button className="button-delete-account" type={"submit"} onClick={openModal}>Delete Account</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Delete account</span>
            <div className="modal-body">
                <h5>Are you sure to delete your account?</h5>
            </div>
            <button className="button-delete" id="del-alert"
                    onClick={() => deleteUser(props.appUser.id)}>Delete
            </button>
            <button className="button-cancel" onClick={() => closeModal()}>Cancel</button>
        </Modal>


    </>;
}

export default DeleteAccount;
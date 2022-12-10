import React, {ChangeEvent, FormEvent, useCallback, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import Modal from "react-modal";

type RentBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function RentBook(props: RentBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const [rentedBy, setRentedBy] = useState<string>("")


    const [names, setNames] = useState<string[]>([]);
    const getUsernames = () => {
        axios.get("/api/app-users/getAllUsernames")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(data => setNames(data))
    }
    useEffect(() => {
        getUsernames()
    }, [])

    function getSelect() {
        return <select onChange={getUsernames}>
            {names.map(name => {
                return (
                    <option value={name}>{name}</option>
                )
            })}
        </select>;
    }


    const rentBook = useCallback((id: string, username: string) => {
        axios.post("/api/books/rentBook/" + id + "/" + username)
            .then(props.reloadAllBooks)
            .then(() => alert("Rent book with id " + props.book.id + "on user " + username + " successful!"))
            .catch(error => console.error("POST Error: " + error))
    }, [props.book.id, props.reloadAllBooks])


    const handleRentBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        rentBook(props.book.id, rentedBy);

    }
    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const closeModalCallback = useCallback(closeModal, [closeModal])

    const handleRentChange = (event: ChangeEvent<HTMLSelectElement>) => {
        getUsernames()
        setRentedBy(event.target.value)
    }
    return <>
        <button className="db-list-button db-list-button-delete" type={"submit"} onClick={openModal}>Rent</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Rent book</span>
            <form onSubmit={handleRentBook}>
                <br/>
                <label htmlFor="rentBy">RentBy:</label>

                <select className="selector" value={props.book.rentedBy} name="rentedBy" id="rentedBy"
                        onChange={handleRentChange}>
                    {names.map(name => {
                        return (
                            <option value={name}>{name}</option>
                        )
                    })}
                </select>
                <br/><br/>
                <div className="modal-body">
                    <p>Are you sure to update this book?</p>
                </div>
                <div>
                    <button className="modal-button modal-button-update">Update</button>
                    <button className="modal-button modal-button-cancel" onClick={() => closeModal()}>Cancel</button>
                </div>
            </form>
        </Modal>

    </>;
}

export default RentBook;
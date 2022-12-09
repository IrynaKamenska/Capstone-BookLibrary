import React, {FormEvent} from "react";
import Modal from 'react-modal';
import axios from "axios";
import {Availability} from "./Availability";
import "./css/CreateBook.css";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadAllBooks: () => void,

}
export default function CreateBook(props: ModalProps) {
    const [newBook, setNewBook] = React.useState(
        {
            title: "",
            author: "",
            isbn: "",
            availability: Availability.AVAILABLE
        }
    );


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        axios.post("/api/books", newBook)
            .catch((e) => console.log("POST Error: " + e))
            .then(props.reloadAllBooks)
            .then(props.closeModal)
        setNewBook({
            title: "",
            author: "",
            isbn: "",
            availability: Availability.AVAILABLE
        });
    }


    function handleChange(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name]: event.target.value
        })
    }

    return (
        <Modal className="modal"
               isOpen={props.modalIsOpen}
               onRequestClose={props.closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >
            <span className="modal-heading">Add book manually</span>
            <form onSubmit={addNewBook}>
                <label htmlFor="title">Title:</label>
                <input className="input-text" type="text"
                       id="title"
                       name="title"
                       value={newBook.title}
                       onChange={handleChange}
                       placeholder="title"/>
                <br/>
                <label htmlFor="author">Author:</label>
                <input className="input-text" type="text"
                       id="author"
                       name="author"
                       value={newBook.author}
                       onChange={handleChange}
                       placeholder="author"/>
                <br/>
                <label htmlFor="isbn">ISBN:</label>
                <input className="input-text" type="text"
                       id="isbn"
                       name="isbn"
                       value={newBook.isbn}
                       onChange={handleChange}
                       placeholder="isbn"/>
                <br/>
                <label htmlFor="availability">Availability:</label>
                <select className="selector" name="availability" id="availability">
                    <option value={Availability.AVAILABLE}>AVAILABLE</option>
                    <option value={Availability.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/><br/>
                <button className="modal-button modal-button-create">Create</button>
            </form>
            <button className="modal-button modal-button-cancel" onClick={() => props.closeModal()}>Cancel</button>
        </Modal>
    );

}

import React, {FormEvent} from "react";
import Modal from 'react-modal';
import axios from "axios";
import {Availability} from "./api/model/Availability";
import "./css/BookOverview.css";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadAllBooks: () => void,

}
export default function CreateBook(props: ModalProps) {
    const [newBook, setNewBook] = React.useState(
        {
            cover: "",
            title: "",
            author: "",
            isbn: "",
            availability: Availability.AVAILABLE
        }
    );


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if (!newBook.title || !newBook.author || !newBook.isbn || !newBook.availability) {
            alert(`Please fill book title, author, isbn and state`)
            return
        }

        axios.post("/api/books", newBook)
            .catch((e) => console.log("POST Error: " + e))
            .then(props.reloadAllBooks)
            .then(props.closeModal)
        setNewBook({
            cover: "",
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
            <form onSubmit={addNewBook}>
                <br/>
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
                <select name="availability" id="availability">
                    <option value={Availability.AVAILABLE}>AVAILABLE</option>
                    <option value={Availability.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/><br/>
                <button>Create</button>
            </form>
            <button onClick={() => props.closeModal()}>Close</button>
        </Modal>
    );

}

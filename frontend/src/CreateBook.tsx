import React, {FormEvent} from "react";
import Modal from 'react-modal';
import axios from "axios";
import {BookState} from "./BookState";
import "./css/BookOverview.css";

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
            bookState: BookState.AVAILABLE
        }
    );


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if (!newBook.title || !newBook.author || !newBook.isbn || !newBook.bookState) {
            alert(`Please fill book title, author, isbn and state`)
            return
        }

        axios.post("/api/books", newBook)
            .catch((e) => console.log("POST Error: " + e))
            .then(props.reloadAllBooks)
            .then(props.closeModal)
        setNewBook({
            title: "",
            author: "",
            isbn: "",
            bookState: BookState.AVAILABLE
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
                <label>
                    New title:
                    <input className="input-text" type="text"
                           id="title"
                           name="title"
                           value={newBook.title}
                           onChange={handleChange}
                           placeholder="title"
                    />
                </label>
                <br/>
                <label>
                    New Author:
                    <input className="input-text" type="text"
                           id="author"
                           name="author"
                           value={newBook.author}
                           onChange={handleChange}
                           placeholder="author"
                    />
                </label>
                <br/>
                <label>
                    New ISBN:
                    <input className="input-text" type="text"
                           id="isbn"
                           name="isbn"
                           value={newBook.isbn}
                           onChange={handleChange}
                           placeholder="isbn"
                    />
                </label>
                <br/>
                <label htmlFor="bookState">New Book State:</label>
                <select name="bookState" id="bookState">
                    <option value={BookState.AVAILABLE}>AVAILABLE</option>
                    <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/><br/>
                <button>Create</button>
            </form>
            <button onClick={() => props.closeModal()}>Close</button>
        </Modal>
    );

}

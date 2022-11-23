
import React, {FormEvent} from "react";
import Modal from 'react-modal';
import axios from "axios";
import {BookState} from "./BookState";

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadAllBooks: () => void,

}
export default function CreateBookModal(props: ModalProps) {
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
        <Modal
            isOpen={props.modalIsOpen}
            onRequestClose={props.closeModal}
            contentLabel="Example Modal"
            ariaHideApp={false}
        >
        <button onClick={() => props.closeModal()}>Close</button>
        <form onSubmit={addNewBook}>
            <br/>
            <label>
                Book title:
                <input type="text"
                       id="title"
                       name="title"
                       value={newBook.title}
                       onChange={handleChange}
                       placeholder="title"
                />
            </label>
            <br/>
            <label>
                Book author:
                <input type="text"
                       id="author"
                       name="author"
                       value={newBook.author}
                       onChange={handleChange}
                       placeholder="author"
                />
            </label>
            <br/>
            <label>
                Book isbn:
                <input type="isbn"
                       id="isbn"
                       name="isbn"
                       value={newBook.isbn}
                       onChange={handleChange}
                       placeholder="isbn"
                />
            </label>
            <br/>
            <label htmlFor="bookState">Book State:</label>
            <select name="bookState" id="bookState">
                <option value={BookState.AVAILABLE}>AVAILABLE</option>
                <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
            </select>
            <br/><br/>
            <button>add new book</button>
        </form>
        </Modal>
    );
}
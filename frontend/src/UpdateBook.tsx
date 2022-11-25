import React, {FormEvent, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import {BookState} from "./BookState";
import Modal from "react-modal";
import "./css/BookOverview.css";

type UpdateBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function UpdateBook(props: UpdateBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const [updatedBook, setUpdatedBook] = useState(
        {
            ...props.book
        }
    );

    const handleEditBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        console.log("Updated Book: " + updatedBook)
        axios.put("/api/books/" + props.book.id, updatedBook)
            .then(response => {
                closeModal()
                return response.data
            })
            .catch(error => console.log("error =>" + error))
            .then(props.reloadAllBooks)
    }

    const openModal = () => {
        setModalIsOpen(true)
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }

    function handleUpdateChange(event: any) {
        setUpdatedBook({
            ...updatedBook,
            [event.target.name]: event.target.value
        })
    }


    return <>
        <button className="button-left" type={"submit"} onClick={openModal}>Edit</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <form onSubmit={handleEditBook}>
                <br/>
                <label>
                    New title:
                    <input type="text"
                           id="title"
                           name="title"
                           value={updatedBook.title}
                           onChange={handleUpdateChange}
                           placeholder="title"
                    />
                </label>
                <br/>
                <label>
                    New Author:
                    <input type="text"
                           id="author"
                           name="author"
                           value={updatedBook.author}
                           onChange={handleUpdateChange}
                           placeholder="author"
                    />
                </label>
                <br/>
                <label>
                    New ISBN:
                    <input type="text"
                           id="isbn"
                           name="isbn"
                           value={updatedBook.isbn}
                           onChange={handleUpdateChange}
                           placeholder="isbn"
                    />
                </label>
                <br/>
                <label htmlFor="bookState">New Book State:</label>
                <select value={updatedBook.bookState} name="bookState" id="bookState" onChange={handleUpdateChange}>
                    <option value={BookState.AVAILABLE}>AVAILABLE</option>
                    <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/><br/>
                <div className="modal-body">
                    <p>Are you sure to update this book?</p>
                </div>
                <button className="button-left">Update</button>
            </form>
            <button className="button-right" onClick={() => closeModal()}>Close</button>
        </Modal>
    </>;
}

export default UpdateBook;

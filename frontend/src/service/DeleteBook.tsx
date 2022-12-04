import React, {useState} from 'react';
import {BookModel} from "../model/BookModel";
import axios from "axios";
import Modal from "react-modal";
import "../css/DeleteBook.css";

type DeleteBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function DeleteBook(props: DeleteBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const deleteBook = (id: string) => {
        axios.delete("/api/books/" + id)
            .then(props.reloadAllBooks)
            .then(() => alert("Remove book with id " + props.book.id + " successful!"))
            .catch(error => console.error("DELETE Error: " + error))
    }
    const openModal = () => {
        setModalIsOpen(true)
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }

    return <>
        <button className="db-list-button db-list-button-delete" type={"submit"} onClick={openModal}>Delete</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Delete book</span>
            <div className="modal-body">
                <h5>Are you sure to delete this book?</h5>
            </div>
            <button className="modal-button modal-button-delete" id="del-alert" onClick={() => deleteBook(props.book.id)}>Delete</button>
            <button className="modal-button modal-button-cancel" onClick={() => closeModal()}>Cancel</button>
        </Modal>


    </>;
}

export default DeleteBook;

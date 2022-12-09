import React, {useCallback, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import Modal from "react-modal";
import "./css/DeleteBook.css";

type DeleteBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function DeleteBook(props: DeleteBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)

    const deleteBook = useCallback((id: string) => {
        axios.delete("/api/books/" + id)
            .then(props.reloadAllBooks)
            .then(() => alert("Remove book with id " + props.book.id + " successful!"))
            .catch(error => console.error("DELETE Error: " + error))
    }, [props.book.id, props.reloadAllBooks])


    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const closeModalCallback = useCallback(closeModal, [closeModal])

    const handleCancelClick = useCallback(() => closeModal(), [closeModal]);
    const handleDeleteClick = useCallback(() => deleteBook(props.book.id), [deleteBook, props.book.id]);


    return <>
        <button className="db-list-button db-list-button-delete" type={"submit"} onClick={openModal}>Delete</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Delete book</span>
            <div className="modal-body">
                <h5>Are you sure to delete this book?</h5>
            </div>
            <button className="modal-button modal-button-delete" id="del-alert" onClick={handleDeleteClick}>Delete
            </button>
            <button className="modal-button modal-button-cancel" onClick={handleCancelClick}>Cancel</button>
        </Modal>

    </>;
}

export default DeleteBook;

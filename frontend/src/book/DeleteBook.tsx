import React, {useCallback, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import Modal from "react-modal";
import "../Buttons.css";
import "../Modals.css";

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
        <button className="button button-delete-book" type={"submit"} onClick={openModal}>Delete</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
               overlayClassName={"modal-overlay"}
        >

            <span className="modal-heading">Delete book</span>
            <div className="modal-body">
                <img className="modal-cover" src={props.book.cover} alt="cover"/><br />
                <span className="modal-book-info">
                    {props.book.title}<br />
                    from <br />
                    {props.book.author}<br />
                    {props.book.isbn.map(current => {
                        return (
                            <>
                                <p key={current.identifier}>{current.type}: {current.identifier}</p>
                            </>

                        )
                    })
                    }
                </span>
                <h5>Are you sure to delete this book?</h5>
                <button className="button button-delete" id="del-alert" onClick={handleDeleteClick}>Delete
                </button>
                <button className="button button-cancel" onClick={handleCancelClick}>Cancel</button>
            </div>
        </Modal>

    </>;
}

export default DeleteBook;

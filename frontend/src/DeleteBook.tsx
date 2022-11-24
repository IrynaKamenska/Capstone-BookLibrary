import React, {useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import Modal from "react-modal";

type DeleteBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function DeleteBook(props: DeleteBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const deleteBook = (id: string) => {
        axios.delete("/api/books/" + id)
            .then(props.reloadAllBooks)
            .then(() => alert("Remove book with id " + props.book.id + "successful!"))
            .catch(error => console.error("DELETE Error: " + error))
    }
    const openModal = () => {
        setModalIsOpen(true)
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }

    return <>
        <button type={"submit"} onClick={openModal}>Delete</button>
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={closeModal}
            contentLabel="Example Modal"
            ariaHideApp={false}
        >

            <div className="modal-body">
                <h5>Are you sure to delete this book?</h5>
            </div>
            <button id="del-alert" className="button-right" onClick={() => deleteBook(props.book.id)}>Delete</button>
            <button onClick={() => closeModal()}>Close</button>
        </Modal>


    </>;
}

export default DeleteBook;

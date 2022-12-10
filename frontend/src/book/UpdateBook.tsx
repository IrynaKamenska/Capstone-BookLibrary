import React, {FormEvent, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import {Availability} from "./Availability";
import Modal from "react-modal";
import "./css/UpdateBook.css";

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
        <button className="db-list-button db-list-button-edit" type={"submit"} onClick={openModal}>Edit</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Update book manually</span>
            <form onSubmit={handleEditBook}>
                <br/>
                <label htmlFor="title">New title:</label>
                <input className="input-text"
                       type="text"
                       id="title"
                       name="title"
                       value={updatedBook.title}
                       onChange={handleUpdateChange}
                       placeholder="title"/>
                <br/>
                <label htmlFor="author">New Author:</label>
                <input className="input-text"
                       type="text"
                       id="author"
                       name="author"
                       value={updatedBook.author}
                       onChange={handleUpdateChange}
                       placeholder="author"/>
                <br/>
                <label htmlFor="isbn">New ISBN:</label>
                <input className="input-text"
                       type="text"
                       id="isbn"
                       name="isbn"
                       value={updatedBook.isbn}
                       onChange={handleUpdateChange}
                       placeholder="isbn"/>
                <br/>
                <label htmlFor="availability">Availability</label>
                <select className="selector" value={updatedBook.availability} name="availability" id="availability"
                        onChange={handleUpdateChange}>
                    <option value={Availability.AVAILABLE}>AVAILABLE</option>
                    <option value={Availability.NOT_AVAILABLE}>NOT_AVAILABLE</option>
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

export default UpdateBook;
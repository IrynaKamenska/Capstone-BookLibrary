import React, {FormEvent, useCallback, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import {Availability} from "./Availability";
import Modal from "react-modal";
import "../Buttons.css";
import "../Modals.css";

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
        axios.put("/api/books/" + props.book.id, updatedBook)
            .then(response => {
                closeModal()
                return response.data
            })
            .catch(error => console.log("error =>" + error))
            .then(props.reloadAllBooks)
    }

    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])

    function handleUpdateChange(event: any) {
        setUpdatedBook({
            ...updatedBook,
            [event.target.name]: event.target.value
        })
    }

    function handleUpdateChangeIsbn(event: any) {
        const {value, name} = event.target
        setUpdatedBook({
            ...updatedBook,
            isbn: updatedBook.isbn.map((current) => {
                if (current.type === name) {
                    return {
                        type: current.type,
                        identifier: value
                    }
                } else {
                    return current
                }
            })
        })
    }

    return <>
        <button className="button button-edit-book" type={"submit"} onClick={openModal}>Edit</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
               overlayClassName={"modal-overlay"}
        >

            <span className="modal-heading">Update book manually</span>
            <form onSubmit={handleEditBook}>
                <div className="modal-body">
                    <img className="modal-cover" src={props.book.cover} alt="cover"/><br/>
                </div>
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
                <label htmlFor="isbn">New ISBN_10:</label>
                <input className="input-text"
                       type="text"
                       id="isbn"
                       name="ISBN_10"
                       value={updatedBook.isbn[0] ? updatedBook.isbn[0].identifier : ""}

                       onChange={handleUpdateChangeIsbn}
                       placeholder="isbn"/>
                <br/>
                <label htmlFor="isbn">New ISBN_13:</label>
                <input className="input-text"
                       type="text"
                       id="isbn"
                       name="ISBN_13"
                       value={updatedBook.isbn[1] ? updatedBook.isbn[1].identifier : ""}
                       onChange={handleUpdateChangeIsbn}
                       placeholder="isbn"/>
                <br/>
                <label htmlFor="title">New category:</label>
                <input className="input-text"
                       type="text"
                       id="category"
                       name="category"
                       value={updatedBook.category}
                       onChange={handleUpdateChange}
                       placeholder="category"/>
                <br/>
                <label htmlFor="title">New printType:</label>
                <input className="input-text"
                       type="text"
                       id="printType"
                       name="printType"
                       value={updatedBook.printType}
                       onChange={handleUpdateChange}
                       placeholder="printType"/>
                <br/>
                <label htmlFor="title">New pageCount:</label>
                <input className="input-text"
                       type="text"
                       id="pageCount"
                       name="pageCount"
                       value={updatedBook.pageCount}
                       onChange={handleUpdateChange}
                       placeholder="pageCount"/>
                <br/>
                <label htmlFor="availability">Availability</label>
                <select className="selector" value={updatedBook.availability} name="availability" id="availability"
                        onChange={handleUpdateChange}>
                    <option value={Availability.AVAILABLE}>AVAILABLE</option>
                    <option value={Availability.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/><br/>
                <div className="modal-body">
                    <h5>Are you sure to update this book?</h5>
                    <button className="button button-update">Update</button>
                    <button className="button button-cancel" onClick={() => closeModal()}>Cancel</button>
                </div>
            </form>
        </Modal>
    </>;
}

export default UpdateBook;

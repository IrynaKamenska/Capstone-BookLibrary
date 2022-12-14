import React, {useCallback, useState} from 'react';
import axios from "axios";
import {BookModel} from "./BookModel";
import Modal from "react-modal";
import "../Buttons.css";
import "../Modals.css";

type ReturnBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;
}

function ReturnBook(props: ReturnBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const closeModalCallback = useCallback(closeModal, [closeModal])

    const returnBook = useCallback((id: string) => {
        axios.post("/api/books/returnBook/" + id)
            .then(response => {
                closeModal()
                return response.data
            })
            .then(props.reloadAllBooks)
            .catch(error => console.error("POST Error: " + error))

    }, [closeModal, props.reloadAllBooks])


    const handleCancelClick = useCallback(() => closeModal(), [closeModal]);
    const handleReturnClick = useCallback(() => returnBook(props.book.id), [returnBook, props.book.id]);


    return <>
        <button className="button button-return-book" type={"submit"} onClick={openModal}>Return</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Return</span>

            <div className="modal-body">
                <img className="modal-cover" src={props.book.cover} alt="cover"/><br/>
                <h3 className="book-title">{props.book.title}</h3>
                <p className="book-info">{props.book.author}</p>
                <p className="book-info">{props.book.isbn.map(current => {
                    return (
                        <>
                            <p key={current.identifier}>{current.type}: {current.identifier}</p>
                        </>

                    )
                })
                }</p>
                <p className="book-info">RentedBy: {props.book.rentBookInfo.rentByUsername}</p>
                {props.book.rentBookInfo.rentUntil.toString() !== "" &&
                    <p className="book-info">RentedUntil: {props.book.rentBookInfo.rentUntil.toString()}</p>
                }
            </div>
            <div className="modal-body">
                <button className="button button-return-book" id="rent-alert" onClick={handleReturnClick}>Return
                </button>
                <button className="button button-cancel" onClick={handleCancelClick}>Cancel</button>
            </div>
        </Modal>

    </>;
}

export default ReturnBook;
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

    const [modalReturnBookIsOpen, setModalReturnBookIsOpen] = useState<boolean>(false)
    const openReturnBookModal = useCallback(() => {
        setModalReturnBookIsOpen(true)
    }, [])

    const closeReturnBookModal = useCallback(() => {
        setModalReturnBookIsOpen(false)
    }, [])
    const closeModalCallback = useCallback(closeReturnBookModal, [closeReturnBookModal])

    const returnBook = useCallback((id: string) => {
        axios.post("/api/books/returnBook/" + id)
            .then(response => {
                closeReturnBookModal()
                return response.data
            })
            .then(props.reloadAllBooks)
            .catch(error => console.error("POST Error: " + error))

    }, [closeReturnBookModal, props.reloadAllBooks])


    const handleCancelClick = useCallback(() => closeReturnBookModal(), [closeReturnBookModal]);
    const handleReturnClick = useCallback(() => returnBook(props.book.id), [returnBook, props.book.id]);


    function getIsbnForReturnBook() {
        return <>
            {props.book.isbn.map(current => {
                return (
                    <>
                        <p key={current.identifier}>{current.type}: {current.identifier}</p>
                    </>

                )
            })
            }
        </>;
    }

    return <>
        <button className="button button-return-book" type={"submit"} onClick={openReturnBookModal}>Return</button>
        <Modal className="modal"
               isOpen={modalReturnBookIsOpen}
               onRequestClose={closeModalCallback}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >

            <span className="modal-heading">Return</span>

            <div className="modal-body">
                <img className="modal-cover" src={props.book.cover} alt="cover"/><br/>
                <h3 className="book-title">{props.book.title}</h3>
                <p className="book-info">{props.book.author}</p>
                <p className="book-info">{getIsbnForReturnBook()}</p>
                <p className="book-info">Category: {props.book.category}</p>
                <p className="book-info">PrintType: {props.book.printType}</p>
                <p className="book-info">PageCount: {props.book.pageCount}</p>
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
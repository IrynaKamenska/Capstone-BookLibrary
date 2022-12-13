import React, {ChangeEvent, FormEvent, useCallback, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import Modal from "react-modal";
import DatePicker from "react-datepicker";
import "../Buttons.css";
import "../Modals.css";
import "react-datepicker/dist/react-datepicker.css";
import {RentBookInfo} from "./RentBookInfo";

type RentBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;
}

function RentBook(props: RentBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])
    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const handleCancelClick = useCallback(() => closeModal(), [closeModal]);


    const [names, setNames] = useState<string[]>([]);
    const fetchUsernames = useCallback(() => {
        axios.get("/api/app-users/getAllUsernames")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setNames)
    }, [])
    useEffect(fetchUsernames, [fetchUsernames])

    function rentBookBy() {
        return <>
            {names.map(name => {
                return (
                    <option key={name} value={name}>{name}</option>
                )
            })}
        </>;
    }

    const [rentBookInfo, setRentBookInfo] = useState<RentBookInfo>({
        rentByUsername: "",
        rentUntil: new Date()
    })

    const handleRentBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        console.log("RENT-BOOK-OBJECT", rentBookInfo)
        axios.post("/api/books/rentBook/" + props.book.id, rentBookInfo)
            .catch(error => console.error("POST Error: " + error))
            .then(props.reloadAllBooks)
            .then(closeModal)
    }


    const handleRentChangeUsername = (event: ChangeEvent<HTMLSelectElement>) => {
        console.log("RENT-Object", rentBookInfo)
        console.log("RENT-Username", event.target.value)
        setRentBookInfo({
            ...rentBookInfo, "rentByUsername": event.target.value
        })
    }

    const handleRentChangeDate = (date: Date) => {
        setRentBookInfo({
            ...rentBookInfo, "rentUntil": date
        })
    }

    return <>
        <button className="button button-rent-book" type={"submit"} onClick={openModal}>Rent</button>
        <Modal className="modal"
               isOpen={modalIsOpen}
               onRequestClose={closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
        >
            <span className="modal-heading">Rent book</span>
            <form onSubmit={handleRentBook}>
                <div className="modal-body">
                    <img className="modal-cover" src={props.book.cover} alt="cover"/><br/>
                    <h3 className="book-title">{props.book.title}</h3>
                    <p className="book-info">{props.book.author}</p>
                    <p className="book-info">ISBN:{props.book.isbn}</p>
                </div>
                <br/>
                <label htmlFor="rentBy">RentBy:</label>

                <select className="selector" value={rentBookInfo.rentByUsername} name="rentByUsername" id="rentByUsername"
                        onChange={handleRentChangeUsername}>{rentBookBy()}</select>
                <p>Rent until:</p>
                <DatePicker selected={rentBookInfo.rentUntil} onChange={handleRentChangeDate} showTimeSelect
                            dateFormat="Pp"/>
                <br/><br/>
                <div className="modal-body">
                    <button className="button button-rent-book">Rent</button>
                    <button className="button button-cancel" onClick={handleCancelClick}>Cancel</button>
                </div>
            </form>
        </Modal>

    </>;
}

export default RentBook;
import React, {ChangeEvent, useCallback, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import Modal from "react-modal";
import DatePicker from "react-datepicker";
import "../Buttons.css";
import "../Modals.css";
import "react-datepicker/dist/react-datepicker.css";

type RentBookProps = {
    book: BookModel;
    reloadAllBooks: () => void;
}

function RentBook(props: RentBookProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const [rentedBy, setRentedBy] = useState<string>("")
    const [names, setNames] = useState<string[]>([]);
    const [date, setDate] = useState<Date>(new Date())


    const handleChangeDate = useCallback(() => setDate(new Date()), []);
    const handleDateSelect = useCallback(() => props.reloadAllBooks(), [props]);

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

    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])
    const handleCancelClick = useCallback(() => closeModal(), [closeModal]);

    const [rentInfo, setRentInfo] = useState(
        {
            ...props.book.rentBookInfo
        }
    );

    const rentBook = useCallback((id: string) => {
            axios.post("/api/books/rentBook/" + id, {rentInfo})
                .then(response => {
                    closeModal()
                    return response.data
                })
                .catch(error => console.error("POST Error: " + error))
                .then(props.reloadAllBooks)
            // setRentedBy("")
            setRentInfo({
                rentByUsername: "",
                rentUntil: ""
            })
        },
        [props.reloadAllBooks, closeModal])


    const handleRentBook = (event: any) => {
        event.preventDefault()
        console.log("handleRentBook: " + names)
        rentBook(props.book.id);

    }

    const handleRentChange = (event: ChangeEvent<HTMLSelectElement>) => {
        setRentedBy(event.target.value)
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
                    <p className="book-info">RENTED by: {rentInfo.rentByUsername}</p>
                    <p className="book-info">RENTED by: {rentInfo.rentUntil}</p>
                </div>
                <br/>
                <label htmlFor="rentBy">RentBy:</label>

                <select className="selector" value={props.book.rentBookInfo.rentByUsername} name="rentedBy" id="rentedBy"
                        onChange={handleRentChange}>
                    {rentBookBy()}
                </select>
                <p>Rent until:</p>
                <DatePicker selected={date} onChange={handleChangeDate} showTimeSelect dateFormat="Pp"
                            onCalendarClose={handleDateSelect}/>
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
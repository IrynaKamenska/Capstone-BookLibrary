import React, {FormEvent} from "react";
import Modal from 'react-modal';
import axios from "axios";
import {Availability} from "./Availability";
import "./css/CreateBook.css";
import "../Modals.css"

type ModalProps = {
    modalIsOpen: boolean,
    closeModal: () => void,
    reloadAllBooks: () => void,

}
export default function CreateBook(props: ModalProps) {
    const [newBook, setNewBook] = React.useState(
        {
            title: "",
            author: "",
            isbn: [{type: "ISBN_10", identifier: ""}, {type: "ISBN_13", identifier: ""}],
            category: "",
            printType: "",
            pageCount: 0,
            availability: Availability.AVAILABLE,
            rentBookInfo: {
                rentByUsername: "",
                rentUntil: ""
            }
        }
    );


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/api/books", newBook)
            .catch((e) => console.log("POST Error: " + e))
            .then(props.reloadAllBooks)
            .then(props.closeModal)
        setNewBook({
            title: "",
            author: "",
            isbn: [{type: "", identifier: ""}],
            category: "",
            printType: "",
            pageCount: 0,
            availability: Availability.AVAILABLE,
            rentBookInfo: {
                rentByUsername: "",
                rentUntil: ""
            }
        });
    }

    function handleChange(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name] : event.target.value
        })
    }

    function handleChangeIsbn(event: any) {
        const {value, name} = event.target
        setNewBook({
            ...newBook,
            isbn: newBook.isbn.map( (current) => {
                if(current.type === name) {
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

    return (
        <Modal className="modal"
               isOpen={props.modalIsOpen}
               onRequestClose={props.closeModal}
               contentLabel="Example Modal"
               ariaHideApp={false}
               overlayClassName={"modal-overlay"}
        >
            <span className="modal-heading">Add book manually</span>
            <form onSubmit={addNewBook}>
                <label htmlFor="title">Title:</label>
                <input className="input-text" type="text"
                       id="title"
                       name="title"
                       value={newBook.title}
                       onChange={handleChange}
                       placeholder="title"/>
                <br/>
                <label htmlFor="author">Author:</label>
                <input className="input-text" type="text"
                       id="author"
                       name="author"
                       value={newBook.author}
                       onChange={handleChange}
                       placeholder="author"/>
                <br/>
                <label htmlFor="isbn">New ISBN_10:</label>
                <input className="input-text"
                       type="text"
                       id="isbn"
                       name="ISBN_10"
                       value={newBook.isbn[0] ? newBook.isbn[0].identifier : ""}

                       onChange={handleChangeIsbn}
                       placeholder="isbn"/>
                <br/>
                <label htmlFor="isbn">New ISBN_13:</label>
                <input className="input-text"
                       type="text"
                       id="isbn"
                       name="ISBN_13"
                       value={newBook.isbn[1] ? newBook.isbn[1].identifier : ""}
                       onChange={handleChangeIsbn}
                       placeholder="isbn"/>
                <br/>
                <label htmlFor="category">Category:</label>
                <input className="input-text" type="text"
                       id="category"
                       name="category"
                       value={newBook.category}
                       onChange={handleChange}
                       placeholder="category"/>
                <br/>
                <label htmlFor="printType">PrintType:</label>
                <input className="input-text" type="text"
                       id="printType"
                       name="printType"
                       value={newBook.printType}
                       onChange={handleChange}
                       placeholder="printType"/>
                <br/>
                <label htmlFor="pageCount">PageCount:</label>
                <input className="input-text" type="text"
                       id="pageCount"
                       name="pageCount"
                       value={newBook.pageCount}
                       onChange={handleChange}
                       placeholder="pageCount"/>
                <br/>
                <label htmlFor="availability">Availability:</label>
                <select className="selector" name="availability" id="availability">
                    <option value={Availability.AVAILABLE}>AVAILABLE</option>
                </select>
                <br/><br/>
                <div className="modal-body">
                    <h5>Create book?</h5>
                    <button className="button button-create">Create</button>
                    <button className="button button-cancel" onClick={() => props.closeModal()}>Cancel</button>
                </div>
            </form>
        </Modal>
    );

}

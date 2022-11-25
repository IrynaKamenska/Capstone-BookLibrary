import React, {useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import CreateBook from "./CreateBook";
import DeleteBook from "./DeleteBook";
import UpdateBook from "./UpdateBook";
import "./css/BookOverview.css";


function BookOverview() {
    const [books, setBooks] = useState<BookModel[]>([]);
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }

    useEffect(fetchAllBooks,[])

    const openModal = () => {
        setModalIsOpen(true)
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }

    return <>
        <CreateBook modalIsOpen={modalIsOpen} closeModal={closeModal} reloadAllBooks={fetchAllBooks}/>
        {books.length > 0 ?

            <>
                <table className="content-table">
                    <tbody>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>ISBN</th>
                        <th>Book State</th>
                        <th>Action</th>
                    </tr>
                    {books.map(book => {
                        return <tr key={book.id}>
                            <td>{book.title}</td>
                            <td>{book.author}</td>
                            <td>{book.isbn}</td>
                            <td>{book.bookState}</td>
                            <td>
                                <React.Fragment>
                                    <UpdateBook book={book} reloadAllBooks={fetchAllBooks}></UpdateBook>
                                    <DeleteBook book={book} reloadAllBooks={fetchAllBooks}></DeleteBook>
                                </React.Fragment>
                            </td>
                        </tr>;
                    })}
                    </tbody>
                </table>
                <button className="button-add" type={"submit"} onClick={openModal}>New Book</button>
            </>
                :
                <div>
                    <p>Keine Bücher vorhanden</p>
                    <button className="button-add" type={"submit"} onClick={openModal}>New Book</button>
                </div>
        }
    </>;
}


export default BookOverview;

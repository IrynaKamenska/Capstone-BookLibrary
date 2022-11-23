import React, {useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import CreateBookModal from "./CreateBookModal";



function BookOverview() {
    const [books, setBooks] = useState<BookModel[]>([]);
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)
    const [editMode, setEditMode] = useState({
        status: true,
        rowId: ""
    });

    const onEdit = (id: string) => {
        setEditMode({
            status: true,
            rowId: id
        })
    }

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

    const deleteBook = (id: string) => {
        axios.delete("/api/books/"+ id)
            .then(fetchAllBooks)
            .catch(error => console.error("DELETE Error: " + error))
    }


    return <>
        <CreateBookModal modalIsOpen={modalIsOpen} closeModal={closeModal} reloadAllBooks={fetchAllBooks}/>
        {books.length > 0 ?

            <>
                <div>
                    <table>
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
                                        <button className="left" onClick={() => onEdit(book.id)}>Edit</button>
                                        <button className="button-right" onClick={() => deleteBook(book.id)}>Delete</button>
                                    </React.Fragment>
                                </td>

                            </tr>;
                        })}
                        </tbody>
                    </table>
                </div>
                <button type={"submit"} onClick={openModal}>New Book</button>
            </>
                :
                <div>
                    <p>Keine Bücher vorhanden</p>
                    <button type={"submit"} onClick={openModal}>New Book</button>
                </div>
        }
    </>;
}


export default BookOverview;

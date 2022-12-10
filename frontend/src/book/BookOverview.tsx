import React, {useCallback, useState} from 'react';
import {BookModel} from "./BookModel";
import CreateBook from "./CreateBook";
import DeleteBook from "./DeleteBook";
import UpdateBook from "./UpdateBook";
import "./css/BookOverview.css";
import "../Buttons.css";
import "../Modals.css";
import AddBookManually from "./AddBookManually";
import RentBook from "./RentBook";

type BookOverviewProps = {
    books: BookModel[],
    fetchAllBooks: () => void
}

function BookOverview(props: BookOverviewProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)

    const openModal = useCallback(() => {
        setModalIsOpen(true)
    }, [])

    const closeModal = useCallback(() => {
        setModalIsOpen(false)
    }, [])

    return <>
        <CreateBook modalIsOpen={modalIsOpen} closeModal={closeModal} reloadAllBooks={props.fetchAllBooks}/>
        {props.books.length > 0 ?

            <>
                <div className="content-main-div">
                    <h2>Overview of all Books</h2>
                    <table className="content-table">
                        <tbody>
                        <tr>
                            <th>Cover</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>ISBN</th>
                            <th>Availability</th>
                            <th>Action</th>
                        </tr>
                        {props.books.map(book => {
                            return <tr key={book.id}>
                                <td className="db-list-cover-td"><img className="db-list-cover-img" src={book.cover} alt="Dummy-Cover"/></td>
                                <td>{book.title}</td>
                                <td>{book.author}</td>
                                <td>{book.isbn}</td>
                                <td>{book.availability === "AVAILABLE" ?
                                    <span className="content-available">Available</span>
                                    :
                                    <span className="content-not-available">Not Available</span>
                                }
                                </td>

                                <td>
                                    <React.Fragment>
                                        <UpdateBook book={book} reloadAllBooks={props.fetchAllBooks}></UpdateBook>
                                        <DeleteBook book={book} reloadAllBooks={props.fetchAllBooks}></DeleteBook>
                                        <RentBook book={book} reloadAllBooks={props.fetchAllBooks}/>
                                    </React.Fragment>
                                </td>
                            </tr>;
                        })}
                        </tbody>
                    </table>
                    <AddBookManually openModal={openModal}></AddBookManually>
                </div>
            </>
                :
                <div className="content-main-div">
                    <p>Library is empty :-(</p>
                    <AddBookManually openModal={openModal}></AddBookManually>
                </div>
        }
    </>;
}


export default BookOverview;

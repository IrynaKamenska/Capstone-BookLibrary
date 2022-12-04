import React, {useState} from 'react';
import {BookModel} from "../model/BookModel";
import CreateBook from "../service/CreateBook";
import DeleteBook from "../service/DeleteBook";
import UpdateBook from "../service/UpdateBook";
import "../css/BookOverview.css";

type BookOverviewProps = {
    books: BookModel[],
    fetchAllBooks: () => void
}

function BookOverview(props: BookOverviewProps) {
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false)

    const openModal = () => {
        setModalIsOpen(true)
    }

    const closeModal = () => {
        setModalIsOpen(false)
    }

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
                                {/*<td>{book.availability}</td>*/}
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
                                    </React.Fragment>
                                </td>
                            </tr>;
                        })}
                        </tbody>
                    </table>
                    <h2>Add new Books</h2>
                    <p>Add book manually via form: </p>
                    <button className="button-add" type={"submit"} onClick={openModal}>Manually add new Book</button>
                </div>
            </>
                :
                <div className="content-main-div">
                    <p>Library is empty :-(</p>
                    <h2>Add new Books</h2>
                    <p>Add book manually via form: </p>
                    <button className="button-add" type={"submit"} onClick={openModal}>Manually add new Book</button>
                </div>
        }
    </>;
}


export default BookOverview;

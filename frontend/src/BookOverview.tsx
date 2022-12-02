import React, {useState} from 'react';
import {BookModel} from "./api/model/BookModel";
import CreateBook from "./CreateBook";
import DeleteBook from "./DeleteBook";
import UpdateBook from "./UpdateBook";
import "./css/BookOverview.css";

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
                <table className="content-table">
                    <tbody>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>ISBN</th>
                        <th>Book State</th>
                        <th>Action</th>
                    </tr>
                    {props.books.map(book => {
                        return <tr key={book.id}>
                            <td>{book.title}</td>
                            <td>{book.author}</td>
                            <td>{book.isbn}</td>
                            <td>{book.bookState}</td>
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
                <button className="button-add" type={"submit"} onClick={openModal}>Add Book</button>
            </>
                :
                <div>
                    <p>Keine Bücher vorhanden</p>
                    <button className="button-add" type={"submit"} onClick={openModal}>Add Book</button>
                </div>
        }
    </>;
}


export default BookOverview;

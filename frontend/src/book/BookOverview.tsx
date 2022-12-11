import React, {useState} from 'react';
import {BookModel} from "./BookModel";

import DeleteBook from "./DeleteBook";
import UpdateBook from "./UpdateBook";
import "./css/BookOverview.css";
import "../Buttons.css";
import "../Modals.css";
import AddBookManually from "./AddBookManually";
import RentBook from "./RentBook";
import ReturnBook from "./ReturnBook";

type BookOverviewProps = {
    books: BookModel[],
    fetchAllBooks: () => void
}

function BookOverview(props: BookOverviewProps) {

    const [filterText, setFilterText] = useState<string>("")
    const filteredBooks = props.books.filter(
        (book) => book.title.toLowerCase().includes(filterText.toLowerCase()));

    return <>
        <p>Search Books in DB: {filterText}</p>
        <input className={"search-input"} onChange={(event) => setFilterText(event.target.value)}/>
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
                            <th>RentedBy</th>
                            <th>Action</th>
                        </tr>
                        {filteredBooks.map((book) => {
                            return <tr key={book.id}>
                                <td className="db-list-cover-td"><img className="db-list-cover-img" src={book.cover}
                                                                      alt="Dummy-Cover"/></td>
                                <td>{book.title}</td>
                                <td>{book.author}</td>
                                <td>{book.isbn}</td>
                                <td>{book.availability === "AVAILABLE" ?
                                    <span className="content-available">Available</span>
                                    :
                                    <span className="content-not-available">Not Available</span>
                                }
                                </td>
                                <td>{book.rentedBy}</td>

                                <td>
                                    <React.Fragment>
                                        <UpdateBook book={book} reloadAllBooks={props.fetchAllBooks}></UpdateBook>
                                        <DeleteBook book={book} reloadAllBooks={props.fetchAllBooks}></DeleteBook>
                                        {book.availability === "AVAILABLE" ?
                                            <RentBook book={book} reloadAllBooks={props.fetchAllBooks}/>
                                            :
                                            <ReturnBook book={book} reloadAllBooks={props.fetchAllBooks}/>
                                        }
                                    </React.Fragment>
                                </td>
                            </tr>;
                        })}
                        </tbody>
                    </table>
                </div>
            </>
                :
                <div className="content-main-div">
                    <p>Library is empty :-(</p>
                </div>
        }

    </>;
}


export default BookOverview;

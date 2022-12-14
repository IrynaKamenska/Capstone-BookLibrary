import React, {useState} from 'react';
import {BookModel} from "./BookModel";

import DeleteBook from "./DeleteBook";
import UpdateBook from "./UpdateBook";
import "./css/BookOverview.css";
import "../Buttons.css";
import "../Modals.css";
import RentBook from "./RentBook";
import ReturnBook from "./ReturnBook";
import {AppUserInfo} from "../security/AppUserInfo";

type BookOverviewProps = {
    books: BookModel[],
    fetchAllBooks: () => void
    appUserInfo: AppUserInfo
}

function BookOverview(props: BookOverviewProps) {

    const [filterText, setFilterText] = useState<string>("")
    const filteredBooks = props.books.filter(
        (book) => book.title.toLowerCase().includes(filterText.toLowerCase()));

    return <>

        <div className="content-main-div">
            <h2>Overview of all Books in Library</h2>
            <span>Filter Books: </span>
            <input className={"filter-input"} onChange={(event) => setFilterText(event.target.value)}/>
            {props.books.length > 0 ?

                <>
                    <table className="content-table">
                        <tbody>
                        <tr>
                            <th>Cover</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>ISBN</th>
                            <th>Availability</th>
                            {props.appUserInfo.role === "LIBRARIAN" ?
                                <>
                                    <th>Rented</th>
                                    <th>Action</th>
                                </> : ""}
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

                                {props.appUserInfo.role === "LIBRARIAN" ?
                                    <>
                                        <td>
                                        {book.rentBookInfo.rentUntil.toString() !== "" &&
                                            <>by: {book.rentBookInfo.rentByUsername} <br/> until: {book.rentBookInfo.rentUntil.toString().substring(0, 10)}
                                            <br/>{book.rentBookInfo.rentUntil.toString().substring(11, 16)} Uhr
                                            </>
                                        }
                                        </td>
                                        <td>
                                            <React.Fragment>
                                                <UpdateBook book={book}
                                                            reloadAllBooks={props.fetchAllBooks}></UpdateBook>
                                                <DeleteBook book={book}
                                                            reloadAllBooks={props.fetchAllBooks}></DeleteBook>
                                                {book.availability === "AVAILABLE" ?
                                                    <RentBook book={book} reloadAllBooks={props.fetchAllBooks}/>
                                                    :
                                                    <ReturnBook book={book} reloadAllBooks={props.fetchAllBooks}/>
                                                }
                                            </React.Fragment>
                                        </td>
                                    </>
                                    :
                                    ""}
                            </tr>;
                        })}
                        </tbody>
                    </table>
                </>
                :
                <p>Library is empty :-(</p>

            }
        </div>
    </>;
}


export default BookOverview;

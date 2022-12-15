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
            <div className={"book-cards"}>
                {props.books.length > 0 ?

                    <>
                        {filteredBooks.map((book) => {
                            return <>
                                <div className={"book-card"}>
                                    <div className={"book-card-inner"}>
                                        <div className="book-cover-div">
                                            <img className="book-cover" src={book.cover} alt={book.title}/>
                                        </div>
                                        <div className="book-info-div">
                                            <h3 className="book-title">{book.title}</h3>
                                            <div className="book-info-left-div">
                                                <p className="book-author">by {book.author}</p>
                                                {book.isbn.map(current => {
                                                    return (
                                                        <p className="book-isbn"
                                                           key={current.identifier}>{current.type}: {current.identifier}</p>
                                                    )
                                                })
                                                }
                                            </div>
                                            <div className="book-info-center-div">
                                                <p className="book-isbn">Category: {book.category}</p>
                                                <p className="book-isbn">PrintType: {book.printType}</p>
                                                <p className="book-isbn">PageCount: {book.pageCount}</p>
                                            </div>
                                            <div className="book-info-right-div">
                                                <p className="book-isbn">{book.availability === "AVAILABLE" ?
                                                    <span className="content-available">Available</span>
                                                    :
                                                    <span className="content-not-available">Not Available</span>
                                                }
                                                </p>
                                                {props.appUserInfo.role === "LIBRARIAN" ?
                                                    <>
                                                        {book.rentBookInfo.rentUntil.toString() !== "" &&
                                                            <>
                                                                <p className="book-isbn">
                                                                    rented by: {book.rentBookInfo.rentByUsername}
                                                                </p>
                                                                <p className="book-isbn">
                                                                    until: {book.rentBookInfo.rentUntil.toString().substring(0, 10)} {book.rentBookInfo.rentUntil.toString().substring(11, 16)} Uhr
                                                                </p>
                                                            </>
                                                        }
                                                    </>
                                                    :
                                                    ""}
                                            </div>
                                        </div>

                                    </div>
                                    {props.appUserInfo.role === "LIBRARIAN" ?
                                        <>
                                            <p>
                                                <React.Fragment>
                                                    <UpdateBook book={book}
                                                                reloadAllBooks={props.fetchAllBooks}></UpdateBook>
                                                    <DeleteBook book={book}
                                                                reloadAllBooks={props.fetchAllBooks}></DeleteBook>
                                                    {book.availability === "AVAILABLE" ?
                                                        <RentBook book={book} reloadAllBooks={props.fetchAllBooks}/>
                                                        :
                                                        <ReturnBook book={book}
                                                                    reloadAllBooks={props.fetchAllBooks}/>
                                                    }
                                                </React.Fragment>
                                            </p>
                                        </>
                                        :
                                        ""}
                                </div>
                            </>
                        })}
                    </>
                    :
                    <p>Library is empty :-(</p>
                }
            </div>
        </div>
    </>
}

export default BookOverview;

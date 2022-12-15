import React, {useCallback, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import "../api/css/GetBooksFromApi.css";
import "../book/css/BookCard.css";

function GetRentedBooks() {
    const [books, setBooks] = useState<BookModel[]>([]);

    const getRentedBooks = useCallback(() => {
        axios.get("/api/books/rentedBooks")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }, [])

    useEffect(getRentedBooks, [getRentedBooks])

    return (
        <>
            <div className="content-main-div">
                <h2>My Rented Books:</h2>
                <div className={"book-cards"}>
                    {Array.isArray(books)
                        ? books.map((current) => <div key={current.id} className={"book-card"}>
                            <div className={"book-card-inner"}>
                                <div className="book-cover-div">
                                    <img className="book-cover" src={current.cover} alt={current.title}/>
                                </div>
                                <div className="book-info-div">
                                    <h3 className="book-title">Title: {current.title}</h3>
                                    <div className="book-info-left-div">
                                        <p className="book-author">Author: {current.author}</p>
                                        <p className="book-isbn">{current.isbn.map(current => {
                                            return (
                                                <p className="book-isbn"
                                                   key={current.identifier}>{current.type}: {current.identifier}</p>
                                            )
                                        })
                                        }</p>
                                    </div>
                                    <div className="book-info-center-div">
                                        <p className="book-isbn">Category: {current.category}</p>
                                        <p className="book-isbn">PrintType: {current.printType}</p>
                                        <p className="book-isbn">PageCount: {current.pageCount}</p>
                                    </div>
                                    <div className="book-info-right-div">
                                        <p className="book-isbn">Rented by: {current.rentBookInfo.rentByUsername}</p>
                                        {current.rentBookInfo.rentUntil.toString() !== "" &&
                                            <p className="book-author">Rented
                                                until: {current.rentBookInfo.rentUntil.toString().substring(0, 10)} {current.rentBookInfo.rentUntil.toString().substring(11, 16)} Uhr</p>}
                                    </div>
                                </div>
                            </div>
                        </div>)
                        : ""}
                </div>
            </div>
        </>
    );
}

export default GetRentedBooks;
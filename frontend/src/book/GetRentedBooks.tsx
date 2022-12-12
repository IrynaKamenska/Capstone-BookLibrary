import React, {useCallback, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import "../api/css/GetBooksFromApi.css";

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

        <div className={"book-cards"}>
            <p>My Rented Books:</p>
            {Array.isArray(books)
                ? books.map((current) =>
                    <div key={current.id} className={"book-card"}>
                        <div className={"book-card-inner"}>
                            <div className="book-cover-div">
                                <img className="book-cover" src={current.cover} alt={current.title}/>
                            </div>
                            <h3 className="book-title">Title: {current.title}</h3>
                            <p className="book-author">Author: {current.author}</p>
                            <p className="book-isbn">ISBN: {current.isbn}</p>
                            <p className="book-isbn">Rented by:{current.rentBookInfo.rentByUsername}</p>
                            <p className="book-isbn">Rented by:{current.rentBookInfo.rentUntil}</p>
                        </div>
                    </div>)
                : ""}
        </div>
    );
}

export default GetRentedBooks;
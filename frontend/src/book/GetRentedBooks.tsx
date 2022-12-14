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
                            <p className="book-isbn">ISBN: {current.isbn.map(item => item.identifier)}</p>
                            <p className="book-author">Category: {current.category}</p>
                            <p className="book-author">PrintType: {current.printType}</p>
                            <p className="book-author">PageCount: {current.pageCount}</p>
                            <p className="book-author">Rented by:{current.rentBookInfo.rentByUsername}</p>
                            {current.rentBookInfo.rentUntil.toString() !== "" &&
                                <h4 className="book-author">Rented until: {current.rentBookInfo.rentUntil.toString().substring(0, 10)} {current.rentBookInfo.rentUntil.toString().substring(11, 16)} Uhr</h4>
                            }
                        </div>
                    </div>)
                : ""}
        </div>
    );
}

export default GetRentedBooks;
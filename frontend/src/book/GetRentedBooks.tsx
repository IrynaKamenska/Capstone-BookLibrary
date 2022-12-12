import React, {useCallback, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import BookCard from "./BookCard";
import "../api/css/GetBooksFromApi.css";

type GetRentedBooksProps = {
    reloadAllBooks: () => void;
}


function GetRentedBooks(props: GetRentedBooksProps) {
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
            <p>Rented Books by me:</p>
            {Array.isArray(books)
                ? books.map((current) =>
                    <div className={"book-card"}>
                        <BookCard key={current.id} book={current} reloadAllBooks={props.reloadAllBooks}/>
                    </div>)
                : ""}
        </div>
    );
}

export default GetRentedBooks;
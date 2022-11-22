import React, {useEffect, useState} from 'react';
import {BookModel} from "./BookModel";

import axios from "axios";


function BookOverview() {
    const [books, setBooks] = useState<BookModel[]>([]);


    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(data => setBooks(data))
    }


    useEffect(fetchAllBooks,[])


    return <>
            <p>Book List:</p>
        <section>
            <ul>
                {books.map((book) => {
                    return <li key={book.isbn}>
                        {book.title},
                        {book.author},
                        {book.isbn},
                        {book.bookState}</li>
                })}
            </ul>
        </section>
    </>;
}

export default BookOverview;
import React, {useEffect, useState} from 'react';
import './App.css';
import axios from "axios";
import {BookModel} from "./BookModel";

function App() {

    const [books, setBooks] = useState<BookModel[]>([]);

    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.log("GET Error: " + error))
            .then(data => setBooks(data))
    }


    useEffect(() => {
        fetchAllBooks()
    }, [])


    const bookList = books.map((book) =>
        <li>Title {book.title}
            Author {book.author}
            ISBN {book.isbn}
            State {book.bookState}
        </li>
    )
    return <>
        <h1>Books</h1>
        <ul>{bookList}</ul>
    </>;
}

export default App;

import React, {useEffect, useState} from 'react';
import './css/App.css';
import BookOverview from "./BookOverview";
import GetBooksFromApi from "./api/service/GetBooksFromApi";
import axios from "axios";
import {BookModel} from "./BookModel";

function App() {
    const [books, setBooks] = useState<BookModel[]>([]);

    const fetchAllBooks = () => {
        axios.get("/api/books/")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }
    useEffect(fetchAllBooks, [])
    return <>
        <h1>Book Library</h1>
        <main>
            <BookOverview/>
            <GetBooksFromApi reloadAllBooks={fetchAllBooks}/>
        </main>
    </>;

}

export default App;

import React, {useEffect, useState} from 'react';
import './css/App.css';
import BookOverview from "./components/BookOverview";
import GetBooksFromApi from "./service/GetBooksFromApi";
import axios from "axios";
import {BookModel} from "./model/BookModel";

function App() {
    const [books, setBooks] = useState<BookModel[]>([]);

    const fetchAllBooks = () => {
        axios.get("/api/books/")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }


    console.log("BOOKS:" + books);
    useEffect(fetchAllBooks, [])

    return <>
        <h1>Book Library</h1>
        <main>
            <BookOverview books={books} fetchAllBooks={fetchAllBooks}/>
            <GetBooksFromApi reloadAllBooks={fetchAllBooks}/>
        </main>
    </>;

}

export default App;

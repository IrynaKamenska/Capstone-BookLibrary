import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import {BookState} from "./BookState";

function Books() {

    const [books, setBooks] = useState<BookModel[]>([]);
    const[bookTitle, setBookTitle] = useState<string>("");
    const[bookAuthor, setAuthor] = useState<string>("");
    const[bookIsbn, setBookIsbn] = useState<string>("");
    const[bookState, setState] = useState<BookState>(BookState.AVAILABLE);

    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.log("GET Error: " + error))
            .then(data => setBooks(data))
    }


    useEffect(() => {
        fetchAllBooks()
    }, [])


    function handleTitle(event: ChangeEvent<HTMLInputElement>) {
        setBookTitle(event.target.value);
    }

    function handleAuthor(event: ChangeEvent<HTMLInputElement>) {
        setAuthor(event.target.value);
    }

    function handleIsbn(event: ChangeEvent<HTMLInputElement>) {
        setBookIsbn(event.target.value);
    }

    function handleState(event: ChangeEvent<HTMLInputElement>) {
        setState(BookState.AVAILABLE);
    }


    const handleAddBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/api/books", {
            title: bookTitle,
            author: bookAuthor,
            isbn: bookIsbn,
            status: bookState
        })
            .catch((error) => console.log("POST Error: " + error))
        setBookTitle("")
        setAuthor("")
        setBookIsbn("")
        setState(BookState.AVAILABLE)
    }

    return (
        <section>
            <h1>Books</h1>
            <ul>
                {books.map((book) => {
                    return <li>Title: {book.title}
                        Author: {book.author}
                        ISBN: {book.isbn}
                        State: {book.bookState}</li>
                })}
            </ul>
            <form onSubmit={handleAddBook}>
                <input value={bookTitle} onChange={handleTitle}/>
                <input value={bookAuthor} onChange={handleAuthor}/>
                <input value={bookIsbn} onChange={handleIsbn}/>
                <input value={bookState} onChange={handleState}/>
                <button type={"submit"}>Add Book</button>
            </form>
        </section>
    );
}

export default Books;
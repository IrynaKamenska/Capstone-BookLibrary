import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {BookModel} from "./BookModel";
import axios from "axios";
import {BookState} from "./BookState";

function Books() {

    const [books, setBooks] = useState<BookModel[]>([]);
    const [bookTitle, setBookTitle] = useState<string>("");
    const [bookAuthor, setAuthor] = useState<string>("");
    const [bookIsbn, setBookIsbn] = useState<string>("");
    const [bookState, setState] = React.useState(BookState.AVAILABLE);

    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.log("GET Error: " + error))
            .then(data => setBooks(data))
    }


    useEffect(fetchAllBooks,[])


    function handleTitle(event: ChangeEvent<HTMLInputElement>) {
        setBookTitle(event.target.value);
    }

    function handleAuthor(event: ChangeEvent<HTMLInputElement>) {
        setAuthor(event.target.value);
    }

    function handleIsbn(event: ChangeEvent<HTMLInputElement>) {
        setBookIsbn(event.target.value);
    }



    const handleAddBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if (!bookTitle || !bookAuthor || !bookIsbn || !bookState) {
            alert(`Please fill book title, author, isbn and state`)
            return
        }

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
                    return <li key={book.isbn}>
                        {book.title},
                        {book.author},
                        {book.isbn},
                        {book.bookState}</li>
                })}
            </ul>



            <form onSubmit={handleAddBook}>
                <br/>
                <label htmlFor="title">Book Title</label>
                <input type="title" id="title" onChange={handleTitle}/>
                <br/>
                <label htmlFor="author">Book Author</label>
                <input type="author" id="author" onChange={handleAuthor}/>
                <br/>
                <label htmlFor="isbn">Book ISBN</label>
                <input type="isbn" id="isbn" onChange={handleIsbn}/>
                <br/>
                <label htmlFor="state">Book State:</label>
                <select name="state" id="state">
                    <option value={BookState.AVAILABLE}>AVAILABLE</option>
                    <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/>
                <button type={"submit"}>Add Book</button>
            </form>
        </section>
    );
}

export default Books;